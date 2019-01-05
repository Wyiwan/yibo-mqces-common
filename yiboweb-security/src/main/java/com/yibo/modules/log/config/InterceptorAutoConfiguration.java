/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：日志记录模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2019-01-03  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的log包
{*****************************************************************************
*/

package com.yibo.modules.log.config;

import com.yibo.modules.log.interceptor.LogInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 日志拦截器配置
 * @author 高云
 * @since 2019-01-03
 * @version v1.0
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class InterceptorAutoConfiguration {
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Autowired
    LogInterceptor logInterceptor;

    @PostConstruct
    public void addPageInterceptor(){
        for( SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList ){
            sqlSessionFactory.getConfiguration().addInterceptor(logInterceptor);
        }
    }
}
