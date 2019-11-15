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

package cn.yibo.core.web.exception;

import cn.yibo.core.protocol.ResponseErrorMap;
import cn.yibo.core.protocol.ResponseT;
import cn.yibo.core.protocol.ReturnCodeEnum;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 *  描述: 定制异常错误信息，使用ResponseT包装类
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-09
 *  版本: v1.0
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace){
        boolean debug = webRequest.getParameter("debug") != null;
        ResponseT responseT = (ResponseT)webRequest.getAttribute("responseT", 0);
        Map<String, Object> errorMap = super.getErrorAttributes(webRequest, debug);

        if( responseT == null ){
            responseT = new ResponseT<>(ReturnCodeEnum.REQUEST_ERROR, (String)errorMap.get("error"), debug);
        }
        responseT.setUri((String)errorMap.get("path"));

        ResponseErrorMap<String, Object> dataMap = new ResponseErrorMap<String, Object>();
        dataMap.put("responseT", responseT);
        return dataMap;
    }
}