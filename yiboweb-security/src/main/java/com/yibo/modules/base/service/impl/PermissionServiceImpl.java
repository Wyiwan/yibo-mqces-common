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

import cn.yibo.base.service.impl.AbstractBaseService;
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.security.constant.CommonConstant;
import com.google.common.collect.Maps;
import com.yibo.modules.base.dao.PermissionDao;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 菜单权限表实体服务实现层类(Permission)
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class PermissionServiceImpl extends AbstractBaseService<PermissionDao, Permission> implements PermissionService {
    /**
     * 重新新增方法
     * @param entity
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int insert(Permission entity) {
        int result = super.insert(entity);
        dao.updateAncestor(entity);
        return result;
    }

    /**
     * 重新删除方法
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteByIds(List list) {
        int result = dao.deleteCascade(list);

        // 清除用户缓存
        CacheUtils.removeAll(CommonConstant.USER_CACHE_NAME);
        return result;
    }

    /**
     * 重写修改方法
     * @param entity
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int update(Permission entity) {
        int result = super.update(entity);
        dao.updateAncestor(entity);

        // 清除用户缓存
        CacheUtils.removeAll(CommonConstant.USER_CACHE_NAME);
        return result;
    }

    @Override
    public List<Permission> findTree() {
        return dao.findTree();
    }

    @Override
    public List<Permission> findByType(String type){
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("status", CommonConstant.STATUS_NORMAL);
        condition.put("permsType", type);

        return dao.queryList(condition, null, null);
    }

    @Override
    public List<Permission> findByUserId(String userId, String type){
        return dao.findByUserId(userId, type);
    }

    @Override
    public List<Permission> findByWeight(Integer min, Integer max, String type) {
        return dao.findByWeight(min, max, type);
    }

}