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
import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.constant.CommonConstant;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

/**
 * 用户表实体控制器层类(User)
 * @author 高云
 * @since 2018-12-11
 * @version v1.0
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "1004.用户管理")
public class UserController extends BaseController{
   @Autowired
   private UserService userService;
   
    /**
     * 新增
     * @param user
     * @return
     */
    @ApiOperation("新增")
    @PostMapping("/created")
    public String created(@Valid User user){
        userService.insert(user);
        return user.getId();
    }
    
    /**
     * 修改
     * @param user
     * @return
     */
    @ApiOperation("修改")
    @PostMapping("/updated")
    @CacheEvict(key = "#user.username")
    public String updated(User user){
        User vo = userService.fetch(user.getId());
        BeanUtils.copyProperties(user, vo, ObjectUtils.getNullPropertyNames(user));

        userService.update(vo);
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
    public String deleted(String ids){
        userService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DEL_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @查询相关
    //------------------------------------------------------------------------------------------------------------------
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
        @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
        @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number")
    })
    public PageInfo<T> paged(User user){
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
        user.setPassword(null);
        user.setMgrType(CommonConstant.USER_TYPE_ADMIN);
        userService.insert(user);
        return user.getId();
    }

    /**
     * 编辑
     * @param user
     * @return
     */
    @ApiOperation("编辑（系统管理员）")
    @PostMapping("/mgr-updated")
    public String mgrUpdated(@RequestBody User user){
        if( !verifyUnique(user.getId(), user.getUsername()) ){
            throw new BusinessException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "系统已存在相同的机构名称");
        }

        // 编辑时不允许修改密码
        user.setPassword(null);
        User vo = userService.fetch(user.getId());
        BeanUtils.copyProperties(user, vo, ObjectUtils.getNullPropertyNames(user));

        userService.update(vo);
        return UPDATE_SUCCEED;
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
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query",dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number")
    })
    public PageInfo<T> mgrPaged(User user){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("mgrType", CommonConstant.USER_TYPE_ADMIN);
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
        User user = userService.fetch(id);

        if( user != null ){
            user.statusSwitch();
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
    @PostMapping("/ mgr-reseted")
    public String mgrReseted(@RequestBody String id){
        User user = userService.fetch(id);

        if( user != null ){
            user.setPassword(null);
            userService.update(user);
        }
        return OPER_SUCCEED;
    }

    /**
     * 唯一性校验
     * @return
     */
    @ApiOperation("验证登录账号是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query",dataType = "String", required = false),
            @ApiImplicitParam(name = "username", value = "登录账号", paramType = "query", dataType = "String", required = true)
    })
    @GetMapping("/verify")
    public Boolean verifyUnique(String id, String username){
        Map conditionMap = Maps.newHashMap();
        if( StringUtils.isNotBlank(id) ){
            conditionMap.put("id", id);
        }
        conditionMap.put("username", username);
        return userService.count(conditionMap) > 0 ? false : true;
    }
}