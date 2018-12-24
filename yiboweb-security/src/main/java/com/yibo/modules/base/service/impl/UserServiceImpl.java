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
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.context.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
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
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteByIds(List list) {
        // 清除用户缓存
        list.forEach(item -> {
            User user = dao.fetch(item);
            if( user != null ){
                CacheUtils.remove(CommonConstant.USER_CACHE, user.getUsername());
            }
        });
        return super.deleteByIds(list);
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

        // 管理员类型
        String mgrType = ObjectUtils.toString(params.get("mgrType"));
        if( ObjectUtils.isEmpty(mgrType) ){
            params.put("mgrType", CommonConstant.USER_MGR_TYPE_NORMAL);
        }

        // 普通用户查询关联租户
        if( CommonConstant.USER_MGR_TYPE_NORMAL.equals(mgrType) ){
            params.put("tenantId", UserContext.getUser().getTenantId());
        }
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

            // 关联操作权限
            List<Permission> permissions = permsService.findAccessTreeData(user);
            if( !ListUtils.isEmpty(permissions) ){
                user.setPermissions(permissions);
            }
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