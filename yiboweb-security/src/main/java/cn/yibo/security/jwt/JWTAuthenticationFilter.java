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
import cn.yibo.security.SecurityUserDetails;
import cn.yibo.security.constant.SecurityConstant;
import cn.yibo.security.exception.LoginFailEnum;
import cn.yibo.security.exception.LoginFailLimitException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  描述: 登录认证过滤器
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter{
    private JWTUtil jwtUtil;

    private UserDetailsService userDetailsService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService){
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        String headToken = request.getHeader(SecurityConstant.HEADER);

        if( StrUtil.isEmpty(headToken) ){
            headToken = request.getParameter(SecurityConstant.HEADER);
        }

        if( StrUtil.isEmpty(headToken) || (!jwtUtil.isTokenRedis() && !headToken.startsWith(SecurityConstant.TOKEN_SPLIT)) ){
            chain.doFilter(request, response);
            return;
        }

        try{
            UsernamePasswordAuthenticationToken authentication = getAuthentication(headToken, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(Exception exception){
            log.error(exception.getMessage());

            if( exception instanceof LoginFailLimitException || exception instanceof SignatureException || exception instanceof ExpiredJwtException ){
                ResponseTs.outResponseException(response, new BusinessException(LoginFailEnum.LOGIN_EXPIRED_ERROR.getCode(), LoginFailEnum.LOGIN_EXPIRED_ERROR.getDesc()));
            }else{
                ResponseTs.outResponseException(response, new BusinessException(LoginFailEnum.LOGIN_FAIL_ERROR.getCode(), exception.getMessage()));
            }
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     *  解析token
     * @param headToken
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String headToken, HttpServletRequest request){
        log.info("============================================【JWTAuthenticationFilter validateToken：" + headToken+"】");
        String username = jwtUtil.validateToken(headToken);

        if( StrUtil.isNotBlank(username) ) {
            SecurityUserDetails userDetails = (SecurityUserDetails)userDetailsService.loadUserByUsername(username);

            String tenantId = request.getParameter(SecurityConstant.TENANT_KEY);
            if( userDetails != null && userDetails.isSuperAdmin() && StrUtil.isNotBlank(tenantId)){
                userDetails.setTenantId(tenantId);
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            return authentication;
        }
        return null;
    }

}

