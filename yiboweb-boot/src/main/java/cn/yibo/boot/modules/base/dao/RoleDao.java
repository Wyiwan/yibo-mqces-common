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
{  2018-12-20  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.dao;

import cn.yibo.boot.modules.base.entity.Role;
import cn.yibo.boot.modules.base.entity.User;
import cn.yibo.common.base.dao.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色表数据访问层
 * @author 高云
 * @since 2018-12-20
 * @version v1.0
 */
public interface RoleDao extends CrudDao<Role> {
    /**
     * 分页查询扩展
     * @param condition
     * @return
     */
    List<User> queryPageExt(@Param("condition") Map<String, Object> condition);

    /**
     * 根据用户ID查询角色
     * @param userId
     * @return
     */
    List<Role> findByUserId(String userId);

    /**
     * 菜单授权
     * @param role
     */
    void grantPermission(Role role);

    /**
     * 用户授权
     * @param role
     */
    void grantUser(Role role);

    /**
     * 取消用户授权
     * @param role
     */
    void unGrantUser(Role role);
}