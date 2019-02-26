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

package cn.yibo.boot.modules.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.yibo.boot.common.aync.ClearUserCacheThread;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.common.constant.LoginFailEnum;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.dao.RoleDao;
import cn.yibo.boot.modules.base.entity.Role;
import cn.yibo.boot.modules.base.service.RoleService;
import cn.yibo.common.base.controller.BaseForm;
import cn.yibo.common.base.service.impl.AbstractBaseService;
import cn.yibo.core.web.exception.BizException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 角色表服务实现层
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

        if( !CollUtil.isEmpty(role.getPermissionIdList()) ){
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
    public void updateNull(Role role){
        super.updateNull(role);
        clearUsersCacheByIds(CollUtil.newArrayList(role.getId()), null);
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
        if( !UserContext.getUser().isSuperAdmin() ){
            Map<String, Object> condition = MapUtil.newHashMap();
            condition.put("ids", list);

            List<Role> roleList = dao.queryList(condition, null, null);
            if( !CollUtil.isEmpty(roleList) ){
                for( Role role : roleList ){
                    if( CommonConstant.YES.equals(role.getIsSys()) ){
                        throw new BizException(LoginFailEnum.UNDECLARED_ERROR.getCode(), "抱歉，您没有权限操作内置角色");
                    }
                }
            }
        }
        super.deleteByIds(list);
        clearUsersCacheByIds(list, null);
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
        PageHelper.startPage(baseForm.getPageNo(), baseForm.getPageSize());

        // 获取查询参数
        Map<String, Object> params = baseForm.getParameters();

        // 如果不是超级管理员
        if( !UserContext.getUser().isSuperAdmin() ){
            params.put("userWeight", UserContext.getUser().getUserWeight());
        }
        params.put("tenantId", UserContext.getUser().getTenantId());
        this.logger.info("分页请求参数："+params);

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
        clearUsersCacheByIds(CollUtil.newArrayList(role.getId()),null);
    }

    /**
     * 用户授权
     * @param role
     */
    @Override
    @Transactional(readOnly = false)
    public void grantUser(Role role){
        dao.grantUser(role);
        clearUsersCacheByIds(null, role.getUserIdList());
    }

    /**
     * 取消用户授权
     * @param role
     */
    @Override
    @Transactional(readOnly = false)
    public void unGrantUser(Role role){
        dao.unGrantUser(role);
        clearUsersCacheByIds(null, role.getUserIdList());
    }

    /**
     * 清除用户缓存
     * @param roleIdList
     * @param userIdList
     */
    public void clearUsersCacheByIds(List roleIdList, List userIdList){
        if( !CollUtil.isEmpty(roleIdList) ){
            ClearUserCacheThread clearUserCacheThread = new ClearUserCacheThread();
            clearUserCacheThread.setRoleIdList(roleIdList);
            ThreadUtil.execute(clearUserCacheThread);
        }

        if( !CollUtil.isEmpty(userIdList) ){
            ClearUserCacheThread clearUserCacheThread = new ClearUserCacheThread();
            clearUserCacheThread.setUserIdList(userIdList);
            ThreadUtil.execute(clearUserCacheThread);
        }
    }
}