/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：Role.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.base.entity;

import cn.yibo.base.entity.DataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *  描述: 角色
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Getter
@Setter
public class Role extends DataEntity<String> {
    @ApiModelProperty(value = "角色名")
    private String name;

    @ApiModelProperty(value = "拥有权限")
    private List<Permission> permissions;

    public Role(){
        super();
    }

    public Role(String id, String name){
        this.setId(id);
        this.setName(name);
    }
}
