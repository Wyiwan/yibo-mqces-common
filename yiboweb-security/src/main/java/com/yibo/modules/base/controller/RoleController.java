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
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.exception.LoginFailEnum;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.entity.Role;
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
   
    /**
     * 新增
     * @param role
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody Role role){
        if( !verifyUnique(role.getId(), role.getRoleCode()) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色编码");
        }
        roleService.insert(role);
        return role.getId();
    }
    
    /**
     * 修改
     * @param role
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@RequestBody Role role){
        if( !verifyUnique(role.getId(), role.getRoleCode()) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色编码");
        }

        Role vo = VerifyRole(role);
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
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/disabled")
    public String disabled(@RequestBody Role role){
        Role entity = VerifyRole(role);

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

    /**
     * 唯一性校验
     * @return
     */
    @ApiOperation("验证角色编码是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify")
    public Boolean verifyUnique(String id, String roleCode){
        Map conditionMap = Maps.newHashMap();
        if( StringUtils.isNotBlank(id) ){
            conditionMap.put("id", id);
        }
        conditionMap.put("roleCode", roleCode);
        return roleService.count(conditionMap) > 0 ? false : true;
    }

    /**
     * 操作内置角色的权限验证
     * @param role
     * @return
     */
    private Role VerifyRole(Role role){
        Role vo = roleService.fetch(role.getId());

        if( CommonConstant.YES.equals(vo.getIsSys()) && !role.getCurrentUser().isSuperAdmin() ){
            throw new BusinessException(LoginFailEnum.UNDECLARED_ERROR.getCode(), "抱歉，您没有权限操作内置角色");
        }
        return vo;
    }
}