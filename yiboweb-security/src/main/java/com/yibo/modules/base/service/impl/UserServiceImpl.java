/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：安全控制模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package com.yibo.modules.base.service.impl;

import cn.yibo.base.service.impl.AbstractBaseService;
import com.yibo.modules.base.dao.UserDao;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.PermissionService;
import com.yibo.modules.base.service.RoleService;
import com.yibo.modules.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述: 用户服务类
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends AbstractBaseService<UserDao, User> implements UserService {
    @Autowired
    PermissionService PermissionService;

    @Autowired
    RoleService roleService;

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        // User user = dao.findByUsername(username);
        User user = null;
        if( "admin".equals(username) ){
            user = new User("82E3AC87DBAF4A2DBBF273D4A28687EC", "admin", "$2a$10$eQCxTf3HbIq/sya3Shb/leiZHKyJSXxNQP2B83xskPrCnnXQIK2IO",
                    "7E1D4D41240841A8ACAC3A464413DBC2", 1);
        }else if( "test".equals(username) ){
            user = new User("673E2F20A48D486588E4BF0BC594D53C", "test", "$2a$10$w.FRvORbDulnCb15DqtYb.YePwBlgTGMATLuVqTt1YN8oxJ2JgToy",
                    "DF4D8164E30140EE991A97D89CC22A12", 1);
        }

        List<Permission> permList = PermissionService.findByUserId(user.getId());
        List<Role> roleList = roleService.findByUserId(user.getId());
        user.setRoles(roleList);
        user.setPermissions(permList);
        return user;
    }

}
