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

package com.yibo.modules.base.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import cn.yibo.common.collect.ListUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.context.UserContext;
import cn.yibo.security.exception.LoginFailEnum;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.PermissionTree;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.service.PermissionService;
import com.yibo.modules.base.service.RoleService;
import com.yibo.modules.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 角色表实体控制器层类(Role)
 * @author 高云
 * @since 2018-12-20
 * @version v1.0
 */
@RestController
@RequestMapping("/api/role")
@Api(tags = "9003.角色管理")
public class RoleController extends BaseController{
   @Autowired
   private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;
   
    /**
     * 新增
     * @param role
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody Role role) throws Exception{
        VerifyRole(role, null);
        roleService.save(role);
        return role.getId();
    }
    
    /**
     * 修改
     * @param role
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@Valid @RequestBody Role role) throws Exception{
        VerifyRole(role, role.getId());
        roleService.save(role);
        return UPDATE_SUCCEED;
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ApiOperation("删除")
    @ApiImplicitParam(name = "ids", value = "标识ID(多个以逗号隔开)", paramType = "query", required = true, dataType = "String")
    @PostMapping("/deleted")
    public String deleted(@RequestBody String ids){
        roleService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DEL_SUCCEED;
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("启用或停用")
    @PostMapping("/disabled")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", required = true, dataType = "String")
    public String disabled(@RequestBody String id) throws Exception{
        Role fetchRole = VerifyRole(null, id);

        if( fetchRole != null ){
            fetchRole.enabled();
            roleService.update(fetchRole);
        }
        return OPER_SUCCEED;
    }

    /**
     * 单个查询
     * @param id
     * @return
     */
    @ApiOperation("单个查询")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @GetMapping("/fetched")
    public Role fetched(String id){
        Role vo = roleService.fetch(id);
        return vo == null ? new Role() : vo;
    }
    
    /**
     * 分页查询
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/paged")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number")
    })
    public PageInfo<T> paged(){
        return roleService.queryPage(new BaseForm<T>());
    }

    //------------------------------------------------------------------------------------------------------------------
    // @验证相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 唯一性校验
     * @return
     */
    @ApiOperation("验证角色编码是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify-code")
    public Boolean verifyUniqueCode(String id, String roleCode){
        Map conditionMap = Maps.newHashMap();
        conditionMap.put("id", id);
        conditionMap.put("roleCode", roleCode);
        return roleService.count(conditionMap) > 0 ? false : true;
    }

    @ApiOperation("验证角色名称是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "roleName", value = "角色名称", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify-name")
    public Boolean verifyUniqueName(String id, String roleName){
        Map conditionMap = Maps.newHashMap();
        conditionMap.put("id", id);
        conditionMap.put("roleName", roleName);
        conditionMap.put("tenantId", UserContext.getUser().getTenantId());
        return roleService.count(conditionMap) > 0 ? false : true;
    }

    /**
     * 内部方法：后端验证角色名称和角色编码
     * @param role 角色对象
     * @param roleId 角色ID，验证内置角色操作权限
     * @return
     */
    private Role VerifyRole(Role role, String roleId) throws Exception{
        Role fetchRole = null;
        if( role != null ){
            if( !verifyUniqueName(role.getId(), role.getRoleName()) ){
                throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色名称");
            }else if( !verifyUniqueCode(role.getId(), role.getRoleCode()) ){
                throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色编码");
            }
        }
        if( StringUtils.isNotBlank(roleId) ){
            fetchRole = roleService.fetch(roleId);

            if( !UserContext.getUser().isSuperAdmin() && fetchRole != null && CommonConstant.YES.equals(fetchRole.getIsSys()) ){
                throw new BusinessException(LoginFailEnum.UNDECLARED_ERROR.getCode(), "抱歉，您没有权限操作内置角色");
            }
        }
        return fetchRole;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @菜单授权相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取授权菜单的树结构
     * @return
     */
    @ApiOperation("获取授权菜单的树结构")
    @GetMapping("/get-grant-permission")
    public List getGrantPermision(){
        List<Permission> treeData = permissionService.getGrantPermission();
        return new PermissionTree(treeData).getTreeList();
    }

    /**
     * 获取已授权的菜单权限
     * @return
     */
    @ApiOperation("获取已授权的菜单权限")
    @GetMapping("/get-granted-permission")
    @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    public List getGrantedPermision(String roleId){
        List<Permission> permissionList = permissionService.findByRoleId(roleId);
        if( !ListUtils.isEmpty(permissionList) ){
            return ListUtils.extractToList(permissionList, "id");
        }
        return Lists.newArrayList();
    }

    /**
     * 菜单授权
     * @param role
     * @return
     */
    @ApiOperation("菜单授权")
    @PostMapping("/granted-permission")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "permissionIds", value = "菜单ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String grantedPermision(@RequestBody Role role) throws Exception{
        VerifyRole(null, role.getId());
        roleService.grantPermission(role);
        return "菜单授权成功";
    }

    //------------------------------------------------------------------------------------------------------------------
    // @分配用户相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取已授权的用户
     * @return
     */
    @ApiOperation("已授权用户的分页查询")
    @GetMapping("/granted-user-paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    })
    public PageInfo<T> getGrantedUser(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("queryType", "grantedUser");
        return userService.queryPage(baseForm);
    }

    /**
     * 获取未授权的用户
     * @return
     */
    @ApiOperation("未授权用户的分页查询")
    @GetMapping("/grant-user-paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    })
    public PageInfo<T> getGrantUser(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("queryType", "grantUser");
        return userService.queryPage(baseForm);
    }

    /**
     * 用户授权
     * @param role
     * @return
     */
    @ApiOperation("用户授权")
    @PostMapping("/granted-user")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "userIds", value = "用户ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String grantedUser(@RequestBody Role role){
        roleService.grantUser(role);
        return "分配用户成功";
    }

    /**
     * 取消用户授权
     * @param role
     * @return
     */
    @ApiOperation("取消用户授权")
    @PostMapping("/un-granted-user")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "userIds", value = "用户ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String unGrantedUser(@RequestBody Role role){
        roleService.unGrantUser(role);
        return "取消用户成功";
    }

}