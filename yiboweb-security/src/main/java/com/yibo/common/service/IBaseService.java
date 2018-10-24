/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: IBaseService.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package com.yibo.common.service;

import com.github.pagehelper.PageInfo;
import com.yibo.common.controller.BaseForm;
import com.yibo.common.dao.BaseDAO;
import com.yibo.common.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  描述: 服务接口类
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public interface IBaseService<D extends BaseDAO, T extends BaseEntity> {
    public T fetch(Object id);

    public List<T> like(Map<String, Object> condition);
    public List<T> like(Map<String, Object> condition, String orderBy, String sortBy);

    public T findOne(String property, Object value);
    public List<T> findList(String property, Object value);
    public List<T> findList(String property, Object value, String orderBy, String sortBy);
    public List<T> findAll();
    public List<T> findAll(String orderBy, String sortBy);

    public T queryOne(Map<String, Object> condition);
    public T queryOne(Map<String, Object> condition, String orderBy, String sortBy);
    public List<T> queryList(Map<String, Object> condition);
    public List<T> queryList(Map<String, Object> condition, String orderBy, String sortBy);
    public <T>PageInfo<T> queryPage(BaseForm<T> baseForm);
    public List<T> queryBySql(@Param("executeSql") String executeSql);

    public int deleteById(Object id);
    public int deleteByIds(List list);
    public int deleteByCondition(Map<String, Object> condition);
    public int deleteByProperty(String property, Object value);

    public int insert(T entity);
    public int insertMap(Map<String, Object> entityMap);

    public int update(T entity);
    public int updateNull(T entity);
    public int updateMap(Map<String, Object> entityMap);
    public int updateByCondition(Map<String, Object> updateMap, Map<String, Object> conditionMap);

    public void save(T t);
    public int count(Map<String, Object> condition);
    public Object selectMaxId();
}
