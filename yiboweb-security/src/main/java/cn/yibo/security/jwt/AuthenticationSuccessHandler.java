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

import cn.yibo.core.protocol.ResponseTs;
import cn.yibo.security.SecurityUserDetails;
import com.yibo.modules.base.entity.Log;
import com.yibo.modules.base.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  描述: 登录成功处理类
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private JWTUtil jwtUtil;

    public AuthenticationSuccessHandler(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityUserDetails userDetails = ((SecurityUserDetails)authentication.getPrincipal());
        String token = jwtUtil.genAccessToken(userDetails);
        ResponseTs.outResponse(response, token);
        LogUtils.saveLog(userDetails, request, "系统登录", Log.TYPE_LOGIN);
    }
}
