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

package com.yibo.modules.base.service;

import cn.yibo.base.service.IBaseService;
import com.yibo.modules.base.dao.OfficeDao;
import com.yibo.modules.base.entity.Office;

/**
 * 医疗机构表服务接口层
 * @author 高云
 * @since 2018-12-14
 * @version v1.0
 */
public interface OfficeService extends IBaseService<OfficeDao, Office>{
    /**
     * 启用或停用
     * @param office
     * @return
     */
    void disabled(Office office);
}