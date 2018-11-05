/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：PermissionService.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.base.service.impl;

import com.google.common.collect.Lists;
import com.yibo.base.service.impl.AbstractBaseService;
import com.yibo.modules.base.dao.PermissionDao;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.service.PermissionService;
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
        p1 = new Permission("1", "","测试数据:新增数据:1", 1, "/test/add");
        p2 = new Permission("2", "","测试数据:新增数据:2", 1, "/test/addMap");
        p3 = new Permission("3", "","测试数据:分页查询", 1, "/test/queryPage");
        p4 = new Permission("4", "","测试数据:列表数据", 1, "/test/queryList");
        p5 = new Permission("5", "","测试数据:更新数据:1", 1, "/test/updateMap");
        p6 = new Permission("6", "","测试数据:更新数据:2", 1, "/test/updateNull");
        p7 = new Permission("7", "","测试数据:删除数据", 1, "/test/deleteById");
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
