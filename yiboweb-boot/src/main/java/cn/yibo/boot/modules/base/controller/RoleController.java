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
{  2018-12-20  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.controller.BaseForm;
import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.base.entity.TreeBuild;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.common.exception.LoginFailEnum;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.boot.modules.base.entity.Role;
import cn.yibo.boot.modules.base.service.PermissionService;
import cn.yibo.boot.modules.base.service.RoleService;
import cn.yibo.boot.modules.base.service.UserService;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BizException;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色表控制器层
 * @author 高云
 * @since 2018-12-20
 * @version v1.0
 */
@RestController
@RequestMapping("/api/role")
@Api(tags = "9003.角色管理")
public class RoleController extends CrudController<RoleService, Role> {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    /**
     * 保存方法内部调用：验证数据合法性
     * [角色编码]系统全局唯一
     * [角色名称]在当前租户下唯一
     * @param entity
     * @throws Exception
     */
    @Override
    public void verifyUnique(Role entity) throws Exception {
        Map conditionMap = MapUtil.newHashMap();
        if( entity != null ){
            conditionMap.put("id", entity.getId());
            conditionMap.put("roleCode", entity.getRoleCode());

            if( !this.verifyUnique(conditionMap) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色编码");
            }else{
                conditionMap.remove("roleCode");
                conditionMap.put("roleName", entity.getRoleName());
                conditionMap.put("tenantId", UserContext.getUser().getTenantId());

                if( !this.verifyUnique(conditionMap) ){
                    throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在角色名称");
                }
            }
        }
    }

    /**
     * 重写字段校验接口
     * @param entity
     * @return
     */
    @Override
    public Boolean verifyEntity(Role entity){
        if( StrUtil.isNotEmpty(entity.getRoleCode()) ){
            Map conditionMap = MapUtil.newHashMap();
            conditionMap.put("id", entity.getId());
            conditionMap.put("roleCode", entity.getRoleCode());
            return this.verifyUnique(conditionMap);
        }else{
            return super.verifyEntity(entity);
        }
    }

    /**
     * 验证操作用户身份
     * @param role
     * @throws Exception
     */
    public void verifyUserIdentity(Role role) throws Exception {
        if( role != null && !UserContext.getUser().isSuperAdmin() && CommonConstant.YES.equals(role.getIsSys()) ){
            throw new BizException(LoginFailEnum.UNDECLARED_ERROR.getCode(), "抱歉，您没有权限操作内置角色");
        }
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("启用或停用")
    @PostMapping("/disabled")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", required = true, dataType = "String")
    public String disabled(@RequestBody String id) throws Exception{
        Role role = this.baseSevice.fetch(id);

        if( role != null ){
            verifyUserIdentity(role);
            role.enabled();
            this.baseSevice.updateNull(role);
        }
        return OPERATE_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @菜单授权相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取授权菜单的树结构
     * @return
     */
    @IgnoredLog
    @ApiOperation("角色管理/查询可授权菜单")
    @GetMapping("/get-grant-permission")
    public List findGrantPermission(){
        List<Permission> list = permissionService.findGrantPermission();
        return new TreeBuild(list).getTreeList();
    }

    /**
     * 获取已授权的菜单权限
     * @return
     */
    @IgnoredLog
    @ApiOperation("角色管理/查询已授权菜单ID")
    @GetMapping("/get-granted-permission")
    @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    public List findGrantedPermision(String roleId){
        List<Permission> permissionList = permissionService.findByRoleId(roleId);
        if( !CollUtil.isEmpty(permissionList) ){
            return ListUtils.extractToList(permissionList, "id");
        }
        return ListUtils.newArrayList();
    }

    /**
     * 菜单授权
     * @param role
     * @return
     */
    @ApiOperation("角色管理/菜单授权")
    @PostMapping("/granted-permission")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "permissionIds", value = "菜单ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String grantedPermision(@RequestBody Role role) throws Exception{
        Role fetchRole = this.baseSevice.fetch(role.getId());
        verifyUserIdentity(fetchRole);

        this.baseSevice.grantPermission(role);
        return "菜单授权成功";
    }

    //------------------------------------------------------------------------------------------------------------------
    // @分配用户相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取未授权的用户
     * @return
     */
    @IgnoredLog
    @ApiOperation("角色管理/查询可授权用户")
    @GetMapping("/grant-user-paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    })
    public PageInfo<T> getGrantUser(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("queryType", "grantUser");
        return userService.queryPage(baseForm);
    }

    /**
     * 获取已授权的用户
     * @return
     */
    @IgnoredLog
    @ApiOperation("角色管理/查询已授权用户")
    @GetMapping("/granted-user-paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    })
    public PageInfo<T> getGrantedUser(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("queryType", "grantedUser");
        return userService.queryPage(baseForm);
    }

    /**
     * 用户授权
     * @param role
     * @return
     */
    @ApiOperation("角色管理/用户授权")
    @PostMapping("/granted-user")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "userIds", value = "用户ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String grantedUser(@RequestBody Role role){
        this.baseSevice.grantUser(role);
        return "分配用户成功";
    }

    /**
     * 取消用户授权
     * @param role
     * @return
     */
    @ApiOperation("角色管理/取消用户授权")
    @PostMapping("/un-granted-user")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "userIds", value = "用户ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String unGrantedUser(@RequestBody Role role){
        this.baseSevice.unGrantUser(role);
        return "取消用户成功";
    }
}