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
 * 描述: 控制器层（根据实际业务需求情况按需定义）
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@RestController
@RequestMapping("/api/data")
public class TestDataController extends BaseController {
    @Autowired
    TestDataService testDataService;

    //------------------------------------------------------------------------------------------------------------------
    // +新增相关
    //------------------------------------------------------------------------------------------------------------------
    @PostMapping("/add")
    @ApiOperation("add")
    public String add(TestData vo){
        testDataService.insert(vo);
        System.out.println("新增数据的ID：" + vo.getId());
        return SAVE_SUCCEED;
    }

    @PostMapping("/add-map")
    @ApiOperation("add-map")
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

    @PostMapping("/update-null")
    @ApiOperation("update-null")
    public String updateNull(TestData vo){
        testDataService.updateNull(vo);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/update-map")
    @ApiOperation("update-map")
    public String updateMap(){
        Map<String, Object> entityMap = new BaseForm<T>().getParameters();
        entityMap.put("updateDate", new Date());
        testDataService.updateMap(entityMap);
        return UPDATE_SUCCEED;
    }

    @PostMapping("/update-by-condition")
    @ApiOperation("update-by-condition")
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

    @PostMapping("/delete-by-ids")
    @ApiOperation("delete-by-ids")
    public String deleteByIds(String ids) {
        testDataService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DEL_SUCCEED;
    }

    @PostMapping("/delete-by-condition")
    @ApiOperation("delete-by-condition")
    public String deleteByCondition() {
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        testDataService.deleteByCondition(conditionMap);
        return DEL_SUCCEED;
    }

    @PostMapping("/delete-by-property")
    @ApiOperation("delete-by-property")
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

    @GetMapping("/find-one")
    @ApiOperation("find-one")
    public TestData findOne(String property, String value){
        TestData vo = testDataService.findOne(property, value);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/find-list")
    @ApiOperation("find-list")
    public List findList(String property, String value){
        return testDataService.findList(property, value);
    }

    @GetMapping("/find-all")
    @ApiOperation("find-all")
    public List findAll(){
        return testDataService.findAll();
    }

    @GetMapping("/query-one")
    @ApiOperation("query-one")
    public TestData queryOne(){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        TestData vo = testDataService.queryOne(conditionMap);
        return vo == null ? new TestData() : vo;
    }

    @GetMapping("/query-list")
    @ApiOperation("query-list")
    public List queryList(@SecurityUser SecurityUserDetails user){
        System.out.println("--------------> 当前登录用户名--------------> "+ user.getUsername());
        System.out.println("--------------> 当前登录用户ID--------------> "+ user.getId());
        System.out.println("--------------> 当前登录用户拥有角色--------------> "+ user.getRoles());
        System.out.println("--------------> 当前登录用户所属科室--------------> "+ user.getDeptId());
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return testDataService.queryList(conditionMap);
    }

    @GetMapping("/query-page")
    @ApiOperation("query-page")
    public PageInfo<T> queryPage() throws Exception{
        SecurityUserDetails user = UserContext.getUser();
        System.out.println("--------------> 当前登录用户名--------------> "+ user.getUsername());
        System.out.println("--------------> 当前登录用户ID--------------> "+ user.getId());
        System.out.println("--------------> 当前登录用户拥有角色--------------> "+ user.getRoles());
        System.out.println("--------------> 当前登录用户所属科室--------------> "+ user.getDeptId());
        return testDataService.queryPage(new BaseForm<T>());
    }

    @GetMapping("/query-by-sql")
    @ApiOperation("query-by-sql")
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