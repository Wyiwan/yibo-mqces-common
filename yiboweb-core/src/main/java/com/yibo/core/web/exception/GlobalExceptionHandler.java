/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：GlobalExceptionHandler.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.core.web.exception;

import com.yibo.core.protocol.ResponseT;
import com.yibo.core.protocol.ResponseTs;
import com.yibo.core.protocol.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 *  描述: 全局的的异常拦截器（拦截所有的控制器）
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-08
 *  版本: v1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(BizException.class)
    public String handleException(BizException exception, HttpServletRequest request){
        ResponseT<String> responseT = ResponseTs.<String>newResponseException(exception, request.getParameter("debug") != null);
        request.setAttribute("responseT", responseT);

        log.error("invoke {} error: {} ", request.getRequestURL(), exception.getMsg());
        return "forward:/error";
    }

    /**
     * 未知的运行时异常
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException exception, HttpServletRequest request){
        String errorMsg = exception.getMessage();

        if( exception instanceof NullPointerException ){
            errorMsg = exception.toString();
        }

        ResponseT<String> responseT = new ResponseT<>(ReturnCodeEnum.REQUEST_ERROR, errorMsg, request.getParameter("debug") != null);
        request.setAttribute("responseT", responseT);

        log.error("invoke {} error: {} ", request.getRequestURL(), exception);
        return "forward:/error";
        //return new ResponseEntity<>(responseT, HttpStatus.UNAUTHORIZED);
    }

}
