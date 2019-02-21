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

package cn.yibo.boot.modules.base.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.modules.base.entity.Message;
import cn.yibo.boot.modules.base.entity.MessageSend;
import cn.yibo.boot.modules.base.entity.User;
import cn.yibo.boot.modules.base.service.MessageService;
import cn.yibo.boot.modules.base.service.UserService;
import cn.yibo.common.utils.ListUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 消息推送表控制器层
 * @author 高云
 * @since 2019-02-20
 * @version v1.0
 */
@RestController
@RequestMapping("/api/message")
@Api(tags = "9009.消息推送管理")
public class MessageController extends CrudController<MessageService, Message>{
    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 重写新增
     * @param message
     * @return
     * @throws Exception
     */
    @Override
    public Object created(@Valid @RequestBody Message message) throws Exception{
        List<MessageSend> msgPushList = ListUtils.newArrayList();

        // 全部用户
        if(CommonConstant.MESSAGE_RANGE_ALL.equals(message.getRange())){
            List<User> allUser = userService.findAll();
            allUser.forEach(user -> {
                msgPushList.add(new MessageSend(user.getId()));
            });
            // 广播推送
            messagingTemplate.convertAndSend("/topic/subscribe", "您收到了新的系统消息");
         // 指定机构
        }else if(CommonConstant.MESSAGE_RANGE_ORGAN.equals(message.getRange())){
            if( !CollUtil.isEmpty(message.getOrganIdList()) ){
                Map<String, Object> condition = MapUtil.newHashMap();
                condition.put("organIds", message.getOrganIdList());

                List<User> userList = userService.findUserByIds(condition);
                userList.forEach(user -> {
                    msgPushList.add(new MessageSend(user.getId()));
                    // 指定推送
                    messagingTemplate.convertAndSendToUser(user.getId(),"/queue/subscribe", "您收到了新的消息");
                });
            }
        // 指定用户
        }else{
            if( !CollUtil.isEmpty(message.getUserIdList()) ){
                for( String userId : message.getUserIdList() ){
                    msgPushList.add(new MessageSend(userId));
                    // 指定推送
                    messagingTemplate.convertAndSendToUser(userId,"/queue/subscribe", "您收到了新的消息");
                }
            }
        }

        // 保存消息推送信息
        message.setMessagePushList(msgPushList);
        baseSevice.save(message);
        return message.getId();
    }

    @Override
    public void verifyUnique(Message entity) throws Exception{

    }
}