/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：安全控制模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package cn.yibo.security.jwt;

import cn.hutool.core.util.StrUtil;
import cn.yibo.core.protocol.ResponseTs;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.exception.LoginFailEnum;
import cn.yibo.security.exception.LoginFailLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *  描述: 认证失败回调
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${webapp.login-time-limit}")
    private Integer loginTimeLimit;

    @Value("${webapp.login-after-time}")
    private Integer loginAfterTime;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private LoginFailEnum loginFailEnum = LoginFailEnum.LOGIN_FAIL_ERROR;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if( e instanceof UsernameNotFoundException || e instanceof BadCredentialsException ) {
            String username = request.getParameter("username");
            recordLoginTime(username);
            String key = "loginTimeLimit:" + username;
            String value = StrUtil.emptyToDefault(redisTemplate.opsForValue().get(key), "0");

            //获取已登录错误次数
            int loginFailTime = Integer.parseInt(value);
            int restLoginTime = loginTimeLimit - loginFailTime;
            log.info("用户" + username + "登录失败，还有" + restLoginTime + "次机会");

            loginFailEnum = LoginFailEnum.INCORRECT_ERROR;
            if( restLoginTime <= 3 && restLoginTime > 0 ){
                ResponseTs.outResponseException(response, new BusinessException(loginFailEnum.getCode(), loginFailEnum.getDesc()+"，还有" + restLoginTime + "次尝试机会"));
            }else if( restLoginTime <= 0 ){
                loginFailEnum = LoginFailEnum.LOGIN_FAIL_LIMIT_ERROR;
                ResponseTs.outResponseException(response, new BusinessException(loginFailEnum.getCode(), loginFailEnum.getDesc()+"，请" + loginAfterTime + "分钟后再试"));
            }else{
                ResponseTs.outResponseException(response, new BusinessException(loginFailEnum.getCode(), loginFailEnum.getDesc()));
            }
        }else if(e instanceof DisabledException || e instanceof LockedException){
            loginFailEnum = LoginFailEnum.DISABLED_ERROR;
            ResponseTs.outResponseException(response, new BusinessException(loginFailEnum.getCode(), loginFailEnum.getDesc()));
        }else if(e instanceof LoginFailLimitException){
            ResponseTs.outResponseException(response, new BusinessException(loginFailEnum.getCode(), ((LoginFailLimitException)e).getMsg()));
        }else{
            ResponseTs.outResponseException(response, new BusinessException(loginFailEnum.getCode(), loginFailEnum.getDesc(), e.getMessage()));
        }
    }

    /**
     * 判断用户登陆错误次数
     */
    public boolean recordLoginTime(String username) {
        String key = "loginTimeLimit:" + username;
        String flagKey = "loginFailFlag:" + username;
        String value = StrUtil.emptyToDefault(redisTemplate.opsForValue().get(key),"0");

        // 获取已登录错误次数
        int loginFailTime = Integer.parseInt(value) + 1;
        redisTemplate.opsForValue().set(key, String.valueOf(loginFailTime), loginAfterTime, TimeUnit.MINUTES);
        if( loginFailTime >= loginTimeLimit ){
            redisTemplate.opsForValue().set(flagKey, "fail", loginAfterTime, TimeUnit.MINUTES);
            return false;
        }
        return true;
    }
}
