/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: WebServletListener.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package cn.yibo.core.listener;

import cn.yibo.common.io.PropertiesUtils;

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
