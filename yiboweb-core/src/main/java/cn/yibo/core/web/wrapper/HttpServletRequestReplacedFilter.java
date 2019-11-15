/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：工具类模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2019-01-10  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的core包
{*****************************************************************************
*/

package cn.yibo.core.web.wrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 描述: 一句话描述该类的用途
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2019-01-10
 * 版本: v1.0
 */
public class HttpServletRequestReplacedFilter implements Filter {
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        ServletRequest requestWrapper = null;
        if( request instanceof HttpServletRequest ){
            requestWrapper = new HttpServletRequestBodyWrapper((HttpServletRequest) request);
        }

        // 在chain.doFiler方法中传递新的request对象
        if( requestWrapper == null ){
            chain.doFilter(request, response);
        }else{
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
