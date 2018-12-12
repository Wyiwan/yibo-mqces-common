/*
{*****************************************************************************
{  广州医博-基础框架 v1.0													
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.					
{  创建人：  高云
{  审查人：
{  模块：安全控制模块										
{  功能描述:										
{		 													
{  ---------------------------------------------------------------------------	
{  维护历史:													
{  日期        维护人        维护类型						
{  ---------------------------------------------------------------------------	
{  2018-12-12  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.test.service.impl;

import cn.yibo.base.service.impl.AbstractBaseService;
import com.yibo.modules.test.dao.TestDataDao;
import com.yibo.modules.test.entity.TestData;
import com.yibo.modules.test.service.TestDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试数据实体服务实现层类(TestData)
 * @author 高云
 * @since 2018-12-12
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class TestDataServiceImpl extends AbstractBaseService<TestDataDao, TestData> implements TestDataService {

}