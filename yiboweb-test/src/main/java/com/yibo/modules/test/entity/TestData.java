/*
{*****************************************************************************
{  基础框架 v1.0.4
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.					
{  创建人：  高云
{  审查人：
{  模块：测试模块										
{  功能描述:										
{		 													
{  ---------------------------------------------------------------------------	
{  维护历史:													
{  日期        维护人        维护类型						
{  ---------------------------------------------------------------------------	
{  2018-11-13  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.test.entity;

import cn.yibo.base.entity.DataEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 测试数据(TestData)实体类
 * @author 高云
 * @since 2018-11-13
 * @version v1.0
 */
@Data
@ApiModel(value = "测试数据实体")
public class TestData extends DataEntity<String>{
    @NotEmpty(message="姓名不能为空")
    @ApiModelProperty(value = "姓名")
    private String name;
    
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @NotEmpty(message="姓名缩写不能为空")
    @ApiModelProperty(value = "缩写")
    private String shortName;
    
}