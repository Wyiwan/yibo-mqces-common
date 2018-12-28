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
{  2018-12-20  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.entity;

import cn.yibo.base.entity.DataEntity;
import cn.yibo.common.lang.StringUtils;
import com.yibo.modules.base.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * 角色表实体类(Role)
 * @author 高云
 * @since 2018-12-20
 * @version v1.0
 */
@Data
@ApiModel(value = "角色表实体类(Role)")
public class Role extends DataEntity<String>{
    @NotEmpty(message="角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @NotEmpty(message="角色编码不能为空")
    @ApiModelProperty(value = "角色编码")
    private String roleCode;
    
    @ApiModelProperty(value = "角色权重（倒序）")
    private Integer roleWeight;
    
    @ApiModelProperty(value = "角色分类（预留）")
    private String roleType;
    
    @ApiModelProperty(value = "用户类型（normal普通用户 expert专家）")
    private String userType;
    
    @ApiModelProperty(value = "系统内置（0否 1是）")
    private String isSys;

    @ApiModelProperty(value = "角色所对应的权限")
    private List<RolePermission> rolePermissions;

    @ApiModelProperty(value = "角色所对应的用户")
    private List<UserRole> userRoles;

    //------------------------------------------------------------------------------------------------------------------
    // 以下为扩展属性
    //------------------------------------------------------------------------------------------------------------------
    private String permissionIds;

    private List<String> permissionIdList;

    private String userIds;

    private List<String> userIdList;

    public void setPermissionIds(String permissionIds){
        if( StringUtils.isNotBlank(permissionIds) ){
            this.setPermissionIdList( Arrays.asList(permissionIds.split(",")) );
        }
    }

    public void setUserIds(String userIds){
        if( StringUtils.isNotBlank(userIds) ){
            this.setUserIdList( Arrays.asList(userIds.split(",")) );
        }
    }

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
        if( StringUtils.isBlank(this.userType) ){
            this.userType = CommonConstant.USER_TYPE_NORMAL;
        }
        if( StringUtils.isBlank(this.isSys) ){
            this.isSys = CommonConstant.NO;
        }
    }
}