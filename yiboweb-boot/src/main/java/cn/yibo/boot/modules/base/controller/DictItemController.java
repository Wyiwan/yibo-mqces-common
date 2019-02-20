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

package cn.yibo.boot.modules.base.controller;

import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.base.entity.TreeBuild;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.modules.base.entity.DictItem;
import cn.yibo.boot.modules.base.service.DictItemService;
import cn.yibo.boot.common.utils.DictUtils;
import cn.yibo.common.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@Api(tags = "9008.字典数据项管理")
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
        List list = this.baseSevice.queryList(this.getParamMap(), "item_sort", null);
        return new TreeBuild(list).getTreeList();
    }

    /**
     * 字典项名称查询
     * @return
     */
    @IgnoredLog
    @ApiOperation("字典项名称查询")
    @GetMapping("/data")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "dictKind", value = "字典类别", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "itemValue", value = "字典项键值", paramType = "query", dataType = "Number")
    })
    public String data(String dictKind, String itemValue, String defaultValue){
        return DictUtils.getItemName(dictKind, itemValue, ObjectUtils.toString(defaultValue));
    }
}