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

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.base.entity.TreeBuild;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.common.constant.LoginFailEnum;
import cn.yibo.boot.common.utils.PermUtils;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.boot.modules.base.entity.Role;
import cn.yibo.boot.modules.base.service.PermissionService;
import cn.yibo.boot.modules.base.service.RoleService;
import cn.yibo.boot.modules.base.service.UserService;
import cn.yibo.common.base.controller.BaseForm;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BizException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @Autowired
    private PermUtils permUtils;

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
    // @授权菜单相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取可授权菜单权限
     * @return
     */
    @IgnoredLog
    @ApiOperation("获取可授权的菜单权限")
    @GetMapping("/authorizable-permissions")
    public List getAuthorizablePermissions(){
        List<Permission> list = permUtils.getAuthorizablePermissions();
        return new TreeBuild(list).getTreeList();
    }

    /**
     * 获取已授权菜单权限
     * @return
     */
    @IgnoredLog
    @ApiOperation("获取已授权的菜单权限")
    @GetMapping("/authorized-permissions")
    @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    public List getAuthorizedPermissions(String roleId){
        List<Permission> permissionList = permissionService.findByRoleId(roleId);
        if( !ListUtils.isEmpty(permissionList) ){
            return ListUtils.extractToList(permissionList, "id");
        }
        return ListUtils.newArrayList();
    }

    /**
     * 授权菜单权限
     * @param role
     * @return
     */
    @ApiOperation("授权菜单权限")
    @PostMapping("/authorize-permissions")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "permissionIds", value = "菜单ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String authorizePermisions(@RequestBody Role role) throws Exception{
        Role fetchRole = this.baseSevice.fetch(role.getId());
        verifyUserIdentity(fetchRole);

        this.baseSevice.saveAuthorize(role);
        return "授权菜单权限成功";
    }

    //------------------------------------------------------------------------------------------------------------------
    // @授权成员相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取未授权的成员
     * @return
     */
    @IgnoredLog
    @ApiOperation("获取可授权的成员")
    @GetMapping("/authorizable-member")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    })
    public PageInfo<T> getAuthorizableMember(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("authorizeType", "0");
        return userService.queryPage(baseForm);
    }

    /**
     * 获取已授权的成员
     * @return
     */
    @IgnoredLog
    @ApiOperation("获取已授权的成员")
    @GetMapping("/authorized-member")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "String", required = true)
    })
    public PageInfo<T> getAuthorizedMember(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("authorizeType", "1");
        return userService.queryPage(baseForm);
    }

    /**
     * 授权成员
     * @param id
     * @param userIds
     * @return
     */
    @ApiOperation("授权成员")
    @GetMapping("/authorize-member")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "userIds", value = "用户ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String authorizeMember(String id, String userIds){
        List<String> userIdList = Lists.newArrayList();
        if( StrUtil.isNotBlank(userIds) ){
            userIdList = Arrays.asList( userIds.split(",") );
        }
        this.baseSevice.saveMember(id, userIdList);
        return "授权用户成功";
    }
}