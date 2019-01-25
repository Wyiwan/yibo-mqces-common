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
{  2019-01-25  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.service.impl;

import cn.yibo.base.service.impl.AbstractBaseService;
import com.yibo.modules.base.dao.DictItemDao;
import com.yibo.modules.base.entity.DictItem;
import com.yibo.modules.base.service.DictItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典数据项表服务实现层
 * @author 高云
 * @since 2019-01-25
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class DictItemServiceImpl extends AbstractBaseService<DictItemDao, DictItem> implements DictItemService {

}