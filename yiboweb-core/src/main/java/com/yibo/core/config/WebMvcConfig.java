/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：WebMvcConfig.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.core.config;

import com.yibo.core.listener.WebServletListener;
import com.yibo.core.web.restful.ExtFastJsonHttpMessageConverter;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *  描述: Web MVC 配置
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-08
 *  版本: v1.0
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 注册消息转换器
     * @param converters
     */
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(messageConverter());
    }

    @Bean
    public ExtFastJsonHttpMessageConverter messageConverter(){
        ExtFastJsonHttpMessageConverter extFastJsonHttpMessageConverter = new ExtFastJsonHttpMessageConverter();
        return extFastJsonHttpMessageConverter;
    }

    /**
     *  跨域支持
     * @param registry
     */
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    //------------------------------------------------------------------------------------------------------------------
    //--------- Servlet Config  ----------
    //------------------------------------------------------------------------------------------------------------------
    // 注册 RequestContextListener ...
    @Bean
    public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerRegistration() {
        return new ServletListenerRegistrationBean<>(new RequestContextListener());
    }

    // 注册 ServletListener
    @Bean
    public ServletListenerRegistrationBean configListener(){
        ServletListenerRegistrationBean<WebServletListener> registrationBean = new ServletListenerRegistrationBean<>(new WebServletListener());
        return registrationBean;
    }

}
