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
{  注：本模块代码为底层基础框架封装的boot包
{*****************************************************************************
*/

package cn.yibo.boot.config.security;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.common.exception.LoginFailEnum;
import cn.yibo.boot.common.exception.LoginFailLimitException;
import cn.yibo.boot.modules.base.entity.Log;
import cn.yibo.boot.modules.base.entity.User;
import cn.yibo.boot.modules.base.service.UserService;
import cn.yibo.boot.modules.base.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *  描述: 一句话描述该类的用途
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String flagKey = "loginFailFlag:"+username;
        String value = redisTemplate.opsForValue().get(flagKey);
        Long timeRest = redisTemplate.getExpire(flagKey, TimeUnit.MINUTES);

        // 超过限制次数
        if( StrUtil.isNotBlank(value) ){
            throw new LoginFailLimitException("登录错误次数超过限制，请"+timeRest+"分钟后再试");
        }

        User user = userService.findByUsername(username);
        if( user == null ){
            throw new UsernameNotFoundException(LoginFailEnum.INCORRECT_ERROR.getDesc());
        }
        return new SecurityUserDetails(user);
    }

    public void saveLoginInfo(HttpServletRequest request, User user){
        Map<String, Object> entityMap = MapUtil.newHashMap();
        if( user.getFirstVisitDate() == null ){
            entityMap.put("firstVisitDate", new Date());
        }
        entityMap.put("id", user.getId());
        entityMap.put("lastVisitDate", new Date());
        userService.updateMap(entityMap);

        LogUtils.saveLog(user, request, "系统登录", Log.TYPE_LOGIN);
    }
}
