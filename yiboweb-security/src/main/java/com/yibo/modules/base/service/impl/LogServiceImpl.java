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
{  2019-01-07  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.service.impl;

import cn.yibo.base.controller.BaseForm;
import cn.yibo.base.service.impl.AbstractBaseService;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.security.context.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yibo.modules.base.dao.LogDao;
import com.yibo.modules.base.entity.Log;
import com.yibo.modules.base.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 操作日志表实体服务实现层类(Log)
 * @author 高云
 * @since 2019-01-07
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class LogServiceImpl extends AbstractBaseService<LogDao, Log> implements LogService {
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
        params.put("tenantId", UserContext.getUser().getTenantId());
        this.logger.info("分页请求参数："+params);

        List list = dao.queryPage(params);
        return new PageInfo<T>(list);
    }

}