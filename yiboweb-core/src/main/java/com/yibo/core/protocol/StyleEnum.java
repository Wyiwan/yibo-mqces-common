/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: StyleEnum.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.core.protocol;

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
