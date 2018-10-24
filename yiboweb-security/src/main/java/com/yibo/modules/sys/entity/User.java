/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：User.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.sys.entity;

import com.yibo.common.entity.DataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *  描述: 一句话描述该类的用途
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Getter
@Setter
public class User extends DataEntity<String> {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "用户拥有角色")
    private List<Role> roles;

    @ApiModelProperty(value = "用户拥有的权限")
    private List<Permission> permissions;

    public User(){
        super();
    }

    public User(String id, String username, String password, String deptId, Integer status){
        this.setId(id);
        this.setDeptId(deptId);
        this.setUsername(username);
        this.setPassword(password);
        this.setStatus(status);
    }
}
