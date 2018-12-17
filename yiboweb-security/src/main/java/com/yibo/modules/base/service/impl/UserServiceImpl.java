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
import cn.yibo.common.io.PropertiesUtils;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.constant.CommonConstant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yibo.modules.base.dao.UserDao;
import com.yibo.modules.base.entity.Dept;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.DeptService;
import com.yibo.modules.base.service.PermissionService;
import com.yibo.modules.base.service.RoleService;
import com.yibo.modules.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static Object superAdminMinWeight = PropertiesUtils.getInstance().getProperty("webapp.super-admin-get-perms-min-weight");
    public static final Integer SUPER_WEIGHT = ObjectUtils.isEmpty(superAdminMinWeight) ? CommonConstant.ADMIN_PERMS_WEIGHT : ObjectUtils.toInteger(superAdminMinWeight);

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
    public int insert(User user){
        verifyUnique(user.getUsername());
        return super.insert(user);
    }

    /**
     * 重写更新
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int update(User user){
        return super.update(user);
    }

    /**
     * 重写分页
     * @param baseForm
     * @param <T>
     * @return
     */
    @Override
    public <T>PageInfo<T> queryPage(BaseForm<T> baseForm) {
        // 设置分页参数
        if( !ObjectUtils.isEmpty( baseForm.get("page") ) ){
            PageHelper.startPage(baseForm.getPageNo(), baseForm.getPageSize());
        }

        // 获取查询参数
        Map<String, Object> params = baseForm.getParameters();
        logger.info("分页请求参数："+params);

        List list = dao.queryPageExt(params);
        return new PageInfo<T>(list);
    }

    /**
     * 通过用户名查询
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = dao.findOne("username", username);

        if( user != null ){
            String userId = user.getId();

            // 关联科室
            Dept dept = deptService.fetch(user.getDeptId());
            if( dept != null ){
                user.setDept(dept);
                user.setDeptName(dept.getDeptName());
            }

            // 关联角色
            List<Role> roles = roleService.findByUserId(userId);
            user.setRoles(roles);

            // 关联操作权限
            List<Permission> permissions;
            if( user.isSuperAdmin() ){
                permissions = permsService.findByWeight(SUPER_WEIGHT, null, null);
            }else if( user.isAdmin() ){
                permissions = permsService.findByWeight(CommonConstant.ADMIN_PERMS_WEIGHT, CommonConstant.SUPER_ADMIN_PERMS_WEIGHT, null);
            }else{
                permissions = permsService.findByUserId(userId, null);
            }
            user.setPermissions(permissions);
        }
        return user;
    }

    /**
     * 验证登录账号是否可用
     * @param username
     */
    private void verifyUnique(String username){
        Map conditionMap = Maps.newHashMap();
        conditionMap.put("username", username);

        if( dao.count(conditionMap) > 0 ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在登录账号");
        }
    }
}