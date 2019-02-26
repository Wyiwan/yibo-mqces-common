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
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的boot包
{*****************************************************************************
*/

package cn.yibo.boot.base.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.common.base.controller.BaseController;
import cn.yibo.common.base.controller.BaseForm;
import cn.yibo.common.base.entity.BaseEntity;
import cn.yibo.common.base.entity.EmptyEntity;
import cn.yibo.common.base.service.IBaseService;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BizException;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

/**
 *  描述: Crud控制层抽象类
 *  作者: gogo163gao@163.com
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public abstract class CrudController<S extends IBaseService, D extends BaseEntity> extends BaseController {
    @Autowired
    public S baseSevice;

    /**
     * 保存之前调用的方法(validation entity)
     * @param entity
     * @throws Exception
     */
    protected void onBeforeSave(D entity) throws Exception{
        verifyUnique(entity);
    }

    /**
     * 新增
     * @param entity
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    protected Object created(@Valid @RequestBody D entity) throws Exception{
        onBeforeSave(entity);
        baseSevice.save(entity);
        return entity.getId();
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    protected String updated(@Valid @RequestBody D entity) throws Exception{
        onBeforeSave(entity);
        baseSevice.save(entity);
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
    protected String deleted(@RequestBody String ids){
        baseSevice.deleteByIds(Arrays.asList(ids.split(",")));
        return DELETE_SUCCEED;
    }

    /**
     * 单个查询
     * @param id
     * @return
     */
    @IgnoredLog
    @ApiOperation("单个查询")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @GetMapping("/fetched")
    protected BaseEntity fetched(String id){
        BaseEntity vo = baseSevice.fetch(id);
        return vo == null ? new EmptyEntity() : vo;
    }

    /**
     * 分页查询
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "Number")
    })
    protected PageInfo<D> paged(){
        return baseSevice.queryPage(new BaseForm<D>());
    }

    /**
     * 唯一性校验
     * @return
     */
    @IgnoredLog
    @ApiOperation("字段唯一性校验")
    @GetMapping("/verify")
    protected Boolean verifyEntity(D entity){
        Map conditionMap = BeanUtil.beanToMap(entity, false, true);
        return verifyUnique(conditionMap);
    }

    public abstract void verifyUnique(D entity) throws Exception;

    public void verifyUnique(D entity, String message) throws Exception{
        if( entity != null ){
            if( !this.verifyEntity(entity) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), message);
            }
        }
    }

    public Boolean verifyUnique(Map conditionMap){
        return baseSevice.count(conditionMap) > 0 ? false : true;
    }
}
