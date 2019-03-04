/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：权限管理模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2019-02-20  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的系统模块
{*****************************************************************************
*/
package cn.yibo.boot.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.entity.TreeBuild;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.boot.modules.base.entity.User;
import cn.yibo.boot.modules.base.service.PermissionService;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.common.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 权限工具类
 * @author 高云
 * @since 2019-02-20
 * @version v1.0
 */
@Component
public class PermUtils {
    private static String configMinWeight = ObjectUtils.toString( PropertiesUtils.getInstance().getProperty("webapp.super-admin-get-perms-min-weight") );
    public static final Integer SUPER_GET_PERMS_MIN_WEIGHT = StrUtil.isEmpty(configMinWeight) ? CommonConstant.ADMIN_PERMS_WEIGHT : ObjectUtils.toInteger(configMinWeight);

    @Autowired
    private PermissionService permissionService;

    @Value("${webapp.allow-multi-identity}")
    private Boolean allowMultiIdentity;

    /**
     * 获取用户所有权限
     * @param user
     * @return
     */
    public List<Permission> getUserAllPermissions(User user){
        List<Permission> permissions = ListUtils.newArrayList();
        user = user == null ? UserContext.getUser() : user;

        if( user != null ){
            if( user.isSuperAdmin() ){
                permissions = permissionService.findByWeight(SUPER_GET_PERMS_MIN_WEIGHT, null, null);
            }else if( user.isAdmin() ){
                permissions = permissionService.findByWeight(CommonConstant.ADMIN_PERMS_WEIGHT, CommonConstant.SUPER_ADMIN_PERMS_WEIGHT, null);
            }else{
                if( !allowMultiIdentity && user.getRole() != null ){
                    permissions = permissionService.findByRoleId(user.getRole().getId());
                }else{
                    permissions = permissionService.findByUserId(user.getId());
                }
            }
        }
        return permissions;
    }

    /**
     * 获取用户可授权的权限
     * @return
     */
    public List<Permission> getAuthorizablePermissions(){
        List<Permission> permissions = ListUtils.newArrayList();

        User user = UserContext.getUser();
        if( user != null ){
            if( user.isSuperAdmin() ){
                permissions = permissionService.findByWeight(CommonConstant.USER_PERMS_WEIGHT, CommonConstant.SUPER_ADMIN_PERMS_WEIGHT, null);
            }else if( user.isAdmin() ){
                permissions = permissionService.findByWeight(CommonConstant.USER_PERMS_WEIGHT, CommonConstant.ADMIN_PERMS_WEIGHT, null);
            }else{
                permissions = permissionService.findByUserId(user.getId());
            }
        }
        return permissions;
    }

    /***
     * 根据权限类型从已有权限中筛选权限
     * @param permissions
     * @param type
     * @return
     */
    public static List<Permission> filterPermissionsByType(List<Permission> permissions, String type){
        List<Permission> list = ListUtils.newArrayList();
        if( !ListUtils.isEmpty(permissions) ){
            for( Permission p : permissions ){
                if( StrUtil.equals(p.getPermsType(),type) ){
                    list.add(p);
                }
            }
        }
        return list;
    }

    /**
     * 获取页面按钮权限数据(前端权限控制所需)
     * @param permissions
     * @return
     */
    public static Map<String, List<String>> getButtonPermissions(List<Permission> permissions){
        Map<String, List<String>> map = MapUtil.newHashMap();

        List<Permission> opearPermissions = PermUtils.filterPermissionsByType(permissions, CommonConstant.PERMISSION_OPERATION);
        if( !ListUtils.isEmpty(opearPermissions) ) {
            TreeBuild treeBuild = new TreeBuild(permissions);

            for( Permission permission : opearPermissions ){
                Permission parentNode = (Permission) treeBuild.getParent(permission);

                if( parentNode != null && CommonConstant.PERMISSION_PAGE.equals(parentNode.getPermsType()) ){
                    String pageUrl = parentNode.getPermsUrl();
                    String btnType = permission.getButtonType();

                    if( !ListUtils.isEmpty(map.get(pageUrl)) ){
                        map.get(pageUrl).add(btnType);
                    }else{
                        map.put(pageUrl, ListUtils.newArrayList(btnType));
                    }
                }
            }
        }
        return map;
    }

    /**
     * 根据菜单地址获取菜单路径
     * @param menuUrl
     * @return
     */
    public String getMenuNamePath(String menuUrl){
        List<Permission> menuList = permissionService.findAll();

        if( menuList != null ){
            Permission currNode = null;
            for( Permission node : menuList ){
                if( StrUtil.equals(node.getPermsUrl(), menuUrl) ){
                    currNode = node;
                    break;
                }
            }

            if( currNode != null ){
                List parentList = new TreeBuild(menuList).getParentsNode(currNode, false);
                List<String> permsNameList = ListUtils.extractToList(parentList, "permsName");
                return CollectionUtil.join(CollectionUtil.reverse(permsNameList), StrUtil.SLASH);
            }
            return "false";
        }
        return "false";
    }


}
