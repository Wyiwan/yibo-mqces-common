/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：系统管理模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-12-03  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************
*/

package com.yibo.modules.base.service.impl;

import cn.yibo.base.controller.BaseForm;
import cn.yibo.base.service.impl.AbstractBaseService;
import cn.yibo.common.collect.ListUtils;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.security.context.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.dao.UserDao;
import com.yibo.modules.base.entity.Dept;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.DeptService;
import com.yibo.modules.base.service.PermissionService;
import com.yibo.modules.base.service.RoleService;
import com.yibo.modules.base.service.UserService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户表实体服务实现层类(User)
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends AbstractBaseService<UserDao, User> implements UserService {
    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permsService;

    /**
     * 重写新增
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insert(User user){
        super.insert(user);
        if( !ListUtils.isEmpty(user.getRoleIdList()) ){
            this.grantRole(user);
        }
    }

    /**
     * 重写更新
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    @CacheEvict(key = "#user.username")
    public void update(User user){
        super.update(user);
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        super.deleteByIds(list);
        this.clearUsersCacheByUserId(list);
    }

    /**
     * 角色授权
     * @param user
     */
    @Override
    @Transactional(readOnly = false)
    public void grantRole(User user){
        dao.grantRole(user);
        this.clearUsersCacheByUserId( Lists.newArrayList(user.getId()) );
    }

    /**
     * 重写分页查询
     * @param baseForm
     * @param <T>
     * @return
     */
    @Override
    public <T>PageInfo<T> queryPage(BaseForm<T> baseForm){
        // 设置分页参数
        if( !ObjectUtils.isEmpty( baseForm.get("page") ) ){
            PageHelper.startPage(baseForm.getPageNo(), baseForm.getPageSize());
        }

        // 获取查询参数
        Map<String, Object> params = baseForm.getParameters();

        // 管理员类型
        String mgrType = ObjectUtils.toString(params.get("mgrType"));
        if( ObjectUtils.isEmpty(mgrType) ){
            params.put("mgrType", CommonConstant.USER_MGR_TYPE_NORMAL);
        }

        // 普通用户查询关联租户
        if( !CommonConstant.USER_MGR_TYPE_ADMIN.equals(mgrType) ){
            params.put("tenantId", UserContext.getUser().getTenantId());
        }
        logger.info("分页请求参数："+params);

        List list = dao.queryPageExt(params);
        return new PageInfo<T>(list);
    }

    /**
     * 根据角色查询用户
     * @param baseForm
     * @return
     */
    @Override
    public PageInfo<T> queryPageByRole(BaseForm<T> baseForm){
        // 设置分页参数
        if( !ObjectUtils.isEmpty( baseForm.get("page") ) ){
            PageHelper.startPage(baseForm.getPageNo(), baseForm.getPageSize());
        }

        // 获取查询参数
        Map<String, Object> params = baseForm.getParameters();
        params.put("tenantId", UserContext.getUser().getTenantId());
        logger.info("分页请求参数："+params);

        List list = dao.queryPageByRole(params);
        return new PageInfo<T>(list);
    }

    /**
     * 通过用户名查询
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username){
        User user = dao.findOne("username", username);

        if( user != null ){
            // 关联科室
            Dept dept = deptService.fetch(user.getDeptId());
            if( dept != null ){
                user.setDept(dept);
            }

            // 关联角色
            List<Role> roles = roleService.findByUserId(user.getId());
            if( !ListUtils.isEmpty(roles) ){
                user.setRoles(roles);
            }

            // 关联权限
            List<Permission> permissions = permsService.getAccessPermission(user);
            if( !ListUtils.isEmpty(permissions) ){
                user.setPermissions(permissions);
            }
        }
        return user;
    }
}