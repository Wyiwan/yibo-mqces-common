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
import cn.yibo.base.controller.CrudController;
import cn.yibo.base.entity.TreeBuild;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BizException;
import com.yibo.modules.base.config.annotation.IgnoredLog;
import com.yibo.modules.base.entity.DictKind;
import com.yibo.modules.base.service.DictKindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class DictKindController extends CrudController<DictKindService, DictKind>{
    /**
     * 保存方法内部调用：验证数据合法性
     * @param entity
     * @throws Exception
     */
    @Override
    public void verifyUnique(DictKind entity) throws Exception{
        Map conditionMap = MapUtil.newHashMap();
        if( entity != null ){
            conditionMap.put("id", entity.getId());
            conditionMap.put("dictName", entity.getDictName());

            if( !this.verifyUnique(conditionMap) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在字典名称");
            }else{
                conditionMap.remove("dictName");
                conditionMap.put("dictKind", entity.getDictKind());

                if( !this.verifyUnique(conditionMap) ){
                    throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在字典类型");
                }
            }
        }
    }

    /**
     * 树结构查询
     * @return
     */
    @IgnoredLog
    @ApiOperation("树结构查询")
    @GetMapping("/tree")
    public List tree(DictKind dictKind){
        List list = this.baseSevice.queryTree(dictKind);
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
        List list = this.baseSevice.queryList(this.getParamMap(), "dict_sort", null);
        return new TreeBuild(list).getTreeList();
    }
}