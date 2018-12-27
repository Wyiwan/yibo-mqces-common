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
{  2018-12-20  高云        新建	
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
import com.yibo.modules.base.dao.RoleDao;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 角色表实体服务实现层类(Role)
 * @author 高云
 * @since 2018-12-20
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class RoleServiceImpl extends AbstractBaseService<RoleDao, Role> implements RoleService {
    /**
     * 重写新增
     * @param role
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insert(Role role){
        super.insert(role);

        if( !ListUtils.isEmpty(role.getPermissionIdList()) ){
            this.grantPermission(role);
        }
    }

    /**
     * 重写更新
     * @param role
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void update(Role role){
        super.update(role);
        this.clearUsersCacheByRoleId( Lists.newArrayList(role.getId()) );
    }

    /**
     * 重写删除
     * 操作内置角色的权限验证
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        List tmpList = Lists.newArrayList();

        if( !UserContext.getUser().isSuperAdmin() && !ListUtils.isEmpty(list)){
            for(int i = 0 ; i < list.size() ; i++){
                String roleId = ObjectUtils.toString(list.get(i));
                Role role = dao.fetch(list.get(i));

                if( role != null && CommonConstant.NO.equals(role.getIsSys()) ){
                    tmpList.add(roleId);
                }
            }
        }else{
            tmpList = list;
        }

        super.deleteByIds(tmpList);
        this.clearUsersCacheByRoleId(tmpList);
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

        // 如果不是超级管理员
        if( !UserContext.getUser().isSuperAdmin() ){
            params.put("userWeight", UserContext.getUser().getUserWeight());
        }
        params.put("tenantId", UserContext.getUser().getTenantId());
        logger.info("分页请求参数："+params);

        List list = dao.queryPageExt(params);
        return new PageInfo<T>(list);
    }

    /**
     * 根据用户ID查询角色
     * @param userId
     * @return
     */
    @Override
    public List<Role> findByUserId(String userId){
        return dao.findByUserId(userId);
    }

    /**
     * 菜单授权
     * @param role
     */
    @Override
    @Transactional(readOnly = false)
    public void grantPermission(Role role){
        dao.grantPermission(role);
        this.clearUsersCacheByRoleId( Lists.newArrayList(role.getId()) );
    }

    /**
     * 用户授权
     * @param role
     */
    @Override
    @Transactional(readOnly = false)
    public void grantUser(Role role){
        dao.grantUser(role);
        this.clearUsersCacheByUserId(role.getUserIdList());
    }

    /**
     * 取消用户授权
     * @param role
     */
    @Override
    @Transactional(readOnly = false)
    public void unGrantUser(Role role){
        dao.unGrantUser(role);
        this.clearUsersCacheByUserId(role.getUserIdList());
    }

    /**
     * 根据角色ID清除用户缓存
     * @param roleIdList
     */
    private void clearUsersCacheByRoleId(List roleIdList){
        if( !ListUtils.isEmpty(roleIdList) ){
            ClearUserCacheThread clearUserCacheThread = new ClearUserCacheThread();
            clearUserCacheThread.setRoleIdList(roleIdList);
            clearUserCacheThread.start();
        }
    }

}