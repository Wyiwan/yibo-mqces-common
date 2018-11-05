/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：UserDetailsServiceImpl.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.security;

import com.yibo.common.lang.StringUtils;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.UserService;
import com.yibo.security.exception.LoginFailEnum;
import com.yibo.security.exception.LoginFailLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *  描述: 一句话描述该类的用途
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
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
        if( StringUtils.isNotBlank(value) ){
            throw new LoginFailLimitException("登录错误次数超过限制，请"+timeRest+"分钟后再试");
        }

        User user = userService.findByUsername(username);
        if( user == null ){
            throw new UsernameNotFoundException(LoginFailEnum.INCORRECT_ERROR.getDesc());
        }
        return new SecurityUserDetails(user);
    }
}
