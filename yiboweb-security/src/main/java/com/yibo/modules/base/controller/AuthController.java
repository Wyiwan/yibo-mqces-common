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
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************
*/

package com.yibo.modules.base.controller;

import cn.yibo.core.web.exception.BizException;
import cn.yibo.security.context.UserContext;
import cn.yibo.security.exception.LoginFailEnum;
import cn.yibo.security.jwt.JWTUtil;
import com.yibo.modules.base.entity.Log;
import com.yibo.modules.base.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *  描述: 登录相关控制器
 *  作者：高云
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 退出
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        if( UserContext.getUser() != null ){
            jwtUtil.removeRedisToken( UserContext.getUser().getUsername() );
            LogUtils.saveLog(UserContext.getUser(), request, "系统退出", Log.TYPE_LOGOUT);
        }
        return "成功退出";
    }

    /**
     * 未登陆
     */
    @RequestMapping(value = "/unauth",method = RequestMethod.GET)
    public void unauth(){
        throw new BizException(LoginFailEnum.NOT_LOGGED_ERROR.getCode(), LoginFailEnum.NOT_LOGGED_ERROR.getDesc());
    }

}
