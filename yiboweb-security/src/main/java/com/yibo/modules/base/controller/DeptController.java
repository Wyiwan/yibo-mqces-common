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

package com.yibo.modules.base.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.common.collect.MapUtils;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import com.yibo.modules.base.entity.Dept;
import com.yibo.modules.base.entity.DeptTree;
import com.yibo.modules.base.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 科室表实体控制器层类(Dept)
 * @author 高云
 * @since 2018-12-12
 * @version v1.0
 */
@RestController
@RequestMapping("/api/dept")
@Api(tags = "1002.科室表接口")
public class DeptController extends BaseController{
   @Autowired
   private DeptService deptService;
   
    /**
     * 新增
     * @param dept
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody Dept dept) throws Exception{
        if( !verifyUnique(dept) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在相同的科室名称");
        }
        deptService.insert(dept);
        return dept.getId();
    }
    
    /**
     * 修改
     * @param dept
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@RequestBody Dept dept) throws Exception{
        if( !verifyUnique(dept) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在相同的科室名称");
        }

        Dept vo = deptService.fetch(dept.getId());
        BeanUtils.copyProperties(dept, vo, ObjectUtils.getNullPropertyNames(dept));
        deptService.update(vo);
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
        deptService.deleteByIds( Arrays.asList(ids.split(",")) );
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
    public Dept fetched(String id){
        Dept vo = deptService.fetch(id);
        return vo == null ? new Dept() : vo;
    }

    /**
     * 查询树结构数据
     * @return
     */
    @ApiOperation("树结构查询")
    @GetMapping("/tree")
    public List tree(Dept dept){
        List result = deptService.findTree(dept);
        return new DeptTree(result).getTreeList();
    }

    /**
     * 查询列表数据
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list-tree")
    public List treeList(Dept dept) throws Exception{
        Map conditionMap = MapUtils.toMap(dept);
        List result = deptService.queryList(conditionMap);
        return new DeptTree(result).getTreeList();
    }

    /**
     * 唯一性校验
     * @return
     */
    @ApiOperation("科室名称校验")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "deptName", value = "科室名称", paramType = "query", dataType = "String")
    })
    @GetMapping("/verify")
    private Boolean verifyUnique(Dept dept) throws Exception{
        if( dept != null ){
            Map conditionMap = MapUtils.toMap(dept);
            return deptService.count(conditionMap) > 0 ? false : true;
        }
        return false;
    }
}