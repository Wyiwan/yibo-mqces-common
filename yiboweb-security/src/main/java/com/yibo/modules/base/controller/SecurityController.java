/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：SecurityController.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.base.controller;

import com.yibo.core.web.exception.BizException;
import com.yibo.security.exception.LoginFailEnum;
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
        throw new BizException(LoginFailEnum.NOT_LOGGED_ERROR.getCode(), LoginFailEnum.NOT_LOGGED_ERROR.getDesc());
    }
}
