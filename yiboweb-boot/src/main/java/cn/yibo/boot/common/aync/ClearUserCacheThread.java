/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：日志记录模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-12-26  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的系统模块
{*****************************************************************************
*/
package cn.yibo.boot.common.aync;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.common.constant.CacheConstant;
import cn.yibo.boot.modules.base.dao.UserDao;
import cn.yibo.boot.modules.base.entity.User;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.core.web.context.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 描述: 清除用户缓存线程类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-12-26
 * 版本: v1.0
 */
@Slf4j
public class ClearUserCacheThread extends Thread{
    private String tenantId;
    private List<String> userIdList;
    private List<String>  roleIdList;
    private List<String>  deptIdList;

    public ClearUserCacheThread(){
        super();
    }

    public void setTenantId(String tenantId){
        this.tenantId = tenantId;
    }

    public void setUserIdList(List<String>  userIdList){
        this.userIdList = userIdList;
    }

    public void setRoleIdList(List<String>  roleIdList){
        this.roleIdList = roleIdList;
    }

    public void setDeptIdList(List<String>  deptIdList) {
        this.deptIdList = deptIdList;
    }

    @Override
    public void run(){
        UserDao userDao = SpringContextHolder.getBean(UserDao.class);

        if( userDao != null ){
            TimeInterval timer = DateUtil.timer();
            log.info("======================= 清除缓存Start =======================");

            if( !ListUtils.isEmpty(userIdList) ){
                Map<String, Object> condition = MapUtil.newHashMap();
                condition.put("ids", userIdList);
                List<User> userList = userDao.findByIds(condition);

                this.clearUserCaches(ListUtils.extractToList(userList, "username"));
                this.clearUserPermsCaches(userIdList);
            }

            if( !ListUtils.isEmpty(roleIdList) ){
                Map<String, Object> condition = MapUtil.newHashMap();
                condition.put("roleIds", roleIdList);
                List<User> userList = userDao.findByIds(condition);

                this.clearUserCaches(ListUtils.extractToList(userList, "username"));
                this.clearUserPermsCaches(ListUtils.extractToList(userList, "id"));
                this.clearRolePermsCaches(roleIdList);
            }

            if( !ListUtils.isEmpty(deptIdList) ){
                Map<String, Object> condition = MapUtil.newHashMap();
                condition.put("deptIds", deptIdList);
                List<User> userList = userDao.findByIds(condition);

                this.clearUserCaches( ListUtils.extractToList(userList, "username") );
            }

            if( StrUtil.isNotBlank(tenantId) ){
                List<User> userList = userDao.findList("tenant_id",tenantId, null, null);
                this.clearUserCaches( ListUtils.extractToList(userList, "username") );
            }
            log.info("=======================清除缓存End，耗时：" + timer.interval()+ "ms");
        }
    }

    private void clearUserCaches(List<String> list){
        if( !ListUtils.isEmpty(list) ){
            for( String username : list ){
                CacheUtils.remove(CacheConstant.USER_CACHE_NAME, username);
                log.info("清除用户[ "+username+" ]缓存...");
            }
        }
    }

    private void clearUserPermsCaches(List<String> list){
        if( !ListUtils.isEmpty(list) ){
            for( String userId : list ){
                CacheUtils.remove(CacheConstant.PERMS_CACHE_NAME, userId);
                log.info("清除用户[ "+userId+" ]的权限缓存...");
            }
        }
    }

    private void clearRolePermsCaches(List<String> list){
        if( !ListUtils.isEmpty(list) ){
            for( String roleId : list ){
                CacheUtils.remove(CacheConstant.PERMS_CACHE_NAME, roleId);
                log.info("清除角色[ "+roleId+" ]的权限缓存...");
            }
        }
    }
}
