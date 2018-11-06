package com.yibo.modules.base.dao;

import cn.yibo.base.dao.CrudDao;
import com.yibo.modules.base.entity.User;

/**
 * 描述: 用户服务DAO层
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-11-05
 * 版本: v1.0
 */
public interface UserDao extends CrudDao<User> {
    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
