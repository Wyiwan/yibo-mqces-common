/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：JWTAuthenticationFilter.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package cn.yibo.security.jwt;

import cn.yibo.common.lang.StringUtils;
import cn.yibo.core.protocol.ResponseTs;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.SecurityUserDetails;
import cn.yibo.security.constant.SecurityConstant;
import cn.yibo.security.exception.LoginFailEnum;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService){
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint){
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        String header = request.getHeader(SecurityConstant.HEADER);

        if( StringUtils.isBlank(header) ){
            header = request.getParameter(SecurityConstant.HEADER);
        }

        if( StringUtils.isBlank(header) || !header.startsWith(SecurityConstant.TOKEN_SPLIT) ){
            chain.doFilter(request, response);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(Exception e){
            e.toString();
        }
        chain.doFilter(request, response);
    }

    /**
     *  解析token
     * @param request
     * @param response
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader(SecurityConstant.HEADER);

        try{
            if( StringUtils.isNotBlank(token) ) {
                final String authToken = token.substring(SecurityConstant.TOKEN_SPLIT.length());
                String username = jwtUtil.getUsernameFromToken(authToken);
                logger.info("checking authentication " + username);

                if( StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityUserDetails userDetails = (SecurityUserDetails)userDetailsService.loadUserByUsername(username);

                    // 验证Token
                    if( jwtUtil.validateToken(authToken, userDetails) ){
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        logger.info("authenticated user " + username + ", setting security context");
                        return authentication;
                    }
                }
            }
        }catch(ExpiredJwtException e){
            throw new BusinessException("000100", "登录已失效，请重新登录");
        }catch(Exception e){
            ResponseTs.outResponseException(response, new BusinessException(LoginFailEnum.TOKEN_ERROR.getCode(), LoginFailEnum.TOKEN_ERROR.getDesc()));
        }
        return null;
    }

}

