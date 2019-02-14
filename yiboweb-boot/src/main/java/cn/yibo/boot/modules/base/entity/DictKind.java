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
{  2019-01-22  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.entity;

import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.entity.TreeEntity;
import cn.yibo.boot.common.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 字典类别表实体类(sys_dict_kind)
 * @author 高云
 * @since 2019-01-22
 * @version v1.0
 */
@Data
@ApiModel(value = "字典类别表实体类")
public class DictKind extends TreeEntity<DictKind, String> {
    @NotEmpty(message="字典名称不能为空")
    @ApiModelProperty(value = "字典名称")
    private String dictName;
    
    @NotEmpty(message="字典类别不能为空")
    @ApiModelProperty(value = "字典类别")
    private String dictKind;
    
    @ApiModelProperty(value = "排序（升序）")
    private Double dictSort;
    
    @ApiModelProperty(value = "系统内置（0否 1是）")
    private String isSys;
    
    @ApiModelProperty(value = "树型结构（0否 1是）")
    private String isTree;

    @Override
    public void onBeforeSave(){
        this.isTree = StrUtil.emptyToDefault(this.isTree, CommonConstant.NO);
        this.isSys = StrUtil.emptyToDefault(this.isSys, CommonConstant.NO);
    }
}