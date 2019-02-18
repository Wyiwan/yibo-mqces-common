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
{  注：本模块代码为底层基础框架封装的系统模块
{*****************************************************************************
*/

package cn.yibo.boot.config.interceptor;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志拦截器配置
 * @author 高云
 * @since 2019-01-03
 * @version v1.0
 */
@Configuration
public class MybatisInterceptorConfig {
    @Bean
    public String myInterceptor(SqlSessionFactory sqlSessionFactory) {
        LogInterceptor logInterceptor = new LogInterceptor();
        sqlSessionFactory.getConfiguration().addInterceptor(logInterceptor);
        return "interceptor";
    }
}
