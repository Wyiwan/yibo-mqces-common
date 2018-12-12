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
{  2018-12-03  高云        新建	
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

/**
 * 租户表实体类(Tenant)
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@Data
@ApiModel(value = "租户表实体类(Tenant)")
public class Tenant extends DataEntity<String>{
    @ApiModelProperty(value = "租户代码")
    private String tenantCode;
    
    @ApiModelProperty(value = "租户名称")
    private String tenantName;
    
}