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

package com.yibo.modules.base.service;

import cn.yibo.base.service.IBaseService;
import com.yibo.modules.base.dao.PermissionDao;
import com.yibo.modules.base.entity.Permission;

import java.util.List;

/**
 * 描述: 权限接口层
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-09-16
 * 版本: v1.0
 */
public interface PermissionService extends IBaseService<PermissionDao, Permission> {
    /**
     * 通过用户ID查询用户权限
     * @param userId
     * @return
     */
    List<Permission> findByUserId(String userId);

    /**
     * 通过权限类型查找权限
     * @param type
     * @return
     */
    List<Permission> findByType(Integer type);
}
