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
{  2018-12-03  高云        新建
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.controller;

import cn.hutool.core.map.MapUtil;
import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.boot.modules.base.service.PermissionService;
import cn.yibo.common.base.entity.TreeBuild;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BizException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单权限表控制器层
 * @author 高云
 * @since 2018-12-11
 * @version v1.0
 */
@RestController
@RequestMapping("/api/permission")
@Api(tags = "9004.菜单管理")
public class PermissionController extends CrudController<PermissionService, Permission>{
    /**
     * 保存方法内部调用：验证数据合法性
     * @param entity
     * @throws Exception
     */
    @Override
    public void verifyUnique(Permission entity) throws Exception {
        Map conditionMap = MapUtil.newHashMap();
        if( entity != null ){
            conditionMap.put("id", entity.getId());
            conditionMap.put("permsName", entity.getPermsName());

            if( !this.verifyUnique(conditionMap) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在菜单名称");
            }else{
                conditionMap.remove("permsName");
                conditionMap.put("permsUrl", entity.getPermsUrl());

                if( !this.verifyUnique(conditionMap) ){
                    throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在路径地址");
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
    public List tree(Permission permission){
        List list = this.baseSevice.queryTree(permission);
        return new TreeBuild(list).getTreeList();
    }

    /**
     * 列表查询
     * @return
     */
    @ApiOperation("列表查询")
    @ApiImplicitParam(name = "permsName", value = "菜单名称", paramType = "query", required = true, dataType = "String")
    @GetMapping("/list")
    public List treeList(){
        List list = this.baseSevice.queryList(this.getParamMap(), "perms_sort", null);
        return new TreeBuild(list).getTreeList();
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("启用或停用")
    @PostMapping("/disabled")
    @ApiImplicitParam(name = "id", value = "菜单ID", paramType = "query", required = true, dataType = "String")
    public String disabled(@RequestBody String id) throws Exception{
        Permission permission = this.baseSevice.fetch(id);

        if( permission != null ){
            permission.enabled();
            this.baseSevice.updateNull(permission);
        }
        return OPERATE_SUCCEED;
    }
}