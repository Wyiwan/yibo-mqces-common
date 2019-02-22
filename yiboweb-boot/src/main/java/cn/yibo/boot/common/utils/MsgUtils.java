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
{  2019-02-20  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的系统模块
{*****************************************************************************
*/
package cn.yibo.boot.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.modules.base.entity.Message;
import cn.yibo.boot.modules.base.entity.MessageSend;
import cn.yibo.boot.modules.base.service.MessageSendService;
import cn.yibo.boot.modules.base.service.MessageService;
import cn.yibo.common.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 消息发送工具类
 * @author 高云
 * @since 2019-02-20
 * @version v1.0
 */
@Component
public class MsgUtils {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSendService messageSendService;

    @Async
    public void sendMessage(String userId){
        List<Message> messageList = messageService.findList("creator_send", true, "create_date", "desc");
        List<MessageSend> sendList = ListUtils.newArrayList();

        if( !CollUtil.isEmpty(messageList) ){
            messageList.forEach(message -> {
                MessageSend ms = new MessageSend();
                ms.setUserId(userId);
                ms.setMessageId(message.getId());
                ms.setStatus(CommonConstant.MESSAGE_STATUS_UNREAD);
                ms.setCreateDate(new Date());
                ms.setUpdateDate(ms.getCreateDate());
                ms.setUpdateBy(message.getUpdateBy());
                ms.setCreateBy(message.getCreateBy());
                sendList.add(ms);
            });
            messageSendService.batchInsert(sendList);
        }
    }

}
