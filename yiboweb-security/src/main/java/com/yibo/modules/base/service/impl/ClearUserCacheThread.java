package com.yibo.modules.base.service.impl;

import cn.yibo.common.collect.ListUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.core.web.context.SpringContextHolder;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.dao.UserDao;
import com.yibo.modules.base.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 描述: 清除用户缓存线程
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-12-26
 * 版本: v1.0
 */
public class ClearUserCacheThread extends Thread{
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String tenantId;
    private List userIdList;
    private List roleIdList;

    public ClearUserCacheThread(){
        super();
    }

    public void setTenantId(String tenantId){
        this.tenantId = tenantId;
    }

    public void setUserIdList(List userIdList){
        this.userIdList = userIdList;
    }

    public void setRoleIdList(List roleIdList){
        this.roleIdList = roleIdList;
    }

    @Override
    public void run(){
        UserDao userDao = SpringContextHolder.getBean("userDao");
        if( userDao != null ){
            long startTime = System.currentTimeMillis();
            logger.info("清除用户缓存中...");

            if( !ListUtils.isEmpty(userIdList) ){
                List<String> list = userDao.findUsernameByIds(userIdList);
                this.clearCaches(list);
            }

            if( !ListUtils.isEmpty(roleIdList) ){
                List<String> list = userDao.findUsernameByRoleIds(roleIdList);
                this.clearCaches(list);
            }

            if( StringUtils.isNotBlank(tenantId) ){
                List<User> userList = userDao.findList("tenant_id",tenantId, null, null);

                if( !ListUtils.isEmpty(userList) ){
                    List<String> list = ListUtils.extractToList(userList, "username");
                    this.clearCaches(list);
                }
            }

            long endTime = System.currentTimeMillis();
            logger.info("清除用户缓存完成，耗时：" + (endTime-startTime)+ "ms.");
        }
    }

    private void clearCaches(List<String> list){
        if( !ListUtils.isEmpty(list) ){
            for(int i = 0 ; i < list.size() ; i++){
                CacheUtils.remove(CommonConstant.USER_CACHE, list.get(i));
                logger.info("清除用户[ "+list.get(i)+" ]缓存...");
            }
        }
    }
}
