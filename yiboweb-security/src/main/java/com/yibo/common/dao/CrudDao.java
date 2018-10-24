/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: CrudDao.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package com.yibo.common.dao;

import com.yibo.common.entity.BaseEntity;

/**
 *  描述: 持久化提供者接口, 用于进行 CRUD 操作
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public interface CrudDao<T extends BaseEntity> extends BaseDAO<T> {

}
