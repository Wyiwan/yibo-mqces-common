/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：WebSecurityConfig.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.security;

import com.yibo.security.config.IgnoredUrlConfig;
import com.yibo.security.jwt.*;
import com.yibo.security.permission.MyFilterSecurityInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 *  描述: Security 核心配置类
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-20
 *  版本: v1.0
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IgnoredUrlConfig ignoredUrlsProperties;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationFailHandler failHandler;

    @Autowired
    private RestAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    JWTUtil jwtUtil;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                // 允许网页iframe
                .headers().frameOptions().disable()
            .and()
                // 表单登录方式
                .formLogin()
                .loginPage("/auth/unauth")
                .loginProcessingUrl("/auth/login").permitAll()
                .successHandler(new AuthenticationSuccessHandler(jwtUtil))
                .failureHandler(failHandler)
            .and()
                .logout().permitAll()
            .and()
                // 除配置文件忽略路径其它所有请求都需经过认证和授权
                .authorizeRequests()
                .antMatchers(ignoredUrlsProperties.getUrls()).permitAll()
                .anyRequest().authenticated()
            .and()
                // 自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
            .and()
                // 自定义权限拦截器
                .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                // JWT过滤器
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil, userDetailsService));
    }
}
