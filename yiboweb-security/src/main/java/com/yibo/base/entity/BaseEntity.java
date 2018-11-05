/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: BaseEntity.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package com.yibo.base.entity;

import com.yibo.common.lang.ObjectUtils;
import lombok.Data;

import java.io.Serializable;

/**
 *  描述: 实体抽象类
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
@Data
public abstract class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 7924850300269237733L;

    protected T id;

    public BaseEntity(){
    }

    public BaseEntity(T id){
        if( id != null ){
            this.setId(id);
        }
    }

    public abstract void preInsert();

    public abstract void preUpdate();

    public Object clone(){
        return ObjectUtils.cloneBean(this);
    }
}