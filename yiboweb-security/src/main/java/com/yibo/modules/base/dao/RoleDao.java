package com.yibo.modules.base.dao;

import cn.yibo.base.dao.CrudDao;
import com.yibo.modules.base.entity.Role;

import java.util.List;

/**
 * 描述: 角色服务DAO层
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-11-05
 * 版本: v1.0
 */
public interface RoleDao extends CrudDao<Role> {
    /**
     * 通过用户ID查询用户角色
     * @param userId
     * @return
     */
    List<Role> findByUserId(String userId);
}
