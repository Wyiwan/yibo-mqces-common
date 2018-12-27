/*
{*****************************************************************************
{  广州医博-基础框架 v1.0													
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.					
{  创建人：  高云
{  审查人：
{  模块：安全控制模块										
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

package com.yibo.modules.test.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import com.github.pagehelper.PageInfo;
import com.yibo.modules.test.entity.TestData;
import com.yibo.modules.test.service.TestDataService;
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
 * 测试数据实体控制器层类(TestData)
 * @author 高云
 * @since 2018-12-12
 * @version v1.0
 */
@RestController
@RequestMapping("/api/data")
@Api(tags = "测试数据接口")
public class TestDataController extends BaseController{
   @Autowired
   private TestDataService testDataService;
   
    /**
     * 新增
     * @param testData
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody TestData testData){
        testDataService.save(testData);
        return testData.getId();
    }
    
    /**
     * 修改
     * @param testData
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@Valid @RequestBody TestData testData){
        testDataService.save(testData);
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
        testDataService.deleteByIds( Arrays.asList(ids.split(",")) );
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
    public TestData fetched(String id){
        TestData vo = testDataService.fetch(id);
        return vo == null ? new TestData() : vo;
    }
    
    /**
     * 模糊查询
     * @return
     */
    @ApiOperation("模糊查询")
    @GetMapping("/matched")
    public List matched(TestData testData){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.like(conditionMap);
    }
    
    /**
     * 列表查询
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list")
    public List list(TestData testData){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.queryList(conditionMap);
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
    public PageInfo<T> paged(TestData testData){
        return testDataService.queryPage(new BaseForm<T>());
    }
    
}