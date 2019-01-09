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
package com.yibo.modules.base.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.common.utils.ThreadPoolUtils;
import cn.yibo.common.web.UserAgentUtils;
import cn.yibo.core.protocol.ResponseT;
import cn.yibo.core.protocol.ReturnCodeEnum;
import cn.yibo.core.web.context.SpringContextHolder;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.dao.LogDao;
import com.yibo.modules.base.entity.Log;
import com.yibo.modules.base.entity.User;
import com.yibo.modules.base.service.PermissionService;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.mapping.SqlCommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志记录工具类
 * @author 高云
 * @since 2019-01-03
 * @version v1.0
 */
public class LogUtils {
    private static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public LogUtils(){
    }

    /**
     *  保存日志
     * @param user
     * @param request
     * @param logTitle
     * @param logType
     */
    public static void saveLog(User user, HttpServletRequest request, String logTitle, String logType){
        saveLog(user, request, (Object)null, (Exception)null, logTitle, logType, 0L);
    }

    /**
     *  保存日志
     * @param user
     * @param request
     * @param handler
     * @param ex
     * @param logTitle
     * @param logType
     * @param executeTime
     */
    public static void saveLog(User user, HttpServletRequest request, Object handler, Exception ex, String logTitle, String logType, long executeTime){
        if( user == null || StrUtil.isEmpty(user.getUsername()) || request == null ){
            return;
        }

        Log log = new Log();
        log.setLogTitle(logTitle);
        log.setLogType(logType);

        // IP信息
        log.setServerAddr(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
        log.setRemoteAddr(ServletUtil.getClientIP(request));

        // 浏览器信息
        UserAgent userAgent = UserAgentUtils.getUserAgent(request);
        log.setDeviceName(userAgent.getOperatingSystem().getName());
        log.setBrowserName(userAgent.getBrowser().getName());
        log.setUserAgent(request.getHeader("User-Agent"));

        // 请求信息
        log.setRequestUri(StrUtil.brief(request.getRequestURI(), 255));
        log.setRequestParams(request.getParameterMap());
        log.setRequestMethod(request.getMethod());

        // 用户信息
        log.setExecuteTime(executeTime);
        log.setCurrentUser(user);
        log.setCreateByName(user.getUsername());
        log.setTenantId(user.getTenantId());
        log.preInsert();

        ThreadPoolUtils.getPool().execute(new SaveLogThread(request, log, handler));
    }

    public static class SaveLogThread extends Thread{
        private Log log;
        private HttpServletRequest request;
        private Object handler;

        public SaveLogThread(HttpServletRequest request, Log log, Object handler){
            this.log = log;
            this.request = request;
            this.handler = handler;
        }

        public void run(){
            LogDao logDao = SpringContextHolder.getBean(LogDao.class);
            PermissionService permissionService = SpringContextHolder.getBean(PermissionService.class);

            // 操作类型
            log.setSqlCommand(true);
            if( StrUtil.isBlank(log.getLogType()) ){
                String sqlCommandTypes = "," + ObjectUtils.toString(request.getAttribute(SqlCommandType.class.getName())) + ",";

                if( StrUtil.containsAny(sqlCommandTypes, ",INSERT,") ){
                    log.setLogType(Log.TYPE_INSERT);
                }else if(StrUtil.containsAny(sqlCommandTypes, ",UPDATE,")) {
                    if( StrUtil.containsIgnoreCase(log.getRequestUri(), "deleted") ){
                        log.setLogType(Log.TYPE_DELETE);
                    }else{
                        log.setLogType(Log.TYPE_UPDATE);
                    }
                }else if(StrUtil.containsAny(sqlCommandTypes, ",DELETE,")) {
                    log.setLogType(Log.TYPE_DELETE);
                }else if(StrUtil.containsAny(sqlCommandTypes, ",SELECT,")) {
                    log.setLogType(Log.TYPE_SELECT);
                }else{
                    log.setLogType(Log.TYPE_ACCESS);
                    log.setSqlCommand(false);
                }
            }

            // 异常信息
            Object objectT = request.getAttribute("responseT");
            if( objectT != null && objectT instanceof ResponseT){
                ResponseT responseT = (ResponseT)objectT;

                if( !ReturnCodeEnum.SUCCESS.getCode().equals(responseT.getRetcode()) ){
                    log.setLogType(Log.TYPE_ERROR);
                    log.setIsException(CommonConstant.YES);
                    log.setRequestUri(responseT.getUri());

                    String exception = StrUtil.emptyToDefault(responseT.getException(), responseT.getMessage());
                    log.setExceptionInfo(exception);
                }
            }

            // 日志标题
            if( StrUtil.isBlank(log.getLogTitle()) ){
                this.log.setLogTitle(permissionService.getMenuNamePath(log.getRequestUri()));
            }
            if( "false".equals(log.getLogTitle()) ){
                if( !log.isSqlCommand() ) {
                    return;
                }
                if( handler instanceof HandlerMethod ){
                    HandlerMethod hm = (HandlerMethod)handler;
                    ApiOperation apiOperation = hm.getMethodAnnotation(ApiOperation.class);
                    if( apiOperation != null && StrUtil.isNotBlank(apiOperation.value()) ){
                        log.setLogTitle(apiOperation.value());
                    }
                }
            }
            if( StrUtil.isBlank(log.getLogTitle()) || "false".equals(log.getLogTitle()) ){
                this.log.setLogTitle("未知操作");
            }

            // 入库操作
            if( !StrUtil.isBlank(log.getRequestUri()) || !StrUtil.isBlank(log.getExceptionInfo()) ){
                logDao.insert(log);
            }
        }
    }

}