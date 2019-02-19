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
{  2018-12-14  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.yibo.boot.base.service.impl.AbstractBaseService;
import cn.yibo.boot.modules.base.dao.OrganDao;
import cn.yibo.boot.modules.base.entity.Organ;
import cn.yibo.boot.modules.base.service.OrganService;
import cn.yibo.boot.modules.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 机构表服务实现层
 * @author 高云
 * @since 2018-12-14
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class OrganServiceImpl extends AbstractBaseService<OrganDao, Organ> implements OrganService {
    @Autowired
    UserService userService;

    /**
     * 重写更新
     * @param organ
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void updateNull(Organ organ){
        super.updateNull(organ);
        this.clearUsersCacheByTenantId(organ.getId());
    }

    @Override
    @Transactional(readOnly = false)
    public void disabled(Organ organ){
        if( organ != null ){
            organ.enabled();
            dao.update(organ);

            // 更新机构下所有用户的状态
            Map updateMap = CollUtil.newHashMap();
            updateMap.put("status", organ.getStatus());
            Map conditionMap = CollUtil.newHashMap();
            conditionMap.put("tenantId", organ.getId());
            userService.updateByCondition(updateMap, conditionMap);

            // 清除当前机构下的用户缓存
            this.clearUsersCacheByTenantId(organ.getId());
        }
    }
}