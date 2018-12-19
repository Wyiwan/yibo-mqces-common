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
{  2018-12-17  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.entity;

import cn.yibo.base.entity.DataEntity;
import cn.yibo.common.io.PropertiesUtils;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.security.constant.CommonConstant;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 用户表实体类(User)
 * @author 高云
 * @since 2018-12-17
 * @version v1.0
 */
@Data
@ApiModel(value = "用户表实体类(User)")
public class User extends DataEntity<String>{
    private static String userPassword = ObjectUtils.toString(PropertiesUtils.getInstance().getProperty("webapp.user-init-password"));
    private static String superAdminCode = ObjectUtils.toString(PropertiesUtils.getInstance().getProperty("webapp.super-admin-code"));

    public static final String INIT_PASSWORD = ObjectUtils.isEmpty(userPassword) ? CommonConstant.USER_INIT_PASSWORD : userPassword;
    private static final String SUPER_ADMIN_CODE = ObjectUtils.isEmpty(superAdminCode) ? CommonConstant.SUPER_ADMIN_ACCOUNT : superAdminCode;

    @NotEmpty(message="登录账号不能为空")
    @ApiModelProperty(value = "登录账号")
    private String username;

    @ApiModelProperty(value = "登录密码")
    @JSONField(serialize = false)
    private String password;

    @ApiModelProperty(value = "所属科室")
    private String deptId;

    @NotEmpty(message="用户姓名不能为空")
    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "员工编号")
    private String empCode;

    @ApiModelProperty(value = "员工状态：1：在职  2：离职  3：退休")
    private String empStatus;

    @ApiModelProperty(value = "所在岗位（预留）")
    private String empPosts;

    @ApiModelProperty(value = "姓名简称")
    private String shortName;
    
    @ApiModelProperty(value = "五笔编码")
    private String wubiCode;
    
    @ApiModelProperty(value = "身份证号码")
    private String identityNo;
    
    @ApiModelProperty(value = "用户籍贯")
    private String nativePlace;
    
    @ApiModelProperty(value = "用户性别：0女  1男")
    private String sex;
    
    @ApiModelProperty(value = "用户生日")
    private Date birthday;
    
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    
    @ApiModelProperty(value = "邮箱地址")
    private String email;
    
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    
    @ApiModelProperty(value = "办公电话")
    private String telephone;
    
    @ApiModelProperty(value = "用户类型")
    private String userType;
    
    @ApiModelProperty(value = "用户权重（倒序）")
    private Double userWeight;
    
    @ApiModelProperty(value = "管理员类型：0非管理员  1系统管理员")
    private String mgrType;
    
    @ApiModelProperty(value = "首次登录时间")
    private Date firstVisitDate;
    
    @ApiModelProperty(value = "最后登陆IP")
    private String lastVisitIp;
    
    @ApiModelProperty(value = "最后登录时间")
    private Date lastVisitDate;
    
    @ApiModelProperty(value = "允许访问开始时间")
    private Date allowStartTime;
    
    @ApiModelProperty(value = "允许访问结束时间")
    private Date allowEndTime;

    //------------------------------------------------------------------------------------------------------------------
    // 以下为扩展属性
    //------------------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "所属机构名称")
    private String officeName;

    @ApiModelProperty(value = "所属科室名称")
    private String deptName;

    @ApiModelProperty(value = "用户所属科室")
    private Dept dept;

    @ApiModelProperty(value = "用户拥有的角色")
    private List<Role> roles;

    @ApiModelProperty(value = "用户拥有的权限")
    private List<Permission> permissions;

    public boolean isAdmin(){
        return CommonConstant.USER_TYPE_ADMIN.equals(this.mgrType);
    }

    public boolean isSuperAdmin(){
        return isSuperAdmin(this.username);
    }

    public static boolean isSuperAdmin(String username){
        return SUPER_ADMIN_CODE.equals(username);
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

    public void statusSwitch(){
        this.status = (this.status == CommonConstant.STATUS_NORMAL ? CommonConstant.STATUS_DISABLE : CommonConstant.STATUS_NORMAL);
    }

    private void preInit(){
        if( StringUtils.isBlank(this.userType) ){
            this.userType = "none";
        }
        if( StringUtils.isBlank(this.mgrType) ){
            this.mgrType = CommonConstant.USER_TYPE_NORMAL;
        }
        if( StringUtils.isBlank(this.password) ){
            this.password = new BCryptPasswordEncoder().encode(INIT_PASSWORD);
        }
    }

}