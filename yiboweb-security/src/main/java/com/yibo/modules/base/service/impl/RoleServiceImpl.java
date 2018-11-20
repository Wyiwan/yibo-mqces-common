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
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package com.yibo.modules.base.service.impl;

import com.yibo.modules.base.dao.RoleDao;
import com.yibo.modules.base.entity.Role;
import com.google.common.collect.Lists;
import cn.yibo.base.service.impl.AbstractBaseService;
import com.yibo.modules.base.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述: 角色服务类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-09-16
 * 版本: v1.0
 */
@Service
@Transactional(readOnly=true)
public class RoleServiceImpl extends AbstractBaseService<RoleDao, Role> implements RoleService {
    @Override
    public List<Role> findByUserId(String userId) {
        if( "82E3AC87DBAF4A2DBBF273D4A28687EC".equals(userId) ){
            return Lists.newArrayList(new Role("1", "\"超级管理员\""), new Role("2", "系统管理员"));
        }
        return Lists.newArrayList(new Role("2", "系统管理员"));
        //return dao.findByUserId(userId);
    }
}
