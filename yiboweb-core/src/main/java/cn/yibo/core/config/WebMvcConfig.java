/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：核心模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-10  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的core包
{*****************************************************************************
*/

package cn.yibo.core.config;

import cn.yibo.core.web.listener.WebServletListener;
import cn.yibo.core.web.restful.ExtFastJsonHttpMessageConverter;
import cn.yibo.core.web.wrapper.HttpServletRequestReplacedFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

    // 注册复制流过滤器
    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpServletRequestReplacedFilter");
        registration.setOrder(1);
        return registration;
    }
}
