/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：JWTUtils.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.security.jwt;

import com.yibo.security.constant.SecurityConstant;
import com.yibo.common.lang.StringUtils;
import com.yibo.common.web.http.ServletUtils;
import com.yibo.security.SecurityUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: JWT工具类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-09-29
 * 版本: v1.0
 */
@Slf4j
@Component
public class JWTUtil {
    @Value("${webapp.token-expire-time}")
    private Integer tokenExpireTime;

    @Value("${webapp.save-login-time}")
    private Integer saveLoginTime;

    /**
     * 生成JWT Token
     * @param user
     * @return
     */
    public String genAccessToken(SecurityUserDetails user){
        String saveTime = ServletUtils.getRequest().getParameter(SecurityConstant.SAVE_LOGIN);
        if( StringUtils.isNotBlank(saveTime) && Boolean.valueOf(saveTime) ){
            tokenExpireTime = saveLoginTime * 60 * 24;
        }

        Map<String, Object> claims = genClaims(user);
        long expiration = System.currentTimeMillis() + tokenExpireTime * 60 * 1000;
        return generateAccessToken(user.getUsername(), claims, expiration);
    }

    /**
     * 生成JWT Token
     * @param subject
     * @param claims
     * @param expiration
     * @return
     */
    private String generateAccessToken(String subject, Map<String, Object> claims, long expiration){
        return SecurityConstant.TOKEN_SPLIT + generateToken(subject, claims, expiration);
    }

    /**
     * 生成JWT Token
     * @param subject
     * @param claims
     * @param expiration
     * @return
     */
    private String generateToken(String subject, Map<String, Object> claims, long expiration){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                .compact();
    }

    /**
     * Token自定义属性
     * @param user
     * @return
     */
    private Map<String, Object> genClaims(SecurityUserDetails user){
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstant.CLAIM_KEY_USER_ID, user.getId());
        return claims;
    }

    /**
     * JWT Token解析
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token){
        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(SecurityConstant.JWT_SIGN_KEY)
                    .parseClaimsJws(token.replace(SecurityConstant.TOKEN_SPLIT, ""))
                    .getBody();
        }catch(ExpiredJwtException e){
            throw e;
        }
        return claims;
    }

    public String getUserIdFromToken(String token){
        String userId;
        try{
            final Claims claims = getClaimsFromToken(token);
            userId = claims.get(SecurityConstant.CLAIM_KEY_USER_ID).toString();
        }catch(Exception e){
            userId = "";
        }
        return userId;
    }

    public String getUsernameFromToken(String token){
        String username;
        try{
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        }catch(Exception e){
            username = null;
        }
        return username;
    }

    public Date getExpirationDateFromToken(String token){
        Date expiration;
        try{
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        }catch(Exception e){
            expiration = null;
        }
        return expiration;
    }

    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, SecurityUserDetails user){
        final String userId = getUserIdFromToken(token);
        final String userName = getUsernameFromToken(token);
        return userId.equals(user.getId()) && userName.equals(user.getUsername()) && !isTokenExpired(token);
    }
}
