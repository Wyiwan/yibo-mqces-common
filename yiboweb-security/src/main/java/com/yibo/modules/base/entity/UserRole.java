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
{  2018-12-24  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.entity;

import cn.yibo.base.entity.DataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 用户角色关系表实体类(sys_user_role)
 * @author 高云
 * @since 2018-12-24
 * @version v1.0
 */
@Data
@ApiModel(value = "用户角色关系表实体类")
public class UserRole extends DataEntity<String>{
    @NotEmpty(message="用户ID不能为空")
    @ApiModelProperty(value = "用户ID")
    private String userId;
    
    @NotEmpty(message="角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    private String roleId;
    
}