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
{  注：本模块代码为底层基础框架封装的boot包
{*****************************************************************************
*/

package cn.yibo.boot.config.security.permission;

import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.boot.modules.base.service.PermissionService;
import cn.yibo.common.utils.ListUtils;
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
    private PermissionService permissionService;

    private Map<String, Collection<ConfigAttribute>> map = null;

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

        if( map != null ){
            Iterator<String> iterator = map.keySet().iterator();
            while( iterator.hasNext() ){
                String permUrl = iterator.next();
                if( StrUtil.isNotBlank(permUrl) && pathMatcher.match(permUrl, requestUrl) ){
                    return map.get(permUrl);
                }
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

    /**
     * 加载所有的操作权限
     */
    public void loadResourceDefine(){
        map = new HashMap<>(16);
        Collection<ConfigAttribute> configAttributes;

        // 获取所有启用的操作权限
        List<Permission> permissionList = permissionService.findByType(CommonConstant.PERMISSION_OPERATION);
        if( !ListUtils.isEmpty(permissionList) ){
            for( Permission permission : permissionList ){
                if( StrUtil.isNotBlank(permission.getPermsName()) && StrUtil.isNotBlank(permission.getPermsUrl()) ){
                    configAttributes = ListUtils.newArrayList(new SecurityConfig(permission.getPermsName().trim()));
                    map.put(permission.getPermsUrl().trim(), configAttributes);
                }
            }
        }
    }
}
