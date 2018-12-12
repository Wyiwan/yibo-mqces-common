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
{  2018-12-03  高云        新建
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.security.constant.CommonConstant;
import cn.yibo.security.context.UserContext;
import com.google.common.collect.Lists;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.PermissionTree;
import com.yibo.modules.base.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单权限表实体控制器层类(Permission)
 * @author 高云
 * @since 2018-12-11
 * @version v1.0
 */
@RestController
@RequestMapping("/api/permission")
@Api(tags = "1001.菜单权限表接口")
public class PermissionController extends BaseController{
   @Autowired
   private PermissionService permissionService;
   
    /**
     * 新增
     * @param permission
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody Permission permission){
        permissionService.insert(permission);
        return permission.getId();
    }
    
    /**
     * 修改
     * @param permission
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@RequestBody Permission permission){
        Permission vo = permissionService.fetch(permission.getId());
        BeanUtils.copyProperties(permission, vo, ObjectUtils.getNullPropertyNames(permission));

        permissionService.update(vo);
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
        permissionService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DEL_SUCCEED;
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
    public Permission fetched(String id){
        Permission vo = permissionService.fetch(id);
        return vo == null ? new Permission() : vo;
    }

    /**
     * 列表查询
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/tree-list")
    public List treeList(Permission permission){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        List result = permissionService.queryList(conditionMap);
        return new PermissionTree(result).getTreeList();
    }

    /**
     * 查询树结构数据
     * @return
     */
    @ApiOperation("查询树结构数据")
    @GetMapping("/tree")
    public List tree(){
        List result = permissionService.findTree();
        return new PermissionTree(result).getTreeList();
    }

    /**
     * 用户菜单权限查询
     * @return
     */
    @ApiOperation("用户菜单查询")
    @GetMapping("/user-menu")
    public List userMenu(){
        List<String> condition = Lists.newArrayList(CommonConstant.PERMISSION_PAGE);
        List<Permission> result = UserContext.getUser().getPermissions()
                .stream().filter((Permission p) -> condition.contains(p.getPermsType())).collect(Collectors.toList());
        return new PermissionTree(result).getTreeList();
    }
}