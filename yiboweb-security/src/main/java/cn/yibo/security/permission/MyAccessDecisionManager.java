/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：MyAccessDecisionManager.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package cn.yibo.security.permission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 *  描述: 权限管理决断器
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-26
 *  版本: v1.0
 */
@Slf4j
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if( configAttributes == null ){
            return;
        }

        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while( iterator.hasNext() ){
            // 获取请求地址的操作权限标识
            String attribute = iterator.next().getAttribute();

            // 用户所拥有的权限
            for( GrantedAuthority authority : authentication.getAuthorities() ){
                if( attribute.trim().equals(authority.getAuthority()) ){
                    return;
                }
            }
        }
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}