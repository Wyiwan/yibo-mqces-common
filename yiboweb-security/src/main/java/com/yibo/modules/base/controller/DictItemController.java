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
{  2019-01-25  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.controller;

import cn.yibo.base.controller.CrudController;
import cn.yibo.base.entity.TreeBuild;
import com.yibo.modules.base.config.annotation.IgnoredLog;
import com.yibo.modules.base.entity.DictItem;
import com.yibo.modules.base.service.DictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典数据项表控制器层
 * @author 高云
 * @since 2019-01-25
 * @version v1.0
 */
@RestController
@RequestMapping("/api/dict-item")
@Api(tags = "字典数据项表接口")
public class DictItemController extends CrudController<DictItemService, DictItem>{
    /**
     * 保存方法内部调用：验证数据合法性
     * @param entity
     * @throws Exception
     */
    @Override
    public void verifyUnique(DictItem entity) throws Exception{
        super.verifyUnique(entity, "系统已存在字典名称");
    }

    /**
     * 树结构查询
     * @return
     */
    @IgnoredLog
    @ApiOperation("树结构查询")
    @GetMapping("/tree")
    public List tree(DictItem dictItem){
        List list = this.baseSevice.queryTree(dictItem);
        return new TreeBuild(list).getTreeList();
    }

    /**
     * 列表查询
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list")
    @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query",dataType = "String")
    public List treeList(){
        return this.baseSevice.queryList(this.getParamMap(), "item_sort", null);
    }
}