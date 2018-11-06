/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：ControllerArgsResolver.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package cn.yibo.security.annotation;

import cn.yibo.security.SecurityUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *  描述: 应用于所有controller的方法的自定义参数注入处理
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-28
 *  版本: v1.0
 */
@Component
public class SecurityUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SecurityUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if( supportsParameter(methodParameter) ){
            SecurityUserDetails userDetails = (SecurityUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails;
        }
        return null;
    }
}
