/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: AbstractBaseService.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package com.yibo.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yibo.common.controller.BaseForm;
import com.yibo.common.dao.BaseDAO;
import com.yibo.common.entity.BaseEntity;
import com.yibo.common.lang.ObjectUtils;
import com.yibo.common.service.IBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *  描述: 服务抽象类
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
@Transactional(readOnly=true)
public abstract class AbstractBaseService<D extends BaseDAO, T extends BaseEntity> implements IBaseService<D, T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected D dao;

    @Override
    public T fetch(Object id) {
        return (T)dao.fetch(id);
    }

    @Override
    public List like(Map<String, Object> condition) {
        return dao.like(condition, null, null);
    }

    @Override
    public List like(Map<String, Object> condition, String orderBy, String sortBy) {
        return dao.like(condition, orderBy, sortBy);
    }

    @Override
    public T findOne(String property, Object value) {
        return (T)dao.findOne(property, value);
    }

    @Override
    public List findList(String property, Object value) {
        return dao.findList(property, value,null, null);
    }

    @Override
    public List findList(String property, Object value, String orderBy, String sortBy) {
        return dao.findList(property, value,orderBy, sortBy);
    }

    @Override
    public List findAll() {
        return dao.findAll(null, null);
    }

    @Override
    public List findAll(String orderBy, String sortBy) {
        return dao.findAll(orderBy, sortBy);
    }

    @Override
    public T queryOne(Map<String, Object> condition) {
        return (T)dao.queryOne(condition, null, null);
    }

    @Override
    public T queryOne(Map<String, Object> condition, String orderBy, String sortBy) {
        return (T)dao.queryOne(condition, orderBy, sortBy);
    }

    @Override
    public List queryList(Map<String, Object> condition) {
        return dao.queryList(condition, null, null);
    }

    @Override
    public List queryList(Map<String, Object> condition, String orderBy, String sortBy) {
        return dao.queryList(condition, orderBy, sortBy);
    }

    @Override
    public <T>PageInfo<T> queryPage(BaseForm<T> baseForm) {
        // 设置分页参数
        if( !ObjectUtils.isEmpty( baseForm.get("page") ) ){
            PageHelper.startPage(baseForm.getPageNo(), baseForm.getPageSize());
        }

        // 获取查询参数
        Map<String, Object> params = baseForm.getParameters();
        logger.info("分页请求参数："+params);

        List<T> list = dao.queryPage(params);
        return new PageInfo<T>(list);
    }

    @Override
    public List queryBySql(String executeSql) {
        return dao.queryBySql(executeSql);
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteById(Object id) {
        return dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteByIds(List list) {
        return dao.deleteByIds(list);
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteByCondition(Map<String, Object> condition) {
        return dao.deleteByCondition(condition);
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteByProperty(String property, Object value) {
        return dao.deleteByProperty(property, value);
    }

    @Override
    @Transactional(readOnly = false)
    public int insert(T entity) {
        entity.preInsert();
        return dao.insert(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public int insertMap(Map<String, Object> entityMap) {
        return dao.insertMap(entityMap);
    }

    @Override
    @Transactional(readOnly = false)
    public int update(T entity) {
        entity.preUpdate();
        return dao.update(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public int updateNull(T entity) {
        entity.preUpdate();
        return dao.updateNull(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public int updateMap(Map<String, Object> entityMap) {
        return dao.updateMap(entityMap);
    }

    @Override
    @Transactional(readOnly = false)
    public int updateByCondition(Map<String, Object> updateMap, Map<String, Object> conditionMap) {
        return dao.updateByCondition(updateMap, conditionMap);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(T t) {
        if( t.getId() != null ){
            t.preUpdate();
            dao.update(t);
        }else{
            t.preInsert();
            dao.insert(t);
        }
    }

    @Override
    public int count(Map<String, Object> condition) {
        return dao.count(condition);
    }

    @Override
    public Object selectMaxId() {
        return dao.selectMaxId();
    }
}
