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

import cn.yibo.common.lang.StringUtils;
import cn.yibo.security.SecurityUserDetails;
import cn.yibo.security.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  描述: JWT认证过滤器
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter{
    private JWTUtil jwtUtil;

    private UserDetailsService userDetailsService;

    private HandlerExceptionResolver handlerExceptionResolver;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint){
        super(authenticationManager, authenticationEntryPoint);
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceptionResolver){
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        String headToken = request.getHeader(SecurityConstant.HEADER);

        if( StringUtils.isBlank(headToken) ){
            headToken = request.getParameter(SecurityConstant.HEADER);
        }

        if( StringUtils.isBlank(headToken) || (!jwtUtil.isTokenRedis() && !headToken.startsWith(SecurityConstant.TOKEN_SPLIT)) ){
            chain.doFilter(request, response);
            return;
        }

        try{
            UsernamePasswordAuthenticationToken authentication = getAuthentication(headToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        // 可优化...
        }catch(Exception exception){
            log.error(exception.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
        chain.doFilter(request, response);
    }

    /**
     *  解析token
     * @param headToken
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String headToken){
        String username = jwtUtil.validateToken(headToken);

        // 可优化...
        if( StringUtils.isNotBlank(username) ) {
            SecurityUserDetails userDetails = (SecurityUserDetails)userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            return authentication;
        }
        return null;
    }

}

