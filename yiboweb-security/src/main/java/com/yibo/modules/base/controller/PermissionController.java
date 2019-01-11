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

import cn.hutool.core.map.MapUtil;
import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BizException;
import com.yibo.modules.base.config.annotation.IgnoredLog;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.PermissionTree;
import com.yibo.modules.base.service.PermissionService;
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
 * 菜单权限表实体控制器层类(Permission)
 * @author 高云
 * @since 2018-12-11
 * @version v1.0
 */
@RestController
@RequestMapping("/api/permission")
@Api(tags = "9004.菜单管理")
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
    public String created(@Valid @RequestBody Permission permission) throws Exception{
        VerifyPermission(permission);
        permissionService.save(permission);
        return permission.getId();
    }
    
    /**
     * 修改
     * @param permission
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@Valid @RequestBody Permission permission) throws Exception{
        VerifyPermission(permission);
        permissionService.save(permission);
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
     * 查询树结构数据
     * @return
     */
    @IgnoredLog
    @ApiOperation("菜单树查询")
    @GetMapping("/tree")
    public List tree(){
        List result = permissionService.findTree();
        return new PermissionTree(result).getTreeList();
    }

    /**
     * 查询列表数据
     * @return
     */
    @ApiOperation("列表查询")
    @ApiImplicitParam(name = "permsName", value = "菜单名称", paramType = "query", required = true, dataType = "String")
    @GetMapping("/list")
    public List treeList(){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        List result = permissionService.queryList(conditionMap, "perms_sort", null);
        return new PermissionTree(result).getTreeList();
    }

    /**
     * 菜单名称唯一性校验
     * @return
     */
    @IgnoredLog
    @ApiOperation("验证菜单名称是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "permsName", value = "菜单名称", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify-name")
    public Boolean verifyUniqueName(String id, String permsName){
        Map conditionMap = MapUtil.newHashMap();
        conditionMap.put("id", id);
        conditionMap.put("permsName", permsName);
        return permissionService.count(conditionMap) > 0 ? false : true;
    }

    /**
     * 路径地址唯一性校验
     * @return
     */
    @IgnoredLog
    @ApiOperation("验证路径地址是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "permsUrl", value = "路径地址", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify-url")
    public Boolean verifyUniqueUrl(String id, String permsUrl){
        Map conditionMap = MapUtil.newHashMap();
        conditionMap.put("id", id);
        conditionMap.put("permsUrl", permsUrl);
        return permissionService.count(conditionMap) > 0 ? false : true;
    }

    /**
     * 内部方法：验证权限名称、地址是否可用
     * @param permission
     * @return
     */
    private void VerifyPermission(Permission permission) throws Exception{
        if( permission != null ){
            if( !verifyUniqueName(permission.getId(), permission.getPermsName()) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在菜单名称");
            }else if( !verifyUniqueUrl(permission.getId(), permission.getPermsUrl()) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在路径地址");
            }
        }
    }

}