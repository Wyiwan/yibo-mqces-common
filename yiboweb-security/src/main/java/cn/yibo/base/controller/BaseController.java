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

package cn.yibo.base.controller;

import cn.yibo.common.web.ServletUtils;
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

    protected String SAVE_SUCCEED   = "保存成功";

    protected String UPDATE_SUCCEED = "修改成功";

    protected String DEL_SUCCEED    = "删除成功";

    protected String OPER_SUCCEED   = "操作成功";

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
