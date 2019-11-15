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
{  2018-12-12  高云        新建
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.modules.base.entity.Dept;
import cn.yibo.boot.modules.base.service.DeptService;
import cn.yibo.boot.base.entity.TreeBuild;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室表控制器层
 * @author 高云
 * @since 2018-12-12
 * @version v1.0
 */
@RestController
@RequestMapping("/api/dept")
@Api(tags = "9001.科室管理")
public class DeptController extends CrudController<DeptService, Dept>{
    /**
     * 保存方法内部调用：验证数据合法性
     * @param entity
     * @throws Exception
     */
    @Override
    public void verifyUnique(Dept entity) throws Exception {
        super.verifyUnique(entity, "系统已存在科室名称");
    }

    /**
     * 树结构查询(查询租户所属的科室数据)
     * @return
     */
    @IgnoredLog
    @ApiOperation("树结构查询")
    @GetMapping("/tree")
    public List tree(Dept dept){
        List result = this.baseSevice.queryTree(dept);
        return new TreeBuild(result).getTreeList();
    }

    /**
     * 列表查询(查询租户所属的科室数据)
     * @return
     */
    @ApiOperation("列表查询")
    @GetMapping("/list")
    public List treeList(Dept dept) throws Exception{
        List result = this.baseSevice.queryList(BeanUtil.beanToMap(dept));
        return new TreeBuild(result).getTreeList();
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("启用或停用")
    @PostMapping("/disabled")
    @ApiImplicitParam(name = "id", value = "科室ID", paramType = "query", required = true, dataType = "String")
    public String disabled(@RequestBody String id) throws Exception{
        Dept dept = this.baseSevice.fetch(id);

        if( dept != null ){
            dept.enabled();
            this.baseSevice.updateNull(dept);
        }
        return OPERATE_SUCCEED;
    }
}