/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：核心模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-10  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的core包
{*****************************************************************************
*/

package cn.yibo.core.web.listener;

import cn.yibo.common.utils.PropertiesUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *  描述: ServletContext监听器
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-12
 *  版本: v1.0
 */
public class WebServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n    Web应用启动，欢迎使用 "+ PropertiesUtils.getInstance().getProperty("webapp.name")+"  -  Powered By gogo163gao@163.com\r\n");
        System.out.println(sb.toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Web应用销毁...");
    }
}
