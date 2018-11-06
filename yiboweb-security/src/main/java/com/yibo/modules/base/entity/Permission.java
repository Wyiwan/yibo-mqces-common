/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：Permission.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.modules.base.entity;

import cn.yibo.base.entity.DataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述: 菜单/权限
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-09-16
 * 版本: v1.0
 */
@Getter
@Setter
public class Permission extends DataEntity<String> {
    @ApiModelProperty(value = "菜单/权限名称")
    private String name;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "类型 0页面 1具体操作")
    private Integer type;

    @ApiModelProperty(value = "菜单标题")
    private String title; // 操作按钮名称不能重复（相当于权限标识）

    @ApiModelProperty(value = "页面路径/资源链接url")
    private String path;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "排序值")
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "网页链接")
    private String url;

    @ApiModelProperty(value = "子菜单/权限")
    private List<Permission> children;

    @ApiModelProperty(value = "节点展开 前端所需")
    private Boolean expand = true;

    @ApiModelProperty(value = "是否勾选 前端所需")
    private Boolean checked = false;

    @ApiModelProperty(value = "是否选中 前端所需")
    private Boolean selected = false;

    public Permission(){
        super();
    }

    public Permission(String id, String name, String title, Integer type, String path){
        this.setId(id);
        this.setName(name);
        this.setTitle(title);
        this.setType(type);
        this.setPath(path);
    }

}
