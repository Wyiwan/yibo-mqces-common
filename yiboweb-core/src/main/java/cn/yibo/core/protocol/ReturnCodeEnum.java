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

package cn.yibo.core.protocol;

/**
 *  描述: 系统错误枚举类
 *       全局异常代码枚举,例如：001001  第1位（0业务异常、1系统异常），业务模块01的001错误
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public enum ReturnCodeEnum {
    SUCCESS("000000", "success"),

    UNKNOW("100001", "未知异常"),

    NET_ERROR("100002", "网络异常，请稍后重试"),

    SERVER_ERROR("100003","系统错误"),

    REQUEST_ERROR("100004","请求错误"),

    VALIDATE_ERROR("100005","数据校验错误");

    private String code;
    private String desc;

    private ReturnCodeEnum(String code, String desc) {
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

    /**
     * 通过code去拿枚举值
     * @param code
     * @return
     */
    public static ReturnCodeEnum codeOf(String code) {
        if(code == null ){
            return NET_ERROR;
        }
        for (ReturnCodeEnum returnCodeEnum : ReturnCodeEnum.values()) {
            if (code.equals(returnCodeEnum.getCode())) {
                return returnCodeEnum;
            }
        }
        return NET_ERROR;
    }

    /**
     * 通过name去拿枚举值
     * @param name
     * @return
     */
    public static ReturnCodeEnum nameOf(String name) {
        if(name == null ){
            return NET_ERROR;
        }
        for (ReturnCodeEnum returnCodeEnum : ReturnCodeEnum.values()) {
            if (name.equals(returnCodeEnum.name())) {
                return returnCodeEnum;
            }
        }
        return NET_ERROR;
    }

}
