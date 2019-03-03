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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.controller.CrudController;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.entity.Role;
import cn.yibo.boot.modules.base.entity.User;
import cn.yibo.boot.modules.base.service.RoleService;
import cn.yibo.boot.modules.base.service.UserService;
import cn.yibo.common.base.controller.BaseForm;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.exception.BizException;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户表控制器层
 * @author 高云
 * @since 2018-12-11
 * @version v1.0
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "9002.用户管理")
public class UserController extends CrudController<UserService, User> {
    @Autowired
    private RoleService roleService;

    /**
     * 重写保存之前调用的方法
     * @param user
     * @throws Exception
     */
    @Override
    public void onBeforeSave(User user) throws Exception{
        super.onBeforeSave(user);
        user.setMgrType(CommonConstant.USER_MGR_TYPE_NORMAL);

        // 如果是编辑
        if( StrUtil.isNotEmpty(user.getId()) ){
            User oldUser = this.baseSevice.fetched(user.getId());
            user.preUpdateInfo(oldUser);
        }
    }

    /**
     * 保存方法内部调用：验证数据合法性
     * @param entity
     * @throws Exception
     */
    @Override
    public void verifyUnique(User entity) throws Exception{
        super.verifyUnique(entity, "系统已存在登录账号");
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
        User user = this.baseSevice.fetched(id);

        if( user != null ){
            user.enabled();
            this.baseSevice.updateNull(user);
        }
        return OPERATE_SUCCEED;
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
        User user = this.baseSevice.fetched(id);

        if( user != null ){
            user.initPassword();
            this.baseSevice.updateNull(user);
        }
        return OPERATE_SUCCEED;
    }

    //------------------------------------------------------------------------------------------------------------------
    // @系统管理员相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 新增
     * @param user
     * @return
     */
    @ApiOperation("/系统管理员/新增")
    @PostMapping("/admin-created")
    public String adminCreated(@Valid @RequestBody User user) throws Exception{
        super.onBeforeSave(user);
        user.setMgrType(CommonConstant.USER_MGR_TYPE_ADMIN);
        this.baseSevice.save(user);
        return user.getId();
    }

    /**
     * 编辑
     * @param user
     * @return
     */
    @ApiOperation("系统管理员/编辑")
    @PostMapping("/admin-updated")
    public String adminUpdated(@Valid @RequestBody User user) throws Exception{
        return super.updated(user);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ApiOperation("系统管理员/删除")
    @ApiImplicitParam(name = "ids", value = "标识ID(多个以逗号隔开)", paramType = "query", required = true, dataType = "String")
    @PostMapping("/admin-deleted")
    public String adminDeleted(@RequestBody String ids){
        return super.deleted(ids);
    }

    /**
     * 分页查询
     * @return
     */
    @ApiOperation("系统管理员/查询")
    @GetMapping("/admin-paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "rows", value = "页大小", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "page", value = "当前页", paramType = "query", dataType = "Number")
    })
    public PageInfo<T> adminPaged(){
        BaseForm<T> baseForm = new BaseForm<T>();
        baseForm.set("mgrType", CommonConstant.USER_MGR_TYPE_ADMIN);
        return this.baseSevice.queryPage(baseForm);
    }

    /**
     * 启用或停用
     * @param id
     * @return
     */
    @ApiOperation("系统管理员/启用或停用")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/admin-disabled")
    public String adminDisabled(@RequestBody String id) throws Exception{
        return disabled(id);
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @ApiOperation("系统管理员/重置密码")
    @ApiImplicitParam(name = "id", value = "标识ID", paramType = "query", required = true, dataType = "String")
    @PostMapping("/admin-reseted")
    public String adminReseted(@RequestBody String id){
        return reseted(id);
    }

    //------------------------------------------------------------------------------------------------------------------
    // @授权角色相关
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取已授权角色
     * @return
     */
    @IgnoredLog
    @ApiOperation("获取已授权的角色")
    @GetMapping("/assigned-roles")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", dataType = "String", required = true)
    })
    public List assignedRoles(String id){
        List<Role> roleList = roleService.findByUserId(id);
        if( !CollUtil.isEmpty(roleList) ){
            return ListUtils.extractToList(roleList, "id");
        }
        return ListUtils.newArrayList();
    }

    /**
     * 授权角色
     * @param user
     * @return
     */
    @ApiOperation("授权角色")
    @PostMapping("/assign-roles")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "roleIds", value = "角色ID以逗号隔开的字符串", paramType = "query", dataType = "String", required = true)
    })
    public String assignRoles(@RequestBody User user){
        this.baseSevice.assignRoles(user);
        return "授权角色成功";
    }

    //------------------------------------------------------------------------------------------------------------------
    // @其他
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 获取登录(个人)信息
     * @return
     */
    @IgnoredLog
    @ApiOperation("获取登录(个人)信息")
    @GetMapping("/{type}-info")
    public Map<String, Object> userInfo(@PathVariable("type") String type){
        return this.baseSevice.userInfo(type);
    }

    /**
     * 修改个人信息
     * @return
     */
    @ApiOperation("修改个人信息")
    @PostMapping("/edit-info")
    public String editUserInfo(@RequestBody User user){
        if( StrUtil.isEmpty(user.getName()) ){
            throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "姓名不能为空");
        }
        this.baseSevice.editUserInfo(user);
        return SAVE_SUCCEED;
    }

    /**
     * 修改个人密码
     * @return
     */
    @ApiOperation("修改个人密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "confirmNewPassword", value = "确认新密码", paramType = "query", dataType = "String", required = true)
    })
    @PostMapping("/edit-pwd")
    public String editUserPwd(@RequestBody JSONObject jsonObject){
        User vo = this.baseSevice.fetch(UserContext.getUser().getId());
        if( vo != null ){
            String oldPassword = jsonObject.getString("oldPassword");
            String newPassword = jsonObject.getString("newPassword");
            String confirmNewPassword = jsonObject.getString("confirmNewPassword");

            if( !new BCryptPasswordEncoder().matches(oldPassword, vo.getPassword()) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "旧密码不正确");
            }else if( StrUtil.isEmptyOrUndefined(newPassword) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "新密码不能为空");
            }else if( !StrUtil.equals(newPassword, confirmNewPassword) ){
                throw new BizException(ReturnCodeEnum.VALIDATE_ERROR.getCode(), "两次密码不一致");
            }else{
                this.baseSevice.editUserPwd(newPassword);
            }
        }
        return UPDATE_SUCCEED;
    }
}