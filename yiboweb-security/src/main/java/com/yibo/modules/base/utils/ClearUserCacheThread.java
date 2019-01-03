package com.yibo.modules.base.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.yibo.common.collect.ListUtils;
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.core.web.context.SpringContextHolder;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.dao.UserDao;
import com.yibo.modules.base.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 描述: 清除用户缓存线程
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-12-26
 * 版本: v1.0
 */
@Slf4j
public class ClearUserCacheThread extends Thread{
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
            TimeInterval timer = DateUtil.timer();
            log.info("清除用户缓存中...");

            if( !CollUtil.isEmpty(userIdList) ){
                List<String> list = userDao.findUsernameByIds(userIdList);
                this.clearCaches(list);
            }

            if( !CollUtil.isEmpty(roleIdList) ){
                List<String> list = userDao.findUsernameByRoleIds(roleIdList);
                this.clearCaches(list);
            }

            if( StrUtil.isNotBlank(tenantId) ){
                List<User> userList = userDao.findList("tenant_id",tenantId, null, null);

                if( !CollUtil.isEmpty(userList) ){
                    List<String> list = ListUtils.extractToList(userList, "username");
                    this.clearCaches(list);
                }
            }
            log.info("清除用户缓存完成，耗时：" + timer.interval()+ "ms");
        }
    }

    private void clearCaches(List<String> list){
        if( !CollUtil.isEmpty(list) ){
            for(int i = 0 ; i < list.size() ; i++){
                CacheUtils.remove(CommonConstant.USER_CACHE, list.get(i));
                log.info("清除用户[ "+list.get(i)+" ]缓存...");
            }
        }
    }
}
