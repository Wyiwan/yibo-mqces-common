/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：GlobalErrorAttributes.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.core.web.exception;

import com.yibo.core.protocol.ResponseErrorMap;
import com.yibo.core.protocol.ResponseT;
import com.yibo.core.protocol.ReturnCodeEnum;
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
        dataMap.put("debug", debug);
        dataMap.put("error", errorMap);
        dataMap.put("responseT", responseT);
        return dataMap;
    }
}