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

import cn.hutool.core.util.StrUtil;
import cn.yibo.base.entity.DataEntity;
import cn.yibo.common.utils.ObjectUtils;
import com.yibo.modules.base.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 菜单权限表实体类(Permission)
 * @author 高云
 * @since 2018-12-04
 * @version v1.0
 */
@Data
@ApiModel(value = "菜单权限表实体类(Permission)")
public class Permission extends DataEntity<String>{
    @ApiModelProperty(value = "父级ID")
    private String parentId;
    
    @ApiModelProperty(value = "所有父级ID")
    private String ancestorId;
    
    @ApiModelProperty(value = "权限类型(0菜单页面 1操作权限)")
    @NotEmpty(message="类型不能为空")
    private String permsType;

    @ApiModelProperty(value = "权限名称")
    @NotEmpty(message="名称不能为空")
    private String permsName;
    
    @ApiModelProperty(value = "路径地址")
    private String permsUrl;
    
    @ApiModelProperty(value = "排序(升序)")
    private Integer permsSort;
    
    @ApiModelProperty(value = "图标")
    private String permsIcon;
    
    @ApiModelProperty(value = "层级")
    private Integer permsLevel;
    
    @ApiModelProperty(value = "权重")
    private Integer permsWeight;

    @Override
    public void preInsert(){
        preInit();
        super.preInsert();
    }

    @Override
    public void preUpdate(){
        preInit();
        super.preUpdate();
    }

    private void preInit(){
        this.permsType = StrUtil.emptyToDefault(this.permsType, CommonConstant.PERMISSION_OPERATION);
        if( ObjectUtils.isEmpty(this.permsWeight) ){
            this.permsWeight = CommonConstant.USER_PERMS_WEIGHT;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // 扩展属性
    //------------------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "子菜单/权限")
    private List<Permission> children;

    @ApiModelProperty(value = "是否叶子节点 前端所需")
    private Boolean isLeaf = false;


}