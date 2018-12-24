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
import cn.yibo.common.collect.MapUtils;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.exception.LoginFailEnum;
import com.github.pagehelper.PageInfo;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.PermissionTree;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.service.PermissionService;
import com.yibo.modules.base.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
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
@Api(tags = "1005.角色管理")
public class RoleController extends BaseController{
   @Autowired
   private RoleService roleService;

    @Autowired
    private PermissionService permissionService;
   
    /**
     * 新增
     * @param role
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody Role role) throws Exception{
        Role vo = VerifyRole(role, false);
        roleService.insert(vo);
        return role.getId();
    }
    
    /**
     * 修改
     * @param role
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@RequestBody Role role) throws Exception{
        Role vo = VerifyRole(role, false);
        BeanUtils.copyProperties(role, vo, ObjectUtils.getNullPropertyNames(role));

        roleService.update(vo);
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
     * @param role
     * @return
     */
    @ApiOperation("启用或停用")
    @PostMapping("/disabled")
    public String disabled(@RequestBody Role role) throws Exception{
        Role entity = VerifyRole(role, true);

        if( entity != null ){
            entity.disabled();
            roleService.update(entity);
        }
        return OPER_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @查询相关
    //------------------------------------------------------------------------------------------------------------------
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
    public PageInfo<T> paged(Role role){
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
    public Boolean verifyUniqueCode(Role role) throws Exception{
        if( role != null ){
            Map conditionMap = MapUtils.toMap(role);
            conditionMap.remove("tenantId");
            conditionMap.remove("roleName");
            return roleService.count(conditionMap) > 0 ? false : true;
        }
        return false;
    }

    @ApiOperation("验证角色名称是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "roleName", value = "角色名称", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify-name")
    public Boolean verifyUniqueName(Role role) throws Exception{
        if( role != null ){
            Map conditionMap = MapUtils.toMap(role);
            conditionMap.remove("roleCode");
            return roleService.count(conditionMap) > 0 ? false : true;
        }
        return false;
    }

    /**
     * 内部方法：后端验证角色名称和角色编码
     * @param role 角色对象
     * @param onlyVerifySys 是否只验证是否是内置角色
     * @return
     */
    private Role VerifyRole(Role role, boolean onlyVerifySys) throws Exception{
        if( !onlyVerifySys ){
            if( !verifyUniqueName(role) ){
                throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色名称");
            }else if( !verifyUniqueCode(role) ){
                throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色编码");
            }
        }else if( StringUtils.isNotBlank(role.getId()) ){
            role = roleService.fetch(role.getId());
        }
        if( CommonConstant.YES.equals(role.getIsSys()) && !role.getCurrentUser().isSuperAdmin() ){
            throw new BusinessException(LoginFailEnum.UNDECLARED_ERROR.getCode(), "抱歉，您没有权限操作内置角色");
        }
        return role;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @其他
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取菜单授权的树结构
     * @return
     */
    @ApiOperation("获取菜单授权的树结构")
    @GetMapping("/get-grant-permission")
    public List grantTree(){
        List<Permission> treeData = permissionService.findGrantTreeData();
        return new PermissionTree(treeData).getTreeList();
    }

    /**
     * 获取已授权的菜单权限
     * @return
     */
    @ApiOperation("获取已授权的菜单权限")
    @GetMapping("/get-granted-permission")
    public List getGrantedPermision(String roleId){
        List<Permission> permissionList = permissionService.findByRoleId(roleId);
        return ListUtils.extractToList(permissionList, "id");
    }

    /**
     * 授权菜单权限
     * @param role
     * @return
     */
    @ApiOperation("授权菜单权限")
    @PostMapping("/granted-permission")
    public String grantedPermision(@RequestBody Role role) throws Exception{
        VerifyRole(role, true);
        roleService.grantPermission(role);
        return "菜单授权成功";
    }
}