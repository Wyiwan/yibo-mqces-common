/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: TestDataController.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.modules.test.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yibo.base.controller.BaseController;
import com.yibo.base.controller.BaseForm;
import com.yibo.common.lang.ObjectUtils;
import com.yibo.modules.test.entity.TestData;
import com.yibo.modules.test.service.ITestDataService;
import com.yibo.security.SecurityUserDetails;
import com.yibo.security.annotation.SecurityUser;
import com.yibo.security.context.UserContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述: 控制器层实现案例，实际情况根据业务需求定义相关的方法即可
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@RestController
@RequestMapping("/test")
public class TestDataController extends BaseController {
    @Autowired
    ITestDataService testDataService;

    //------------------------------------------------------------------------------------------------------------------
    // +新增相关（注意：返回ResponseMap类型数据，会跳过ResponseT的格式包装）
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/add")
    public String add(TestData vo){
        testDataService.insert(vo);
        System.out.println("新增数据的ID：" + vo.getId());
        return SAVE_SUCCEED;
    }

    @PostMapping("/addMap")
    public String addMap(){
        Map<String, Object> entityMap = new BaseForm<T>().getParameters();
        entityMap.put("createDate", new Date());
        entityMap.put("updateDate", new Date());

        testDataService.insertMap(entityMap);
        return SAVE_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // ※修改相关（注意：返回ResponseMap类型数据，会跳过ResponseT的格式包装）
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/update")
    public String update(TestData vo){
        // 先查询再覆盖不为为空的属性值
        TestData entity = testDataService.fetch(vo.getId());
        BeanUtils.copyProperties(vo, entity, ObjectUtils.getNullPropertyNames(vo));

        testDataService.update(entity);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/updateNull")
    public String updateNull(TestData vo){
        testDataService.updateNull(vo);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/updateMap")
    public String updateMap(){
        Map<String, Object> entityMap = new BaseForm<T>().getParameters();
        entityMap.put("updateDate", new Date());
        testDataService.updateMap(entityMap);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/updateByCondition")
    public String updateByCondition(){
        Map<String, Object> entityMap = new BaseForm<T>().getParameters();
        entityMap.put("updateDate", new Date());

        Map<String, Object> conditionMap = Maps.newHashMap();
        conditionMap.put("name", "张三");
        testDataService.updateByCondition(entityMap, conditionMap);
        return UPDATE_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // -删除相关（注意：返回ResponseMap类型数据，会跳过ResponseT的格式包装）
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/deleteById")
    public String deleteById(String id) {
        testDataService.deleteById(id);
        return DEL_SUCCEED;
    }

    @PostMapping("/deleteByIds")
    public String deleteByIds(String ids) {
        testDataService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DEL_SUCCEED;
    }

    @PostMapping("/deleteByCondition")
    public String deleteByCondition() {
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        testDataService.deleteByCondition(conditionMap);
        return DEL_SUCCEED;
    }

    @PostMapping("/deleteByProperty")
    public String deleteByProperty(String property, String value) {
        testDataService.deleteByProperty(property, value);
        return DEL_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @查询相关（注意：返回ResponseMap类型数据，会跳过ResponseT的格式包装）
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/fetch")
    @ApiOperation("fetch接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "数据Id", paramType = "query", required = true, dataType = "String")
    })
    public TestData fetch(String id){
        TestData vo = testDataService.fetch(id);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/findOne")
    @ApiOperation("findOne接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "property", value = "查询的属性名称", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "value", value = "查询的属性值", paramType = "query", required = true, dataType = "String")
    })
    public TestData findOne(String property, String value){
        TestData vo = testDataService.findOne(property, value);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/findList")
    @ApiOperation("findList接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "property", value = "查询的属性名称", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "value", value = "查询的属性值", paramType = "query", required = true, dataType = "String")
    })
    public List findList(String property, String value){
        return testDataService.findList(property, value);
    }

    @GetMapping("/findAll")
    @ApiOperation("findAll接口")
    @ApiImplicitParams(value = {})
    public List findAll(){
        return testDataService.findAll();
    }

    @GetMapping("/queryOne")
    @ApiOperation("queryOne接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "age", value = "年龄", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "shortName", value = "简称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "createBy", value = "创建人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createDate", value = "创建时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "updateBy", value = "修改人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "updateDate", value = "修改时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "comments", value = "备注", paramType = "query", dataType = "String")
    })
    public TestData queryOne(){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        TestData vo = testDataService.queryOne(conditionMap);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/queryList")
    @ApiOperation("queryList接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "age", value = "年龄", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "shortName", value = "简称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "createBy", value = "创建人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createDate", value = "创建时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "updateBy", value = "修改人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "updateDate", value = "修改时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "comments", value = "备注", paramType = "query", dataType = "String")
    })
    public List queryList(@SecurityUser SecurityUserDetails user){
        System.out.println("--------------> 当前登录用户名--------------> "+ user.getUsername());
        System.out.println("--------------> 当前登录用户ID--------------> "+ user.getId());
        System.out.println("--------------> 当前登录用户拥有角色--------------> "+ user.getRoles());
        System.out.println("--------------> 当前登录用户所属科室--------------> "+ user.getDeptId());
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.queryList(conditionMap);
    }

    @GetMapping("/queryPage")
    @ApiOperation("queryPage接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "id", value = "ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "age", value = "年龄", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "shortName", value = "简称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "createBy", value = "创建人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createDate", value = "创建时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "updateBy", value = "修改人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "updateDate", value = "修改时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "comments", value = "备注", paramType = "query", dataType = "String")
    })
    public PageInfo<T> queryPage() throws Exception{
        SecurityUserDetails user = UserContext.getUser();
        System.out.println("--------------> 当前登录用户名--------------> "+ user.getUsername());
        System.out.println("--------------> 当前登录用户ID--------------> "+ user.getId());
        System.out.println("--------------> 当前登录用户拥有角色--------------> "+ user.getRoles());
        System.out.println("--------------> 当前登录用户所属科室--------------> "+ user.getDeptId());
        return testDataService.queryPage(new BaseForm<T>());
    }

    @GetMapping("/queryBySql")
    public List queryBySql() throws Exception{
        return testDataService.queryBySql("select * from test_data");
    }

    @GetMapping("/like")
    public List like() throws Exception{
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.like(conditionMap);
    }

    @GetMapping("/count")
    public int count() throws Exception{
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.count(conditionMap);
    }

    @GetMapping("/selectMaxId")
    public Object selectMaxId() throws Exception{
        return testDataService.selectMaxId();
    }

}