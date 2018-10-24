/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：PermissionService.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.sys.service;

import com.google.common.collect.Lists;
import com.yibo.modules.sys.entity.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述: 权限服务类（模拟获取数据库数据）
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-09-16
 * 版本: v1.0
 */
@Service
@Transactional(readOnly=true)
public class PermissionService {
    Permission p1 = new Permission("1", "","测试数据:新增数据:1", 1, "/test/add");
    Permission p2 = new Permission("2", "","测试数据:新增数据:2", 1, "/test/addMap");
    Permission p3 = new Permission("3", "","测试数据:分页查询", 1, "/test/queryPage");
    Permission p4 = new Permission("4", "","测试数据:列表数据", 1, "/test/queryList");
    Permission p5 = new Permission("5", "","测试数据:更新数据:1", 1, "/test/updateMap");
    Permission p6 = new Permission("6", "","测试数据:更新数据:2", 1, "/test/updateNull");
    Permission p7 = new Permission("7", "","测试数据:删除数据", 1, "/test/deleteById");

    /**
     * 根据类型获取权限信息
     * @param type
     * @return
     */
    public List<Permission> findByType(Integer type){
        return Lists.newArrayList(p1, p2, p3, p4, p5, p6, p7);
    }

    public List<Permission> getAdminPermission(){
        return findByType(1);
    }

    public List<Permission> getTestPermission(){
        return Lists.newArrayList(p1, p3, p5);
    }
}
