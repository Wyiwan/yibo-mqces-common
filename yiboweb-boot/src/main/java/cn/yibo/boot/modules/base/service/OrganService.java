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

package cn.yibo.boot.modules.base.service;

import cn.yibo.boot.modules.base.dao.OrganDao;
import cn.yibo.boot.modules.base.entity.Organ;
import cn.yibo.common.base.service.IBaseService;

/**
 * 机构表服务接口层
 * @author 高云
 * @since 2018-12-14
 * @version v1.0
 */
public interface OrganService extends IBaseService<OrganDao, Organ> {
    /**
     * 启用或停用
     * @param organ
     * @return
     */
    void disabled(Organ organ);
}