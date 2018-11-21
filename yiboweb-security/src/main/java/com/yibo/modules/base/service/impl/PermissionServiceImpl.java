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

import com.yibo.modules.base.dao.PermissionDao;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.service.PermissionService;
import com.google.common.collect.Lists;
import cn.yibo.base.service.impl.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述: 权限服务类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-09-16
 * 版本: v1.0
 */
@Service
@Transactional(readOnly=true)
public class PermissionServiceImpl extends AbstractBaseService<PermissionDao, Permission> implements PermissionService {

    /**
     * 静态数据模拟数据库数据
     */
    static Permission p1,p2,p3,p4,p5,p6,p7 = null;
    static {
        p1 = new Permission("1", "","测试数据:新增数据:1", 1, "/api/datas/add");
        p2 = new Permission("2", "","测试数据:新增数据:2", 1, "/api/datas/addMap");
        p3 = new Permission("3", "","测试数据:分页查询", 1, "/api/datas/queryPage");
        p4 = new Permission("4", "","测试数据:列表数据", 1, "/api/datas/queryList");
        p5 = new Permission("5", "","测试数据:更新数据:1", 1, "/api/datas/updateMap");
        p6 = new Permission("6", "","测试数据:更新数据:2", 1, "/api/datas/updateNull");
        p7 = new Permission("7", "","测试数据:删除数据", 1, "/api/datas/deleteById");
    }

    @Override
    public List<Permission> findByUserId(String userId) {
        if( "82E3AC87DBAF4A2DBBF273D4A28687EC".equals(userId) ){
            return Lists.newArrayList(p1, p2, p3, p4, p5, p6, p7);
        }
        return Lists.newArrayList(p1, p3, p5);
        //return dao.findByUserId(userId);
    }

    @Override
    public List<Permission> findByType(Integer type) {
        return Lists.newArrayList(p1, p2, p3, p4, p5, p6, p7);
    }
}
