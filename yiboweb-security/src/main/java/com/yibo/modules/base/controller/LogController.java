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
{  2019-01-09  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.security.context.UserContext;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yibo.modules.base.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志表实体控制器层类(Log)
 * @author 高云
 * @since 2019-01-09
 * @version v1.0
 */
@RestController
@RequestMapping("/api/log")
@Api(tags = "操作日志表接口")
public class LogController extends BaseController{
   @Autowired
   private LogService logService;
   
    /**
     * 分页查询
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/paged")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query",dataType = "String"),
        @ApiImplicitParam(name = "categoryId", value = "日志类型(1登录日志  2操作日志  3异常日志)", paramType = "query",dataType = "String"),
        @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query",dataType = "String"),
        @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query",dataType = "String"),
        @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number")
    })
    public PageInfo<T> paged(){
        return logService.queryPage(new BaseForm<T>());
    }

    /**
     * 删除
     * @param jsonObject
     * @return
     */
    @ApiOperation("删除")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "categoryId", value = "日志类型(1登录日志 2操作日志 3异常日志)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "keepTime", value = "保留时间(7保留一周 1保留一个月 3保留一个月)", paramType = "query",dataType = "String")
    })
    @PostMapping(value = "/deleted")
    public String deleted(@RequestBody JSONObject jsonObject){
        if( ObjectUtils.isEmpty(jsonObject.get("categoryId")) ){
            jsonObject.put("categoryId", 1);
        }
        if( ObjectUtils.isEmpty(jsonObject.get("keepTime")) ){
            jsonObject.put("keepTime", 3);
        }
        jsonObject.put("tenantId", UserContext.getUser().getTenantId());

        logService.deleteByCondition(jsonObject);
        return DEL_SUCCEED;
    }

}