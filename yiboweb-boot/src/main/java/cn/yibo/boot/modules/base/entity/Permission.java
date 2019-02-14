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

package cn.yibo.boot.modules.base.entity;

import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.entity.TreeEntity;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.common.utils.ObjectUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 菜单权限表实体类(sys_permission)
 * @author 高云
 * @since 2018-12-04
 * @version v1.0
 */
@Data
@ApiModel(value = "菜单权限表实体类")
public class Permission extends TreeEntity<Permission, String> {
    @ApiModelProperty(value = "权限类型(0菜单页面 1操作权限)")
    @NotEmpty(message="类型不能为空")
    private String permsType;

    @ApiModelProperty(value = "权限名称")
    @NotEmpty(message="名称不能为空")
    private String permsName;
    
    @ApiModelProperty(value = "路径地址")
    @NotEmpty(message="路径地址不能为空")
    private String permsUrl;

    @ApiModelProperty(value = "链接地址")
    private String permsHref;
    
    @ApiModelProperty(value = "排序(升序)")
    private Integer permsSort;
    
    @ApiModelProperty(value = "图标")
    private String permsIcon;
    
    @ApiModelProperty(value = "层级")
    private Integer permsLevel;
    
    @ApiModelProperty(value = "权重")
    private Integer permsWeight;

    @ApiModelProperty(value = "按钮类型")
    private String buttonType;

    @Override
    public void onBeforeSave(){
        this.permsType = StrUtil.emptyToDefault(this.permsType, CommonConstant.PERMISSION_OPERATION);
        if( ObjectUtils.isEmpty(this.permsWeight) ){
            this.permsWeight = CommonConstant.USER_PERMS_WEIGHT;
        }
    }
}