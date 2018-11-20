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

import cn.yibo.core.protocol.ResponseT;
import cn.yibo.core.protocol.ResponseTs;
import cn.yibo.core.protocol.ReturnCodeEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
     * 业务异常
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public String handleException(BusinessException exception, HttpServletRequest request){
        ResponseT<String> responseT = ResponseTs.<String>newResponseException(exception, request.getParameter("debug") != null);
        request.setAttribute("responseT", responseT);

        log.error("invoke {} error: {} ", request.getRequestURL(), exception.getMsg());
        return "forward:/error";
    }

    /**
     * 运行时异常
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

    /**
     * 数据校验异常
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler({BindException.class})
    public String bindException(BindException exception, HttpServletRequest request) {
        BindingResult bindingResult = exception.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<Map<String,Object>> errorList = Lists.newArrayList();

        allErrors.forEach(objectError -> {
            Map<String,Object> errorMap = Maps.newHashMap();
            FieldError fieldError = (FieldError)objectError;
            errorMap.put("field", fieldError.getField());
            errorMap.put("objectName", fieldError.getObjectName());
            errorMap.put("message", fieldError.getDefaultMessage());
            errorList.add(errorMap);
        });

        ResponseT<List> responseT = ResponseTs.<List>newValidateError();
        responseT.setBizdata(errorList);
        request.setAttribute("responseT", responseT);

        log.error("invoke {} error: {} ", request.getRequestURL(), exception);
        return "forward:/error";
    }

    /**
     * 未知异常
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, HttpServletRequest request){
        ResponseT<String> responseT = new ResponseT<>(ReturnCodeEnum.UNKNOW, exception.getMessage(), request.getParameter("debug") != null);
        request.setAttribute("responseT", responseT);

        log.error("invoke {} error: {} ", request.getRequestURL(), exception);
        return "forward:/error";
    }

}
