/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: BaseController.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package cn.yibo.base.controller;

import cn.yibo.common.web.http.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  描述: 控制器抽象类
 *  作者: gogo163gao@163.com
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String SAVE_SUCCEED   = "成功保存";
    protected String UPDATE_SUCCEED = "成功修改";
    protected String DEL_SUCCEED    = "成功删除";

    protected HttpServletRequest getHttpServletRequest() {
        return ServletUtils.getRequest();
    }

    protected HttpServletResponse getHttpServletResponse() {
        return ServletUtils.getResponse();
    }

    protected String getParameter(String name) {
        return ServletUtils.getRequest().getParameter(name);
    }

    protected void setAttribute(String name, Object value) {
        ServletUtils.getRequest().setAttribute(name, value);
    }

}
