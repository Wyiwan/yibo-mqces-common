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
{  2019-01-25  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.entity;

import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.common.base.entity.TreeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 字典数据项表实体类(sys_dict_item)
 * @author 高云
 * @since 2019-01-25
 * @version v1.0
 */
@Data
@ApiModel(value = "字典数据项表实体类")
public class DictItem extends TreeEntity<DictItem, String> {
    @ApiModelProperty(value = "字典类别")
    private String dictKind;

    @NotEmpty(message="字典类别ID不能为空")
    @ApiModelProperty(value = "字典类别ID")
    private String kindId;

    @NotEmpty(message="字典项名称不能为空")
    @ApiModelProperty(value = "字典项名称")
    private String itemName;
    
    @NotEmpty(message="字典值不能为空")
    @ApiModelProperty(value = "字典值")
    private String itemValue;
    
    @ApiModelProperty(value = "排序(升序)")
    private Double itemSort;
    
    @ApiModelProperty(value = "系统内置(1是 0否)")
    private String isSys = CommonConstant.NO;
}