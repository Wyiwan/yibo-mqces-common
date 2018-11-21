/*
{*****************************************************************************
{  基础框架 v1.0.4
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：测试模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-11-13  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************
*/

package com.yibo.modules.test.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.security.SecurityUserDetails;
import cn.yibo.security.annotation.SecurityUser;
import cn.yibo.security.context.UserContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yibo.modules.test.entity.TestData;
import com.yibo.modules.test.service.TestDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述: 控制器层（根据实际业务需求情况按需定义）
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@RestController
@RequestMapping("/api/data")
@Api(tags = "测试数据接口")
public class TestDataController extends BaseController {
    @Autowired
    TestDataService testDataService;

    //------------------------------------------------------------------------------------------------------------------
    // +新增相关
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/add")
    @ApiOperation("add")
    public String add(@Valid TestData vo){
        testDataService.insert(vo);
        System.out.println("新增数据的ID：" + vo.getId());
        return SAVE_SUCCEED;
    }

    @PostMapping("/addMap")
    @ApiOperation("addMap")
    public String addMap(){
        Map<String, Object> entityMap = new BaseForm<T>().getParameters();
        entityMap.put("createDate", new Date());
        entityMap.put("updateDate", new Date());

        testDataService.insertMap(entityMap);
        return SAVE_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // ※修改相关
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/update")
    @ApiOperation("update")
    public String update(TestData vo){
        // 先查询再覆盖不为为空的属性值
        TestData entity = testDataService.fetch(vo.getId());
        BeanUtils.copyProperties(vo, entity, ObjectUtils.getNullPropertyNames(vo));

        testDataService.update(entity);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/updateNull")
    @ApiOperation("updateNull")
    public String updateNull(@Valid TestData vo){
        testDataService.updateNull(vo);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/updateMap")
    @ApiOperation("updateMap")
    public String updateMap(){
        Map<String, Object> entityMap = new BaseForm<T>().getParameters();
        entityMap.put("updateDate", new Date());
        testDataService.updateMap(entityMap);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/updateByCondition")
    @ApiOperation("updateByCondition")
    public String updateByCondition(){
        Map<String, Object> entityMap = new BaseForm<T>().getParameters();
        entityMap.put("updateDate", new Date());

        Map<String, Object> conditionMap = Maps.newHashMap();
        conditionMap.put("name", "张三");
        testDataService.updateByCondition(entityMap, conditionMap);
        return UPDATE_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // -删除相关
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/delete")
    @ApiOperation("delete")
    public String delete(String id) {
        testDataService.deleteById(id);
        return DEL_SUCCEED;
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("deleteByIds")
    public String deleteByIds(String ids) {
        testDataService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DEL_SUCCEED;
    }

    @PostMapping("/deleteByCondition")
    @ApiOperation("deleteByCondition")
    public String deleteByCondition() {
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        testDataService.deleteByCondition(conditionMap);
        return DEL_SUCCEED;
    }

    @PostMapping("/deleteByProperty")
    @ApiOperation("deleteByProperty")
    public String deleteByProperty(String property, String value) {
        testDataService.deleteByProperty(property, value);
        return DEL_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @查询相关
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("/fetch")
    @ApiOperation("fetch")
    public TestData fetch(String id){
        TestData vo = testDataService.fetch(id);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/findOne")
    @ApiOperation("findOne")
    public TestData findOne(String property, String value){
        TestData vo = testDataService.findOne(property, value);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/findList")
    @ApiOperation("findList")
    public List findList(String property, String value){
        return testDataService.findList(property, value);
    }

    @GetMapping("/findAll")
    @ApiOperation("findAll")
    public List findAll(){
        return testDataService.findAll();
    }

    @GetMapping("/queryOne")
    @ApiOperation("queryOne")
    public TestData queryOne(){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        TestData vo = testDataService.queryOne(conditionMap);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/queryList")
    @ApiOperation("queryList")
    public List queryList(@SecurityUser SecurityUserDetails user){
        System.out.println("--------------> 当前登录用户名--------------> "+ user.getUsername());
        System.out.println("--------------> 当前登录用户ID--------------> "+ user.getId());
        System.out.println("--------------> 当前登录用户拥有角色--------------> "+ user.getRoles());
        System.out.println("--------------> 当前登录用户所属科室--------------> "+ user.getDeptId());
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.queryList(conditionMap);
    }

    @GetMapping("/queryPage")
    @ApiOperation("queryPage")
    public PageInfo<T> queryPage() throws Exception{
        SecurityUserDetails user = UserContext.getUser();
        System.out.println("--------------> 当前登录用户名--------------> "+ user.getUsername());
        System.out.println("--------------> 当前登录用户ID--------------> "+ user.getId());
        System.out.println("--------------> 当前登录用户拥有角色--------------> "+ user.getRoles());
        System.out.println("--------------> 当前登录用户所属科室--------------> "+ user.getDeptId());
        return testDataService.queryPage(new BaseForm<T>());
    }

    @GetMapping("/queryBySql")
    @ApiOperation("queryBySql")
    public List queryBySql() throws Exception{
        return testDataService.queryBySql("select * from test_data");
    }

    @GetMapping("/like")
    @ApiOperation("like")
    public List like() throws Exception{
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.like(conditionMap);
    }

    @GetMapping("/count")
    @ApiOperation("count")
    public int count() throws Exception{
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.count(conditionMap);
    }

}