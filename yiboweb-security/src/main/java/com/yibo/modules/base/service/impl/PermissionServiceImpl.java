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

import cn.yibo.base.service.impl.AbstractBaseService;
import cn.yibo.common.io.PropertiesUtils;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.security.context.UserContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.dao.PermissionDao;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 菜单权限表实体服务实现层类(Permission)
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class PermissionServiceImpl extends AbstractBaseService<PermissionDao, Permission> implements PermissionService {
    private static Object configMinWeight = PropertiesUtils.getInstance().getProperty("webapp.super-admin-get-perms-min-weight");
    public static final Integer SUPER_GET_PERMS_MIN_WEIGHT = ObjectUtils.isEmpty(configMinWeight) ? CommonConstant.ADMIN_PERMS_WEIGHT : ObjectUtils.toInteger(configMinWeight);

    /**
     * 重写新增
     * @param permission
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int insert(Permission permission){
        int result = super.insert(permission);
        dao.updateAncestor(permission);
        return result;
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteByIds(List list){
        int result = dao.deleteCascade(list);

        // 清除用户缓存
        CacheUtils.removeAll(CommonConstant.USER_CACHE);
        return result;
    }

    /**
     * 重写更新
     * @param permission
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int update(Permission permission){
        int result = super.update(permission);
        dao.updateAncestor(permission);

        // 清除用户缓存
        CacheUtils.removeAll(CommonConstant.USER_CACHE);
        return result;
    }

    /**
     * 查询树结构数据
     * @return
     */
    @Override
    public List<Permission> findTree() {
        return dao.findTree();
    }

    /**
     * 根据类型查询权限
     * @param type
     * @return
     */
    @Override
    public List<Permission> findByType(String type){
        Map<String, Object> condition = Maps.newHashMap();
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
     * 根据用户获取访问的权限
     * @param user
     * @return
     */
    @Override
    public List<Permission> getAccessPermission(User user) {
        List<Permission> permissions = Lists.newArrayList();
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
     * 根据用户获取可授权的权限
     * @return
     */
    @Override
    public List<Permission> getGrantPermission() {
        User user = UserContext.getUser();
        List<Permission> permissions = Lists.newArrayList();

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

}