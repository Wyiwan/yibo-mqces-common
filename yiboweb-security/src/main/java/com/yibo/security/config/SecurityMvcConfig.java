/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：MvcConfig.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.security.config;

import com.yibo.security.annotation.SecurityUserArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *  描述: Web MVC配置
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-08
 *  版本: v1.0
 */
@Slf4j
@Component
public class SecurityMvcConfig implements WebMvcConfigurer {
    @Autowired
    private SecurityUserArgumentResolver securityUserArgumentResolver;

    /**
     * 注册参数注入类
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        log.info("add custom argument annotation");
        argumentResolvers.add(securityUserArgumentResolver);
    }
}
