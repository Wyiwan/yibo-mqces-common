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
{  2019-01-22  高云        新建	
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
import com.github.pagehelper.PageInfo;
import com.yibo.modules.base.config.annotation.IgnoredLog;
import com.yibo.modules.base.entity.DictKind;
import com.yibo.modules.base.entity.DictKindTree;
import com.yibo.modules.base.service.DictKindService;
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
 * 字典类别表控制器层
 * @author 高云
 * @since 2019-01-22
 * @version v1.0
 */
@RestController
@RequestMapping("/api/dict-kind")
@Api(tags = "9007.字典类别管理")
public class DictKindController extends BaseController{
   @Autowired
   private DictKindService dictKindService;
   
    /**
     * 新增
     * @param dictKind
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody DictKind dictKind) throws Exception{
        VerifyDictKind(dictKind);
        dictKindService.save(dictKind);
        return dictKind.getId();
    }
    
    /**
     * 修改
     * @param dictKind
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@Valid @RequestBody DictKind dictKind) throws Exception{
        VerifyDictKind(dictKind);
        dictKindService.save(dictKind);
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
        dictKindService.deleteByIds( Arrays.asList(ids.split(",")) );
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
    public DictKind fetched(String id){
        DictKind vo = dictKindService.fetch(id);
        return vo == null ? new DictKind() : vo;
    }

    /**
     * 查询树结构数据
     * @return
     */
    @IgnoredLog
    @ApiOperation("字典类别树结构查询")
    @GetMapping("/tree")
    public List tree(){
        List result = dictKindService.findTree();
        return new DictKindTree(result).getTreeList();
    }

    /**
     * 列表查询
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list")
    public List list(DictKind dictKind){
        Map<String, Object> conditionMap = new BaseForm<T>().getParameters();
        return dictKindService.queryList(conditionMap);
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
    public PageInfo<T> paged(DictKind dictKind){
        return dictKindService.queryPage(new BaseForm<T>());
    }

    /**
     * 字典名称唯一性校验
     * @return
     */
    @IgnoredLog
    @ApiOperation("验证字典名称是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "dictName", value = "字典名称", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify-name")
    public Boolean verifyUniqueName(String id, String dictName){
        Map conditionMap = MapUtil.newHashMap();
        conditionMap.put("id", id);
        conditionMap.put("dictName", dictName);
        return dictKindService.count(conditionMap) > 0 ? false : true;
    }

    /**
     * 字典类型唯一性校验
     * @return
     */
    @IgnoredLog
    @ApiOperation("验证字典类型是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "dictKind", value = "字典类型", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify-kind")
    public Boolean verifyUniqueKind(String id, String dictKind){
        Map conditionMap = MapUtil.newHashMap();
        conditionMap.put("id", id);
        conditionMap.put("dictKind", dictKind);
        return dictKindService.count(conditionMap) > 0 ? false : true;
    }

    /**
     * 内部方法：验证字典名称、类型是否可用
     * @param dictKind
     * @return
     */
    private void VerifyDictKind(DictKind dictKind) throws Exception{
        if( dictKind != null ){
            if( !verifyUniqueName(dictKind.getId(), dictKind.getDictName()) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在字典名称");
            }else if( !verifyUniqueKind(dictKind.getId(), dictKind.getDictKind()) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在字典类型");
            }
        }
    }
}