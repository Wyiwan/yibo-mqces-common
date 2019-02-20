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
{  注：本模块代码为底层基础框架封装的系统模块
{*****************************************************************************
*/

package cn.yibo.boot.config.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.SystemUtil;
import cn.yibo.boot.common.annotation.IgnoredLog;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.common.utils.LogUtils;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.common.web.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * 日志拦截器
 * @author 高云
 * @since 2019-01-02
 * @version v1.0
 */
@Slf4j
@Intercepts({
   @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
@Component
public class LogInterceptor extends HandlerInterceptorAdapter implements Interceptor {
    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        long beginTime = System.currentTimeMillis();
        startTimeThreadLocal.set(beginTime);

        if( log.isDebugEnabled() ){
            log.debug("开始计时: {}  URI: {}", DateUtil.date(beginTime).toString("hh:mm:ss.SSS"), request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
        if( modelAndView != null ){
            log.info("ViewName: " + modelAndView.getViewName() + " <<<<<<<<< " + request.getRequestURI() + " >>>>>>>>> " + handler);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
        long beginTime = (Long)startTimeThreadLocal.get();
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - beginTime;

        if( handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            // 异步保存日志
            if( hm.getMethodAnnotation(IgnoredLog.class) == null ){
                LogUtils.saveLog(UserContext.getUser(), request, handler, ex, null, null, executeTime);
            }
        }

        if( log.isDebugEnabled() ){
            RuntimeInfo runtime = SystemUtil.getRuntimeInfo();
            String template = StrUtil.format("计时结束: {}  URI: {}  用时: {}ms  最大内存: {}  已分配内存: {}  已分配内存中的剩余空间: {}  最大可用内存: {}",
                  DateUtil.date(endTime).toString("hh:mm:ss.SSS"), request.getRequestURI(), executeTime,
                  FileUtil.readableFileSize(runtime.getMaxMemory()), FileUtil.readableFileSize(runtime.getTotalMemory()),
                  FileUtil.readableFileSize(runtime.getFreeMemory()), FileUtil.readableFileSize(runtime.getUsableMemory()));
            log.debug(template);
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable{
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        HttpServletRequest request = ServletUtils.getRequest();

        if( request != null && mappedStatement != null ){
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

            if( sqlCommandType != null ) {
                String sqlCommandName = SqlCommandType.class.getName();
                String sqlType = ObjectUtils.toString(request.getAttribute(sqlCommandName));

                if( StrUtil.isNotBlank(sqlType) ){
                    sqlType = (new StringBuilder()).insert(0, sqlType).append(",").toString();
                }
                sqlType = (new StringBuilder()).insert(0, sqlType).append(sqlCommandType.toString()).toString();
                request.setAttribute(sqlCommandName, sqlType);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target){
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties){

    }
}
