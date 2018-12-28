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
{  2018-12-14  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.entity;

import cn.yibo.base.entity.DataEntity;
import com.yibo.modules.base.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import javax.validation.constraints.NotEmpty;

/**
 * 医疗机构表实体类(Office)
 * @author 高云
 * @since 2018-12-14
 * @version v1.0
 */
@Data
@ApiModel(value = "医疗机构表实体类(Office)")
public class Office extends DataEntity<String>{
    @NotEmpty(message="机构名称不能为空")
    @ApiModelProperty(value = "机构名称")
    private String officeName;
    
    @ApiModelProperty(value = "机构编码(系统自动生成)")
    private String officeCode;
    
    @ApiModelProperty(value = "机构类型(1二甲 2三甲 3其他)")
    private String officeType;

    @ApiModelProperty(value = "排序(升序)")
    private Double officeSort;
    
    @ApiModelProperty(value = "机构简称")
    private String shortName;
    
    @ApiModelProperty(value = "质量总监ID")
    private String directorId;

    @ApiModelProperty(value = "口号")
    private String slogan;
    
    @ApiModelProperty(value = "简介")
    private String introduction;
    
    @ApiModelProperty(value = "联系电话")
    private String phone;
    
    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "图片(预留字段)")
    private String avatar;

    @ApiModelProperty(value = "父级ID(预留字段)")
    private String parentId;

    @ApiModelProperty(value = "所有父级ID(预留字段)")
    private String ancestorId;

    @ApiModelProperty(value = "层级(预留字段)")
    private Double officeLevel;

    @Override
    public void preInsert(){
        super.preInsert();
        this.officeCode = RandomStringUtils.randomNumeric(CommonConstant.OFFICE_CODE_NUM);
    }

    public void disabled(){
        this.status = (this.status == CommonConstant.STATUS_NORMAL ? CommonConstant.STATUS_DISABLE : CommonConstant.STATUS_NORMAL);
    }

    //------------------------------------------------------------------------------------------------------------------
    // 扩展属性
    //------------------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "是否选中 前端所需")
    private Boolean selected = false;

    @ApiModelProperty(value = "是否禁用 前端所需")
    private Boolean disabled = false;
}