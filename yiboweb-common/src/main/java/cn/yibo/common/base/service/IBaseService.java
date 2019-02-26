/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：公用模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-07  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的common包
{*****************************************************************************
*/

package cn.yibo.common.base.service;

import cn.yibo.common.base.controller.BaseForm;
import cn.yibo.common.base.dao.BaseDAO;
import cn.yibo.common.base.entity.BaseEntity;
import com.github.pagehelper.PageInfo;

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
    public List<T> queryBySql(String executeSql);
    public List<T> queryTree(T entity);

    public void deleteById(Object id);
    public void deleteByIds(List list);
    public void deleteByCondition(Map<String, Object> condition);
    public void deleteByProperty(String property, Object value);

    public void insert(T entity);
    public void insertMap(Map<String, Object> entityMap);

    public void update(T entity);
    public void updateNull(T entity);
    public void updateMap(Map<String, Object> entityMap);
    public void updateByCondition(Map<String, Object> updateMap, Map<String, Object> conditionMap);

    public int batchInsert(List list);
    public int batchUpdate(List list);
    public void save(T t);

    public int count(Map<String, Object> condition);
    public Object selectMaxId();
}
