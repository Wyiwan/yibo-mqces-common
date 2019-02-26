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
{  2019-02-20  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.service.impl;

import cn.yibo.boot.modules.base.dao.MessageDao;
import cn.yibo.boot.modules.base.entity.Message;
import cn.yibo.boot.modules.base.service.MessageService;
import cn.yibo.common.base.service.impl.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息推送表服务实现层
 * @author 高云
 * @since 2019-02-20
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class MessageServiceImpl extends AbstractBaseService<MessageDao, Message> implements MessageService {
    /**
     * 重写新增
     * @param message
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insert(Message message){
        dao.insertExt(message);
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        dao.deleteByIdsExt(list);
    }
}