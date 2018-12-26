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

import cn.yibo.base.controller.BaseForm;
import cn.yibo.base.service.IBaseService;
import com.github.pagehelper.PageInfo;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.dao.UserDao;
import com.yibo.modules.base.entity.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

/**
 * 用户表实体服务接口层类(User)
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@CacheConfig(cacheNames = CommonConstant.USER_CACHE)
public interface UserService extends IBaseService<UserDao, User>{
    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    @Cacheable(key = "#username", unless = "#result == null")
    User findByUsername(String username);

    /**
     * 根据角色查询用户
     * @param baseForm
     * @return
     */
    PageInfo<T> queryPageByRole(BaseForm<T> baseForm);

    /**
     * 角色授权
     * @param user
     */
    void grantRole(User user);
}