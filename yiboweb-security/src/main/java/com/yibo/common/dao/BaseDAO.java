/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: BaseDAO.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package com.yibo.common.dao;

import com.yibo.common.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  描述: 持久化提供者接口，使用统一的接口定义，利于编码习惯统一
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public interface BaseDAO<T extends BaseEntity>{
    public T fetch(Object id);

    public List<T> like(@Param("condition") Map<String, Object> condition, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);

    public T findOne(@Param("property") String property, @Param("value") Object value);
    public List<T> findList(@Param("property") String property, @Param("value") Object value, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);
    public List<T> findAll(@Param("orderBy") String orderBy, @Param("sortBy") String sortBy);

    public T queryOne(@Param("condition") Map<String, Object> condition, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);
    public List<T> queryList(@Param("condition") Map<String, Object> condition, @Param("orderBy") String orderBy, @Param("sortBy") String sortBy);
    public List<T> queryPage(@Param("condition") Map<String, Object> condition);
    public List<T> queryBySql(@Param("executeSql") String executeSql);

    public int deleteById(Object id);
    public int deleteByIds(List list);
    public int deleteByCondition(@Param("condition") Map<String, Object> condition);
    public int deleteByProperty(@Param("property") String property, @Param("value") Object value);

    public int insert(T entity);
    public int insertMap(@Param("map") Map<String, Object> entityMap);

    public int update(T entity);
    public int updateNull(T entity);
    public int updateMap(@Param("map") Map<String, Object> entityMap);
    public int updateByCondition(@Param("update") Map<String, Object> updateMap, @Param("condition") Map<String, Object> conditionMap);

    public int count(@Param("condition") Map<String, Object> condition);
    public Object selectMaxId();

}