/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：UserService.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.base.service;

import cn.yibo.base.service.IBaseService;
import com.yibo.modules.base.dao.RoleDao;
import com.yibo.modules.base.entity.Role;

import java.util.List;

/**
 * 描述: 用户接口层
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public interface RoleService extends IBaseService<RoleDao, Role> {
    /**
     * 通过用户ID查询用户角色
     * @param userId
     * @return
     */
    List<Role> findByUserId(String userId);

}
