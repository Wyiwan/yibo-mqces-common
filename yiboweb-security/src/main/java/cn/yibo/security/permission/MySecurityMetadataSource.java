/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：MySecurityMetadataSource.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package cn.yibo.security.permission;

import cn.yibo.common.lang.StringUtils;
import com.google.common.collect.Lists;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.service.PermissionService;
import cn.yibo.security.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

/**
 *  描述: 权限资源管理器
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-19
 *  版本: v1.0
 */
@Slf4j
@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private PermissionService PermissionService;

    private Map<String, Collection<ConfigAttribute>> map = null;

    /**
     * 加载权限表中所有操作请求权限
     */
    public void loadResourceDefine(){
        map = new HashMap<>(16);
        Collection<ConfigAttribute> configAttributes;
        ConfigAttribute configAttribute;

        // 获取启用的操作权限
        List<Permission> permissions = PermissionService.findByType(CommonConstant.PERMISSION_OPERATION);
        for( Permission permission : permissions ){
            if( StringUtils.isNotBlank(permission.getTitle()) && StringUtils.isNotBlank(permission.getPath()) ){
                configAttributes = Lists.newArrayList();
                configAttribute = new SecurityConfig(permission.getTitle());

                configAttributes.add(configAttribute);
                map.put(permission.getPath(), configAttributes);
            }
        }
    }

    /**
     * 判定用户请求的url是否在权限表中
     * 如果在权限表中，则返回给decide方法，用来判定用户是否有此权限
     * 如果不在权限表中则放行
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if( map == null ){
            loadResourceDefine();
        }

        //String requestUrl = ((FilterInvocation)object).getRequestUrl();
        String requestUrl = ((FilterInvocation)object).getRequest().getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        Iterator<String> iterator = map.keySet().iterator();
        while( iterator.hasNext() ){
            String permUrl = iterator.next();
            if( StringUtils.isNotBlank(permUrl) && pathMatcher.match(permUrl, requestUrl) ){
                return map.get(permUrl);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes(){
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass){
        return true;
    }

}
