/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：安全控制模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package com.yibo.modules.base.controller;

import cn.yibo.core.web.exception.BusinessException;
import cn.yibo.security.exception.LoginFailEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *  描述: 未登录控制器
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@RestController
@RequestMapping("/common")
public class SecurityController {

    @RequestMapping(value = "/unauth",method = RequestMethod.GET)
    public void unauth(){
        throw new BusinessException(LoginFailEnum.NOT_LOGGED_ERROR.getCode(), LoginFailEnum.NOT_LOGGED_ERROR.getDesc());
    }
}
