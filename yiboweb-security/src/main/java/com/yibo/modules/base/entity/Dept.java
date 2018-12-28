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
{  2018-12-12  高云        新建
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
import java.util.List;

/**
 * 科室表实体类(Dept)
 * @author 高云
 * @since 2018-12-12
 * @version v1.0
 */
@Data
@ApiModel(value = "科室表实体类(Dept)")
public class Dept extends DataEntity<String>{
    @ApiModelProperty(value = "父级ID")
    private String parentId;
    
    @ApiModelProperty(value = "所有父级ID")
    private String ancestorId;
    
    @NotEmpty(message="科室名称不能为空")
    @ApiModelProperty(value = "科室名称")
    private String deptName;

    @NotEmpty(message="科室简称不能为空")
    @ApiModelProperty(value = "科室简称")
    private String shortName;
    
    @ApiModelProperty(value = "科室编码(系统自动生成)")
    private String deptCode;
    
    @ApiModelProperty(value = "科室类型")
    private String deptType;
    
    @ApiModelProperty(value = "层级")
    private Double deptLevel;
    
    @ApiModelProperty(value = "排序(升序)")
    private Double deptSort;
    
    @ApiModelProperty(value = "科室负责人ID")
    private String leaderId;
    
    @ApiModelProperty(value = "联系电话")
    private String phone;

    //------------------------------------------------------------------------------------------------------------------
    // 扩展属性
    //------------------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "子菜单/权限")
    private List<Dept> children;

    @ApiModelProperty(value = "是否叶子节点 前端所需")
    private Boolean isLeaf = false;

    @ApiModelProperty(value = "是否选中 前端所需")
    private Boolean selected = false;

    @ApiModelProperty(value = "是否禁用 前端所需")
    private Boolean disabled = false;

    @ApiModelProperty(value = "科室负责人名称")
    private String leaderName;

    @Override
    public void preInsert(){
        super.preInsert();
        this.deptCode = RandomStringUtils.randomNumeric(CommonConstant.DEPT_CODE_NUM);
    }
}