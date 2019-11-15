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
 *  描述: RequestT中Style类型枚举类
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public enum StyleEnum {
    PLAIN("plain", "普通明文"),
    GZIP("gzip", "gzip压缩"),
    AES("aes", "AES加密");

    private String code;
    private String desc;

    private StyleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StyleEnum codeOf(String code) {
        if(code == null ){
            return null;
        }
        for (StyleEnum rtnCodeEnum : StyleEnum.values()) {
            if (code.equalsIgnoreCase(rtnCodeEnum.name())) {
                return rtnCodeEnum;
            }
        }
        return null;
    }

    public boolean equals(StyleEnum style){
        return style != null && style.getCode().equals(this.getCode());
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return this.getCode();
    }
}
