/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：UserService.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.sys.service;

import com.google.common.collect.Lists;
import com.yibo.modules.sys.entity.Permission;
import com.yibo.modules.sys.entity.Role;
import com.yibo.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述: 实现层
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@Service
@Transactional(readOnly=true)
public class UserService {
    // 1.注入UserDao
    //   User user = userDao.queryOne(Map<String, Object> condition)
    //   condition.put('status', CommonConstant.STATUS_NORMAL);
    //   condition.put('username', username);
    //2.注入DeptDao
    //  Dept dept = deptDao.fetch(user.getDeptId);
    //  user.setDept(dept);
    //3.注入RoleDao
    //  List<Role> roleList = roleDao.findByUserId(user.getId());
    //  user.setRoles(roleList);
    //4.注入PermissionDao
    //  List<Permission> permList = permissionDao.findByUserId(user.getId());
    //  user.permissions(permList);

    @Autowired
    PermissionService permissionService;

    /**
     * 获取用户信息（模拟获取数据库数据）
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        User user = null;
        if( "admin".equals(username) ){
            user = new User("82E3AC87DBAF4A2DBBF273D4A28687EC", "admin", "$2a$10$eQCxTf3HbIq/sya3Shb/leiZHKyJSXxNQP2B83xskPrCnnXQIK2IO",
                    "7E1D4D41240841A8ACAC3A464413DBC2", 1);

            List<Role> roleList = Lists.newArrayList(new Role("1", "\"超级管理员\""), new Role("2", "系统管理员"));
            user.setRoles(roleList);

            List<Permission> permList = permissionService.getAdminPermission();
            user.setPermissions(permList);
        }else if( "test".equals(username) ){
            user = new User("673E2F20A48D486588E4BF0BC594D53C", "test", "$2a$10$w.FRvORbDulnCb15DqtYb.YePwBlgTGMATLuVqTt1YN8oxJ2JgToy",
                    "DF4D8164E30140EE991A97D89CC22A12", 1);

            List<Role> roleList = Lists.newArrayList(new Role("2", "系统管理员"));
            user.setRoles(roleList);

            List<Permission> permList = permissionService.getTestPermission();
            user.setPermissions(permList);
        }
        return user;
    }

}
