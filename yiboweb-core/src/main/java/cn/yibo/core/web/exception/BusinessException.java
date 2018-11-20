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

/**
 * 描述: 业务异常类，为检查时异常，必须捕获
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public class BusinessException extends RuntimeException {
    /** 异常码 例如：001001 业务异常，业务模块01的001错误（0业务异常、1系统异常）*/
    private String errorCode;

    /** 对用户友好的错误信息 */
    private String msg;

    /** 错误堆栈信息，便于排查问题 */
    private String devops;

    /** 表示这个错误相关的web页面，可以帮助开发人员获取更多的错误的信息 */
    private String uri;

    public BusinessException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.msg = message;
    }

    public BusinessException(String errorCode, String message, String devops) {
        this(errorCode, message);
        this.devops = devops;
    }

    public BusinessException(String errorCode, String message, Throwable cause) {
        this(errorCode, message);
        this.devops = cause.getMessage();
    }

    public BusinessException(String errorCode, String message, String developMsg, String uri) {
        this(errorCode, message, developMsg);
        this.uri = uri;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDevops() {
        return devops;
    }

    public void setDevops(String devops) {
        this.devops = devops;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "errorCode='" + errorCode + '\'' +
                ", msg='" + msg + '\'' +
                ", devops='" + devops + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
