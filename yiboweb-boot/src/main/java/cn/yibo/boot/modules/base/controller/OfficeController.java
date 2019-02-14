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
{  2018-12-14  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.controller;

import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.modules.base.entity.Office;
import cn.yibo.boot.modules.base.service.OfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医疗机构表控制器层
 * @author 高云
 * @since 2018-12-14
 * @version v1.0
 */
@RestController
@RequestMapping("/api/office")
@Api(tags = "9005.机构管理")
public class OfficeController extends CrudController<OfficeService, Office>{
    /**
     * 保存方法内部调用：验证数据合法性
     * @param entity
     * @throws Exception
     */
    @Override
    public void verifyUnique(Office entity) throws Exception {
        super.verifyUnique(entity, "系统已存在机构名称");
    }

    /**
     * 列表查询
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list")
    public List list(Office office){
        return this.baseSevice.queryList(this.getParamMap(), "office_sort", null);
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("启用或停用")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/disabled")
    public String disabled(@RequestBody String id){
        Office office = this.baseSevice.fetch(id);
        this.baseSevice.disabled(office);
        return OPERATE_SUCCEED;
    }

    /**
     * 重写删除
     * @param ids
     * @return
     */
    @IgnoredLog
    @Override
    public String deleted(@RequestBody String ids){
        return "机构信息不允许删除";
    }
}