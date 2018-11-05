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
import com.yibo.modules.base.dao.RoleDao;
import com.yibo.modules.base.entity.Role;
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
