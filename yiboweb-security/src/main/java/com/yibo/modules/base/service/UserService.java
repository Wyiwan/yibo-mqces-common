/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：UserService.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.base.service;

import cn.yibo.base.service.IBaseService;
import com.yibo.modules.base.dao.UserDao;
import com.yibo.modules.base.entity.User;

/**
 * 描述: 用户接口层
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public interface UserService extends IBaseService<UserDao, User> {
    /**
     * 通过用户名查询用户信息(包含用户所在部门、所拥有的权限)
     * @param username
     * @return
     */
    User findByUsername(String username);

}
