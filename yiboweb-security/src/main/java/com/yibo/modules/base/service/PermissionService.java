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

package com.yibo.modules.base.service;

import cn.yibo.base.service.IBaseService;
import com.yibo.modules.base.dao.PermissionDao;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.User;

import java.util.List;

/**
 * 菜单权限表服务接口层
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
public interface PermissionService extends IBaseService<PermissionDao, Permission>{
    /**
     * 根据类型查询权限
     * @param type
     * @return
     */
    List<Permission> findByType(String type);

    /**
     * 根据用户ID查询权限
     * @param userId
     * @return
     */
    List<Permission> findByUserId(String userId, String type);

    /**
     * 根据角色ID查询权限
     * @param roleId
     * @return
     */
    List<Permission> findByRoleId(String roleId);

    /**
     * 根据权重查询权限
     * @param min
     * @param max
     * @param type
     * @return
     */
    List<Permission> findByWeight(Integer min, Integer max, String type);

    /**
     * 获取访问的权限
     * @param user
     * @return
     */
    List<Permission> findAccessPermission(User user);

    /**
     * 获取可授权的权限
     * @return
     */
    List<Permission> findGrantPermission();

    /**
     * 根据菜单地址获取菜单路径
     * @param menuUrl
     * @return
     */
    String getMenuNamePath(String menuUrl);
}