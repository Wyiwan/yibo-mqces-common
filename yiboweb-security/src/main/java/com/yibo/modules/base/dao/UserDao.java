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

package com.yibo.modules.base.dao;

import cn.yibo.base.dao.CrudDao;
import com.yibo.modules.base.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表实体数据访问层类(User)
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
public interface UserDao extends CrudDao<User>{
    /**
     * 分页查询扩展
     * @param condition
     * @return
     */
    List<User> queryPageExt(@Param("condition") Map<String, Object> condition);

    /**
     * 根据角色查询用户
     * @param condition
     * @return
     */
    List<User> queryPageByRole(@Param("condition") Map<String, Object> condition);
}