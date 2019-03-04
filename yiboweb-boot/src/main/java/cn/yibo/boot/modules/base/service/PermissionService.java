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

package cn.yibo.boot.modules.base.service;

import cn.yibo.boot.common.constant.CacheConstant;
import cn.yibo.boot.modules.base.dao.PermissionDao;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.common.base.service.IBaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 菜单权限表服务接口层
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@CacheConfig(cacheNames = CacheConstant.PERMS_CACHE_NAME)
public interface PermissionService extends IBaseService<PermissionDao, Permission> {
    /**
     * 根据类型查询权限
     * @param type
     * @return
     */
    List<Permission> findByType(String type);

    /**
     * 根据用户ID查询权限
     * @param userId
     * @return
     */
    @Cacheable(key = "#userId", unless = "#result == null")
    List<Permission> findByUserId(String userId);

    /**
     * 根据角色ID查询权限
     * @param roleId
     * @return
     */
    @Cacheable(key = "#roleId", unless = "#result == null")
    List<Permission> findByRoleId(String roleId);

    /**
     * 根据权重查询权限
     * @param min
     * @param max
     * @param type
     * @return
     */
    List<Permission> findByWeight(Integer min, Integer max, String type);
}