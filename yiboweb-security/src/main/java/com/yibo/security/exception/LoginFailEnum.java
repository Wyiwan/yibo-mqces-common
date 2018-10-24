/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：LoginFailEnum.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.security.exception;

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

    TOKEN_ERROR("101005", "解析token错误"),

    LOGIN_FAIL_ERROR("101006", "登录失败，其他内部错误"),

    UNDECLARED_ERROR("101007", "抱歉，您没有访问权限");

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
