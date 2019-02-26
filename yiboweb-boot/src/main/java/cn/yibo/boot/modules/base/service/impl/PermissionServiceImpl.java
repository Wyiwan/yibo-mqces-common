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

package cn.yibo.boot.modules.base.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.yibo.boot.common.constant.CacheConstant;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.modules.base.dao.PermissionDao;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.boot.modules.base.service.PermissionService;
import cn.yibo.common.base.service.impl.AbstractBaseService;
import cn.yibo.common.constant.StatusEnum;
import cn.yibo.core.cache.CacheUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 菜单权限表服务实现层
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class PermissionServiceImpl extends AbstractBaseService<PermissionDao, Permission> implements PermissionService {
    /**
     * 重写新增
     * @param permission
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insert(Permission permission){
        super.insert(permission);
        dao.updateAncestor(permission);

        CacheUtils.remove(CacheConstant.CACHE_MENU_LIST);
    }

    /**
     * 重写更新
     * @param permission
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void updateNull(Permission permission){
        super.updateNull(permission);
        dao.updateAncestor(permission);

        CacheUtils.remove(CacheConstant.CACHE_MENU_LIST);
        CacheUtils.removeAll(CacheConstant.USER_CACHE_NAME);
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        dao.deleteCascade(list);

        CacheUtils.remove(CacheConstant.CACHE_MENU_LIST);
        CacheUtils.removeAll(CacheConstant.USER_CACHE_NAME);
    }

    /**
     * 重写树结构查询
     * @return
     */
    @Override
    public List queryTree(Permission permission){
        List permissionList = (List)CacheUtils.get(CacheConstant.CACHE_MENU_LIST);

        if( permissionList == null ){
            permissionList = dao.queryTree(permission);
            CacheUtils.put(CacheConstant.CACHE_MENU_LIST, permissionList);
        }
        return permissionList;
    }

    /**
     * 根据类型查询权限
     * @param type
     * @return
     */
    @Override
    public List<Permission> findByType(String type){
        Map<String, Object> condition = MapUtil.newHashMap();
        condition.put("status", StatusEnum.N.getCode());
        condition.put("permsType", type);

        return dao.queryList(condition, "perms_sort", null);
    }

    /**
     * 根据用户ID查询权限
     * @param userId
     * @return
     */
    @Override
    public List<Permission> findByUserId(String userId, String type){
        return dao.findByUserId(userId, type);
    }

    /**
     * 根据角色ID查询权限
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> findByRoleId(String roleId) {
        return dao.findByRoleId(roleId);
    }

    /**
     * 根据权重查询权限
     * @param min
     * @param max
     * @param type
     * @return
     */
    @Override
    public List<Permission> findByWeight(Integer min, Integer max, String type) {
        return dao.findByWeight(min, max, type);
    }

}