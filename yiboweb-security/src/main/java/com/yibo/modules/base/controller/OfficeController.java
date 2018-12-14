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
{  2018-12-14  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import cn.yibo.common.collect.MapUtils;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import com.yibo.modules.base.entity.Office;
import com.yibo.modules.base.service.OfficeService;
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
 * 医疗机构表实体控制器层类(Office)
 * @author 高云
 * @since 2018-12-14
 * @version v1.0
 */
@RestController
@RequestMapping("/api/office")
@Api(tags = "1003.机构管理")
public class OfficeController extends BaseController{
   @Autowired
   private OfficeService officeService;
   
    /**
     * 新增
     * @param office
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody Office office) throws Exception{
        if( !verifyUnique(office) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在相同的机构名称");
        }

        officeService.insert(office);
        return office.getId();
    }
    
    /**
     * 修改
     * @param office
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@RequestBody Office office) throws Exception{
        if( !verifyUnique(office) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在相同的机构名称");
        }

        Office vo = officeService.fetch(office.getId());
        BeanUtils.copyProperties(office, vo, ObjectUtils.getNullPropertyNames(office));
        officeService.update(vo);
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
        officeService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DEL_SUCCEED;
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("启用或停用")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/disabled")
    public String disabled(String id){
        Office office = officeService.fetch(id);
        if( office != null ){
            office.statusToggle();
            officeService.update(office);
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
    public Office fetched(String id){
        Office vo = officeService.fetch(id);
        return vo == null ? new Office() : vo;
    }
    
    /**
     * 列表查询
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list")
    public List list(Office office){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return officeService.queryList(conditionMap);
    }

    /**
     * 唯一性校验
     * @return
     */
    @ApiOperation("机构名称校验")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "deptName", value = "机构名称", paramType = "query", dataType = "String")
    })
    @GetMapping("/verify")
    private Boolean verifyUnique(Office office) throws Exception{
        if( office != null ){
            Map conditionMap = MapUtils.toMap(office);
            return officeService.count(conditionMap) > 0 ? false : true;
        }
        return false;
    }
    
}