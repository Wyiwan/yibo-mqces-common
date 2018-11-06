/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：PermissionService.java
 * @author:：gogo163gao@163.com
 * @version：1.0
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
