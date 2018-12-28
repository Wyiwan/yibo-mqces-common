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

package com.yibo.modules.base.controller;

import cn.yibo.base.controller.BaseController;
import cn.yibo.base.controller.BaseForm;
import cn.yibo.common.collect.ListUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.RoleService;
import com.yibo.modules.base.service.UserService;
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
 * 用户表实体控制器层类(User)
 * @author 高云
 * @since 2018-12-11
 * @version v1.0
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "9002.用户管理")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 新增
     * @param user
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid @RequestBody User user){
        if( !verifyUnique(null, user.getUsername()) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在登录账号");
        }

        user.setPassword(null);
        userService.save(user);
        return user.getId();
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    public String updated(@Valid @RequestBody User user){
        if( !verifyUnique(user.getId(), user.getUsername()) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在登录账号");
        }

        User oldUser = userService.fetch(user.getId());
        user.setPassword(oldUser.getPassword());
        userService.save(user);
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
        userService.deleteByIds(Arrays.asList(ids.split(",")));
        return DEL_SUCCEED;
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
        User user = verifyUser(id, false);

        if( user != null ){
            user.disabled();
            userService.update(user);
        }
        return OPER_SUCCEED;
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @ApiOperation("重置密码")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/reseted")
    public String reseted(@RequestBody String id){
        User user = verifyUser(id, false);

        if (user != null) {
            user.setPassword(null);
            userService.update(user);
        }
        return OPER_SUCCEED;
    }

    /**
     * 单个查询
     * @param id
     * @return
     */
    @ApiOperation("单个查询")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @GetMapping("/fetched")
    public User fetched(String id){
        User vo = userService.fetch(id);
        return vo == null ? new User() : vo;
    }

    /**
     * 分页查询
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number")
    })
    public PageInfo<T> paged(){
        return userService.queryPage(new BaseForm<T>());
    }

    //------------------------------------------------------------------------------------------------------------------
    // @系统管理员相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 新增
     * @param user
     * @return
     */
    @ApiOperation("新增（系统管理员）")
    @PostMapping("/mgr-created")
    public String mgrCreated(@Valid @RequestBody User user){
        user.setMgrType(CommonConstant.USER_MGR_TYPE_ADMIN);
        return created(user);
    }

    /**
     * 编辑
     * @param user
     * @return
     */
    @ApiOperation("编辑（系统管理员）")
    @PostMapping("/mgr-updated")
    public String mgrUpdated(@Valid @RequestBody User user){
        return updated(user);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ApiOperation("删除（系统管理员）")
    @ApiImplicitParam(name = "ids", value = "标识ID(多个以逗号隔开)", paramType = "query", required = true, dataType = "String")
    @PostMapping("/mgr-deleted")
    public String mgrDeleted(@RequestBody String ids){
        return deleted(ids);
    }

    /**
     * 分页查询
     * @return
     */
    @ApiOperation("分页查询（系统管理员）")
    @GetMapping("/mgr-paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number")
    })
    public PageInfo<T> mgrPaged(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("mgrType", CommonConstant.USER_MGR_TYPE_ADMIN);
        return userService.queryPage(baseForm);
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("启用或停用（系统管理员）")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/mgr-disabled")
    public String mgrDisabled(@RequestBody String id){
        User user = verifyUser(id, true);

        if (user != null) {
            user.disabled();
            userService.update(user);
        }
        return OPER_SUCCEED;
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @ApiOperation("重置密码（系统管理员）")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/mgr-reseted")
    public String mgrReseted(@RequestBody String id){
        User user = verifyUser(id, true);

        if (user != null) {
            user.setPassword(null);
            userService.update(user);
        }
        return OPER_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @验证相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 唯一性校验
     * @return
     */
    @ApiOperation("验证登录账号是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", dataType = "String", required = false),
            @ApiImplicitParam(name = "username", value = "登录账号", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify")
    public Boolean verifyUnique(String id, String username){
        Map conditionMap = Maps.newHashMap();
        conditionMap.put("id", id);
        conditionMap.put("username", username);
        return userService.count(conditionMap) > 0 ? false : true;
    }

    /**
     * 验证用户有效性
     * @param id
     * @param isAdmin
     * @return
     */
    private User verifyUser(String id, boolean isAdmin){
        User user = userService.fetch(id);

        if (user != null) {
            String mgrType = user.getMgrType();

            if (isAdmin && CommonConstant.USER_MGR_TYPE_ADMIN.equals(mgrType)) {
                return user;
            } else if (!isAdmin && CommonConstant.USER_MGR_TYPE_NORMAL.equals(mgrType)) {
                return user;
            }
        }
        throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "非法数据");
    }

    //------------------------------------------------------------------------------------------------------------------
    // @角色授权相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取已授权的角色
     * @return
     */
    @ApiOperation("获取已授权的角色")
    @GetMapping("/get-granted-role")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", dataType = "String", required = true)
    })
    public List getGrantedRole(String id){
        List<Role> roleList = roleService.findByUserId(id);
        if( !ListUtils.isEmpty(roleList) ){
            return ListUtils.extractToList(roleList, "id");
        }
        return Lists.newArrayList();
    }

    /**
     * 角色授权
     * @param user
     * @return
     */
    @ApiOperation("角色授权")
    @PostMapping("/granted-role")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "roleIds", value = "角色ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String grantedRole(@RequestBody User user){
        userService.grantRole(user);
        return "角色授权成功";
    }

    //------------------------------------------------------------------------------------------------------------------
    // @其他
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 查询用户菜单权限
     * @return
     */
    @ApiOperation("查询用户登录信息")
    @GetMapping("/login-info")
    public Map<String, Object> loginUser(){
        return userService.loginUser();
    }
}