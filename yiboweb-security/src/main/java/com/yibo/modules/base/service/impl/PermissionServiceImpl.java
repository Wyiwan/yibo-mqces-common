/*
{*****************************************************************************
{  广州医博-基础框架 v1.0													
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.					
{  创建人：  高云
{  审查人：
{  模块：系统管理模块
{  功能描述:										
{		 													
{  ---------------------------------------------------------------------------	
{  维护历史:													
{  日期        维护人        维护类型						
{  ---------------------------------------------------------------------------	
{  2018-12-03  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.base.service.impl.AbstractBaseService;
import cn.yibo.common.collect.ListUtils;
import cn.yibo.common.io.PropertiesUtils;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.security.context.UserContext;
import com.google.common.collect.Lists;
import com.yibo.modules.base.config.constant.CommonConstant;
import com.yibo.modules.base.dao.PermissionDao;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.PermissionTree;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单权限表实体服务实现层类(Permission)
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class PermissionServiceImpl extends AbstractBaseService<PermissionDao, Permission> implements PermissionService {
    private static final String MENU_CACHE = "allMenus";
    private static String configMinWeight = ObjectUtils.toString( PropertiesUtils.getInstance().getProperty("webapp.super-admin-get-perms-min-weight") );
    public static final Integer SUPER_GET_PERMS_MIN_WEIGHT = StrUtil.isEmpty(configMinWeight) ? CommonConstant.ADMIN_PERMS_WEIGHT : ObjectUtils.toInteger(configMinWeight);

    /**
     * 重写新增
     * @param permission
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insert(Permission permission){
        super.insert(permission);
        dao.updateAncestor(permission);
        CacheUtils.remove(MENU_CACHE);
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        dao.deleteCascade(list);
        CacheUtils.remove(MENU_CACHE);
        CacheUtils.removeAll(CommonConstant.USER_CACHE);
    }

    /**
     * 重写更新
     * @param permission
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void updateNull(Permission permission){
        super.updateNull(permission);
        dao.updateAncestor(permission);
        CacheUtils.remove(MENU_CACHE);
        CacheUtils.removeAll(CommonConstant.USER_CACHE);
    }

    /**
     * 查询树结构数据
     * @return
     */
    @Override
    public List<Permission> findTree(){
        List<Permission> permissions = (List<Permission>)CacheUtils.get(MENU_CACHE);
        if( permissions == null ){
            permissions = dao.findTree();
            CacheUtils.put(MENU_CACHE, permissions);
        }
        return permissions;
    }

    /**
     * 根据类型查询权限
     * @param type
     * @return
     */
    @Override
    public List<Permission> findByType(String type){
        Map<String, Object> condition = MapUtil.newHashMap();
        condition.put("status", CommonConstant.STATUS_NORMAL);
        condition.put("permsType", type);

        return dao.queryList(condition, "perms_sort", null);
    }

    /**
     * 根据用户ID查询权限
     * @param userId
     * @return
     */
    @Override
    public List<Permission> findByUserId(String userId, String type){
        return dao.findByUserId(userId, type);
    }

    /**
     * 根据角色ID查询权限
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> findByRoleId(String roleId) {
        return dao.findByRoleId(roleId);
    }

    /**
     * 根据权重查询权限
     * @param min
     * @param max
     * @param type
     * @return
     */
    @Override
    public List<Permission> findByWeight(Integer min, Integer max, String type) {
        return dao.findByWeight(min, max, type);
    }

    /**
     * 获取访问的权限
     * @param user
     * @return
     */
    @Override
    public List<Permission> getAccessPermission(User user) {
        List<Permission> permissions = CollUtil.newArrayList();
        user = user == null ? UserContext.getUser() : user;

        if( user != null ){
            if( user.isSuperAdmin() ){
                permissions = this.findByWeight(SUPER_GET_PERMS_MIN_WEIGHT, null, null);
            }else if( user.isAdmin() ){
                permissions = this.findByWeight(CommonConstant.ADMIN_PERMS_WEIGHT, CommonConstant.SUPER_ADMIN_PERMS_WEIGHT, null);
            }else{
                permissions = this.findByUserId(user.getId(), null);
            }
        }
        return permissions;
    }

    /**
     * 获取可授权的权限
     * @return
     */
    @Override
    public List<Permission> getGrantPermission() {
        User user = UserContext.getUser();
        List<Permission> permissions = CollUtil.newArrayList();

        if( user != null ){
            if( user.isSuperAdmin() ){
                permissions = this.findByWeight(CommonConstant.USER_PERMS_WEIGHT, CommonConstant.SUPER_ADMIN_PERMS_WEIGHT, null);
            }else if( user.isAdmin() ){
                permissions = this.findByWeight(CommonConstant.USER_PERMS_WEIGHT, CommonConstant.ADMIN_PERMS_WEIGHT, null);
            }else{
                permissions = this.findByUserId(user.getId(), null);
            }
        }
        return permissions;
    }

    /**
     * 根据菜单地址获取菜单路径
     * @param menuUrl
     * @return
     */
    @Override
    public String getMenuNamePath(String menuUrl){
        List<Permission> menus = this.findTree();

        if( !CollUtil.isEmpty(menus) && !StrUtil.isEmpty(menuUrl) ){
            List<String> condition = Lists.newArrayList(menuUrl);
            List<Permission> resultList = menus.stream().filter((Permission p) -> condition.contains(p.getPermsUrl())).collect(Collectors.toList());

            if( !CollUtil.isEmpty(resultList) ){
                Permission node = resultList.get(0);
                List<Permission> parentList = new PermissionTree(menus).getParentsNode(node, CommonConstant.PERMISSION_PAGE.equals(node.getPermsType()));
                List<String> parentNameList = ListUtils.extractToList(parentList, "permsName");

                if( !CollUtil.isEmpty(parentNameList) ){
                    return CollUtil.join(CollUtil.reverse(parentNameList), StrUtil.SLASH);
                }
            }else{
                return "false";
            }
        }
        return StrUtil.EMPTY;
    }

}