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

package cn.yibo.boot.modules.base.entity;

import cn.yibo.boot.base.entity.DataEntity;
import cn.yibo.boot.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 消息已推送表实体类(sys_message_send)
 * @author 高云
 * @since 2019-02-20
 * @version v1.0
 */
@Data
@ApiModel(value = "消息已推送表实体类")
public class MessageSend extends DataEntity<String>{
    public MessageSend(){
        super();
    }

    public MessageSend(String userId){
        this.userId = userId;
    }

    @NotEmpty(message="用户ID不能为空")
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @NotEmpty(message="消息ID不能为空")
    @ApiModelProperty(value = "消息ID")
    private String messageId;

    @ApiModelProperty(value = "状态(0删除 1正常 3未读 4已读 5回收站)")
    private Integer status = CommonConstant.MESSAGE_STATUS_UNREAD;

    //------------------------------------------------------------------------------------------------------------------
    // 以下为扩展属性
    //------------------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "消息标题")
    private String messageTitle;

    @ApiModelProperty(value = "用户姓名")
    private String username;

    @ApiModelProperty(value = "用户所属机构")
    private String organName;
}