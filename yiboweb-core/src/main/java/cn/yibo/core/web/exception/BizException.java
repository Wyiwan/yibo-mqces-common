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

import lombok.Data;

/**
 * 描述: 业务异常类，为检查时异常，必须捕获
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.2.4
 */
@Data
public class BizException extends RuntimeException {
    /** 异常码 例如：001001业务异常
     * 异常码分解为两部分 0-01001，第一部分：0业务异常、1系统异常；第二部分：业务模块01的001错误。
     */
    private String errorCode;

    /** 错误堆栈信息，便于排查问题 */
    private String errorInfo;

    /** 对用户友好的错误信息 */
    private String message;

    /** 表示这个错误相关的web页面，可以帮助开发人员获取更多的错误的信息 */
    private String uri;

    public BizException(String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }

    public BizException(String errorCode, String message, String errorInfo){
        this(errorCode, message);
        this.errorInfo = errorInfo;
    }

    public BizException(String errorCode, String message, String errorInfo, String uri){
        this(errorCode, message, errorInfo);
        this.uri = uri;
    }

    public BizException(String errorCode, String message, Throwable cause){
        this(errorCode, message);
        this.errorInfo = cause.getMessage();
    }

}
