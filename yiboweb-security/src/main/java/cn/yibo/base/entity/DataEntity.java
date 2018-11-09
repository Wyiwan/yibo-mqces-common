/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: DataEntity.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package cn.yibo.base.entity;

import cn.yibo.security.context.UserContext;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *  描述: 实体抽象类
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
@Data
public abstract class DataEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = 8724055578251954450L;

    @ApiModelProperty(value = "状态：0删除 1启用 2禁用")
    protected Integer status;

    @ApiModelProperty(value = "创建人")
    protected String createBy;

    @ApiModelProperty(value = "创建人名称")
    protected String createByName;

    @ApiModelProperty(value = "创建时间")
    protected Date createDate;

    @ApiModelProperty(value = "修改人")
    protected String updateBy;

    @ApiModelProperty(value = "修改人名称")
    protected String updateByName;

    @ApiModelProperty(value = "修改时间")
    protected Date updateDate;

    @ApiModelProperty(value = "备注")
    protected String comments;

    @Override
    public void preInsert(){
        this.createBy = UserContext.getUser().getId();
        this.updateBy = this.createBy;
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    @Override
    public void preUpdate(){
        this.updateBy = UserContext.getUser().getId();
        this.updateDate = new Date();
    }

}
