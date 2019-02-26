/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：安全控制模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的boot包
{*****************************************************************************
*/

package cn.yibo.boot.common.constant;

/**
 * 描述: 全局异常代码枚举,例如：001001  第1位（0业务异常、1系统异常），业务模块01的001错误
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public enum LoginFailEnum {
    NOT_LOGGED_ERROR("101001", "您还未登录"),

    INCORRECT_ERROR("101002", "用户名或密码错误"),

    DISABLED_ERROR("101003", "账户被禁用，请联系管理员"),

    LOGIN_FAIL_LIMIT_ERROR("101004", "登录错误次数超过限制"),

    LOGIN_EXPIRED_ERROR("101006", "登录已失效，请重新登录"),

    LOGIN_FAIL_ERROR("101007", "登录失败，其他内部错误"),

    UNDECLARED_ERROR("101008", "抱歉，您没有访问权限");

    private String code;
    private String desc;

    LoginFailEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
