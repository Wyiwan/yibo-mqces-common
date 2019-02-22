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

import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.entity.DataEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * 消息推送表实体类(sys_message)
 * @author 高云
 * @since 2019-02-20
 * @version v1.0
 */
@Data
@ApiModel(value = "消息推送表实体类")
public class Message extends DataEntity<String>{
    @NotEmpty(message="消息类型不能为空")
    @ApiModelProperty(value = "消息类型(0系统公告 1提醒)")
    private String messageType;
    
    @NotEmpty(message="消息标题不能为空")
    @ApiModelProperty(value = "消息标题")
    private String messageTitle;
    
    @NotEmpty(message="消息内容不能为空")
    @ApiModelProperty(value = "消息内容")
    private String messageContent;
    
    @ApiModelProperty(value = "新创建账号也推送")
    private Boolean creatorSend;

    //------------------------------------------------------------------------------------------------------------------
    // 以下为扩展属性
    //------------------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "推送范围")
    private Integer range;

    @ApiModelProperty(value = "指定机构")
    private String organIds;

    @ApiModelProperty(value = "指定用户")
    private String userIds;

    @JsonIgnore
    @JSONField(serialize = false)
    private List<String> organIdList;

    @JsonIgnore
    @JSONField(serialize = false)
    private List<String> userIdList;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "推送消息集合")
    private List<MessageSend> messageSendList;

    public void setOrganIds(String organIds) {
        if( StrUtil.isNotBlank(organIds) ){
            this.setOrganIdList( Arrays.asList(organIds.split(",")) );
        }
    }

    public void setUserIds(String userIds) {
        if( StrUtil.isNotBlank(userIds) ){
            this.setUserIdList( Arrays.asList(userIds.split(",")) );
        }
    }
}