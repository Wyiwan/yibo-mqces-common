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

package cn.yibo.boot.modules.base.dao;

import cn.yibo.boot.base.dao.CrudDao;
import cn.yibo.boot.modules.base.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限表数据访问层
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
public interface PermissionDao extends CrudDao<Permission>{
    /**
     * 根据用户ID查询权限
     * @param userId
     * @return
     */
    List<Permission> findByUserId(@Param("userId") String userId, @Param("type") String type);

    /**
     * 根据角色ID查询权限
     * @param roleId
     * @return
     */
    List<Permission> findByRoleId(@Param("roleId") String roleId);

    /**
     * 根据权重查询权限
     * @param minWeight
     * @param maxWeight
     * @param type
     * @return
     */
    List<Permission> findByWeight(@Param("minWeight") Integer minWeight, @Param("maxWeight") Integer maxWeight, @Param("type") String type);

    /**
     * 级联删除
     * @param list
     * @return
     */
    int deleteCascade(List list);

    /**
     * 更新Ancestor字段信息
     * @param permission
     */
    void updateAncestor(Permission permission);

}