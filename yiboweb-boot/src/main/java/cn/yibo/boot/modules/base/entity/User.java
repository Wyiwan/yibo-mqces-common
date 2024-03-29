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

package cn.yibo.boot.modules.base.entity;

import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.entity.DataEntity;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.common.utils.PropertiesUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 用户表实体类(sys_user)
 * @author 高云
 * @since 2018-12-17
 * @version v1.0
 */
@Data
@ApiModel(value = "用户表实体类")
public class User extends DataEntity<String> {
    private static String configUserInitPassword = ObjectUtils.toString(PropertiesUtils.getInstance().getProperty("webapp.user-init-password"));
    private static String configSuperAdminCode = ObjectUtils.toString(PropertiesUtils.getInstance().getProperty("webapp.super-admin-code"));
    private static final String USER_INIT_PASSWORD = StrUtil.isEmpty(configUserInitPassword) ? CommonConstant.USER_INIT_PASSWORD : configUserInitPassword;
    private static final String SUPER_ADMIN_CODE = StrUtil.isEmpty(configSuperAdminCode) ? CommonConstant.SUPER_ADMIN_ACCOUNT : configSuperAdminCode;

    @NotEmpty(message="登录账号不能为空")
    @ApiModelProperty(value = "登录账号")
    private String username;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "所属科室")
    private String deptId;

    @ApiModelProperty(value = "所属机构名称")
    private String organName;

    @ApiModelProperty(value = "所属科室名称")
    private String deptName;

    @NotEmpty(message="用户姓名不能为空")
    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "员工工号")
    private String empCode;

    @ApiModelProperty(value = "员工状态(1在职 2离职 3退休)")
    private String empStatus;

    @ApiModelProperty(value = "所在岗位(预留)")
    private String empPosts;

    @ApiModelProperty(value = "拼音编码")
    private String pinyinCode;
    
    @ApiModelProperty(value = "五笔编码")
    private String wubiCode;
    
    @ApiModelProperty(value = "身份证号码")
    private String identityNo;
    
    @ApiModelProperty(value = "用户籍贯")
    private String nativePlace;
    
    @ApiModelProperty(value = "用户性别(0女 1男)")
    private String sex;
    
    @ApiModelProperty(value = "用户生日")
    @JSONField(format="yyyy-MM-dd")
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

    @ApiModelProperty(value = "权重(倒序)")
    private Integer userWeight;
    
    @ApiModelProperty(value = "管理员类型(0非管理员 1系统管理员)")
    private String mgrType;
    
    @ApiModelProperty(value = "首次登录时间")
    @JSONField(format="yyyy-MM-dd HH:mm")
    private Date firstVisitDate;
    
    @ApiModelProperty(value = "最后登陆IP")
    private String lastVisitIp;
    
    @ApiModelProperty(value = "最后登录时间")
    @JSONField(format="yyyy-MM-dd HH:mm")
    private Date lastVisitDate;
    
    @ApiModelProperty(value = "允许访问开始时间")
    @JSONField(format="yyyy-MM-dd")
    private Date allowStartTime;

    @ApiModelProperty(value = "允许访问结束时间")
    @JSONField(format="yyyy-MM-dd")
    private Date allowEndTime;

    //------------------------------------------------------------------------------------------------------------------
    // 以下为扩展属性
    //------------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @JSONField(serialize = false)
    private Dept dept;

    @JsonIgnore
    @JSONField(serialize = false)
    private Role role;

    @JsonIgnore
    @JSONField(serialize = false)
    private List<Role> roles;

    @JsonIgnore
    @JSONField(serialize = false)
    private List<Permission> permissions;

    public boolean isAdmin(){
        return CommonConstant.USER_MGR_TYPE_ADMIN.equals(this.mgrType);
    }

    public boolean isSuperAdmin(){
        return isSuperAdmin(this.username);
    }

    public static boolean isSuperAdmin(String username){
        return SUPER_ADMIN_CODE.equals(username);
    }

    public String getRoleNames(){
        if( !ListUtils.isEmpty(roles) ){
            return ListUtils.extractToString(roles, "roleName", ",");
        }
        return "";
    }

    public void setRoleIds(String roleIds){
        roles = ListUtils.newArrayList();

        if( StrUtil.isNotBlank(roleIds) ){
            for( String id : roleIds.split(",") ){
                Role role = new Role();
                role.setId(id);
                roles.add(role);
            }
        }
    }

    @Override
    public void preInsert(){
        super.preInsert();
        initPassword();
    }

    public void initPassword(){
        this.password = new BCryptPasswordEncoder().encode(USER_INIT_PASSWORD);
    }

    public void preUpdateInfo(User oldUser){
        if( oldUser != null ){
            this.password = oldUser.getPassword();
            this.userType = oldUser.getUserType();
            this.mgrType = oldUser.getMgrType();
            this.firstVisitDate = oldUser.getFirstVisitDate();
            this.lastVisitDate = oldUser.getLastVisitDate();
            this.lastVisitIp = oldUser.getLastVisitIp();
            this.allowStartTime = oldUser.getAllowStartTime();
            this.allowEndTime = oldUser.getAllowEndTime();
            this.status = oldUser.getStatus();
            this.tenantId = oldUser.getTenantId();
        }
    }
}