package com.yibo.modules.base.dao;

import cn.yibo.base.dao.CrudDao;
import com.yibo.modules.base.entity.Permission;

import java.util.List;

/**
 * 描述: 权限服务DAO层
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-11-05
 * 版本: v1.0
 */
public interface PermissionDao extends CrudDao<Permission> {
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
