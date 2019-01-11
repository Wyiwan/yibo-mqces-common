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
{  2019-01-02  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本本模块代码为底层基础框架封装的系统模块
{*****************************************************************************
*/

package com.yibo.modules.base.config;

import cn.hutool.core.util.ArrayUtil;
import com.yibo.modules.base.config.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 日志拦截器配置
 * @author 高云
 * @since 2019-01-02
 * @version v1.0
 */
@Component
@ConditionalOnProperty(name={"webapp.log.enabled"}, havingValue="true", matchIfMissing=true)
public class LogMvcConfig implements WebMvcConfigurer {
    @Autowired
    private PathPatternsConfig pathPatternsConfig;

    @Autowired
    LogInterceptor logInterceptor;

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration registration = registry.addInterceptor(logInterceptor);

        if( !ArrayUtil.isEmpty(pathPatternsConfig.getAddPathPatterns()) ){
            registration.addPathPatterns( pathPatternsConfig.getAddPathPatterns() );
        }else{
            registration.addPathPatterns( new String[]{"/**"});
        }

        if( !ArrayUtil.isEmpty(pathPatternsConfig.getExcludePathPatterns()) ){
            registration.excludePathPatterns( pathPatternsConfig.getExcludePathPatterns() );
        }
    }
}
