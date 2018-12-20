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

import cn.yibo.base.service.impl.AbstractBaseService;
import cn.yibo.common.collect.ListUtils;
import cn.yibo.security.context.UserContext;
import com.google.common.collect.Lists;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.dao.RoleDao;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteByIds(List list) {
        // 删除内置角色的权限验证
        if( !UserContext.getUser().isSuperAdmin() ){
            List tmpList = Lists.newArrayList();

            list.forEach(item -> {
                Role role = dao.fetch(item);
                if( role != null && CommonConstant.NO.equals(role.getIsSys()) ){
                    tmpList.add(item);
                }
            });
            if( !ListUtils.isEmpty(tmpList) ){
                return super.deleteByIds(tmpList);
            }
            return 0;
        }
        return super.deleteByIds(list);
    }

    @Override
    public List<Role> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }
}