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

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.common.web.ServletUtils;
import cn.yibo.security.SecurityUserDetails;
import cn.yibo.security.constant.SecurityConstant;
import cn.yibo.security.exception.LoginFailEnum;
import cn.yibo.security.exception.LoginFailLimitException;
import com.alibaba.fastjson.JSON;
import com.yibo.modules.base.entity.TokenUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Value("${webapp.token-save-redis}")
    private Boolean tokenRedis;

    @Value("${webapp.token-expire-time}")
    private Integer tokenExpireTime;

    @Value("${webapp.save-login-time}")
    private Integer saveLoginTime;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 生成JWT Token
     * @param user
     * @return
     */
    public String genAccessToken(SecurityUserDetails user){
        String username = user.getUsername();

        // 删除错误次数记录
        redisTemplate.delete("loginTimeLimit:"+username);

        // 如果Redis存储Token
        if( tokenRedis ){
            return generateRedisToken(username);
        }else{
            // 保存登录的过期时间
            String saveLogin = ServletUtils.getRequest().getParameter(SecurityConstant.SAVE_LOGIN);
            if( StrUtil.isNotBlank(saveLogin) && Boolean.valueOf(saveLogin) ){
                tokenExpireTime = saveLoginTime * 60 * 24;
            }

            Map<String, Object> claims = genClaims(user);
            long expiration = System.currentTimeMillis() + tokenExpireTime * 60 * 1000;
            return generateAccessToken(username, claims, expiration);
        }
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
     * 生成Token保存在Redis中
     * @param username
     * @return
     */
    private String generateRedisToken(String username){
        String token = IdUtil.simpleUUID();
        String saveLogin = ServletUtils.getRequest().getParameter(SecurityConstant.SAVE_LOGIN);
        boolean saved = StrUtil.isNotBlank(saveLogin) && Boolean.valueOf(saveLogin);

        saveRedisToken(token, new TokenUser(username, null, saved));
        return token;
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
        Map<String, Object> claims = new HashMap<String, Object>(){
            {
                put(SecurityConstant.CLAIM_KEY_USER_ID, user.getId());
            }
        };
        return claims;
    }

    /**
     * JWT Token解析
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(SecurityConstant.JWT_SIGN_KEY)
                .parseClaimsJws(token.replace(SecurityConstant.TOKEN_SPLIT, ""))
                .getBody();
    }

    public String getUserIdFromToken(String token){
        final Claims claims = getClaimsFromToken(token);
        return claims.get(SecurityConstant.CLAIM_KEY_USER_ID).toString();
    }

    public String getUsernameFromToken(String token){
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Date getExpirationDateFromToken(String token){
        final Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean isTokenRedis(){
        return this.tokenRedis;
    }

    public String validateToken(String headToken) throws LoginFailLimitException {
        if( tokenRedis ){
            String redisTokenPre = getRedisTokenPre(headToken);
            if( StrUtil.isBlank(redisTokenPre) ){
                throw new LoginFailLimitException(LoginFailEnum.LOGIN_EXPIRED_ERROR.getDesc());
            }

            // 若未保存登录状态重新设置失效时间
            TokenUser tokenUser = JSON.parseObject(redisTokenPre, TokenUser.class);
            if( !tokenUser.getSaveLogin() ){
                setRedisToken(headToken, tokenUser);
            }
            return tokenUser.getUsername();
        }else{
            final String authToken = headToken.substring(SecurityConstant.TOKEN_SPLIT.length());
            return getUsernameFromToken(authToken);
        }
    }

    private void saveRedisToken(String token, TokenUser tokenUser){
        final String username = tokenUser.getUsername();

        // 使之前登录的token失效
        String oldToken = getRedisUserToken(username);
        if( StrUtil.isNotBlank(oldToken) ){
            redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
        }
        setRedisToken(token, tokenUser);
    }

    private void setRedisToken(String token, TokenUser tokenUser){
        final String username = tokenUser.getUsername();
        final String tokenUserKey = SecurityConstant.USER_TOKEN + username;
        final String tokenPreKey =  SecurityConstant.TOKEN_PRE + token;
        String tokenUserVal = JSON.toJSONString(tokenUser);

        if( tokenUser.getSaveLogin() ){
            redisTemplate.opsForValue().set(tokenUserKey, token, saveLoginTime, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(tokenPreKey, tokenUserVal, saveLoginTime, TimeUnit.DAYS);
        }else{
            redisTemplate.opsForValue().set(tokenUserKey, token, tokenExpireTime, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(tokenPreKey, tokenUserVal, tokenExpireTime, TimeUnit.MINUTES);
        }
    }

    public String getRedisUserToken(String username){
        return redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + username);
    }

    public String getRedisTokenPre(String token){
        return redisTemplate.opsForValue().get(SecurityConstant.TOKEN_PRE + token);
    }

}
