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

package cn.yibo.common.base.service.impl;

import cn.yibo.common.base.controller.BaseForm;
import cn.yibo.common.base.dao.BaseDAO;
import cn.yibo.common.base.entity.BaseEntity;
import cn.yibo.common.base.service.IBaseService;
import cn.yibo.common.utils.ObjectUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public T fetch(Object id){
        return (T)dao.fetch(id);
    }

    @Override
    public List like(Map<String, Object> condition){
        return dao.like(condition, null, null);
    }

    @Override
    public List like(Map<String, Object> condition, String orderBy, String sortBy){
        return dao.like(condition, orderBy, sortBy);
    }

    @Override
    public T findOne(String property, Object value){
        return (T)dao.findOne(property, value);
    }

    @Override
    public List findList(String property, Object value){
        return dao.findList(property, value,null, null);
    }

    @Override
    public List findList(String property, Object value, String orderBy, String sortBy){
        return dao.findList(property, value,orderBy, sortBy);
    }

    @Override
    public List findAll(){
        return dao.findAll(null, null);
    }

    @Override
    public List findAll(String orderBy, String sortBy){
        return dao.findAll(orderBy, sortBy);
    }

    @Override
    public T queryOne(Map<String, Object> condition){
        return (T)dao.queryOne(condition, null, null);
    }

    @Override
    public T queryOne(Map<String, Object> condition, String orderBy, String sortBy){
        return (T)dao.queryOne(condition, orderBy, sortBy);
    }

    @Override
    public List queryList(Map<String, Object> condition){
        return dao.queryList(condition, null, null);
    }

    @Override
    public List queryList(Map<String, Object> condition, String orderBy, String sortBy){
        return dao.queryList(condition, orderBy, sortBy);
    }

    @Override
    public <T>PageInfo<T> queryPage(BaseForm<T> baseForm){
        // 设置分页参数
        PageHelper.startPage(baseForm.getPageNo(), baseForm.getPageSize());

        // 获取查询参数
        Map<String, Object> params = baseForm.getParameters();
        logger.info("分页请求参数："+params);

        List list = dao.queryPage(params);
        return new PageInfo<T>(list);
    }

    @Override
    public List queryBySql(String executeSql){
        return dao.queryBySql(executeSql);
    }

    @Override
    public List queryTree(T t){
        return dao.queryTree(t);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteById(Object id){
        dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        dao.deleteByIds(list);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByCondition(Map<String, Object> condition){
        dao.deleteByCondition(condition);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByProperty(String property, Object value){
        dao.deleteByProperty(property, value);
    }

    @Override
    @Transactional(readOnly = false)
    public void insert(T entity){
        entity.preInsert();
        dao.insert(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void insertMap(Map<String, Object> entityMap){
        dao.insertMap(entityMap);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(T entity){
        entity.preUpdate();
        dao.update(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateNull(T entity){
        entity.preUpdate();
        dao.updateNull(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateMap(Map<String, Object> entityMap){
        dao.updateMap(entityMap);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateByCondition(Map<String, Object> updateMap, Map<String, Object> conditionMap){
        dao.updateByCondition(updateMap, conditionMap);
    }

    @Override
    @Transactional(readOnly = false)
    public int batchInsert(List list) {
        return dao.batchInsert(list);
    }

    @Override
    @Transactional(readOnly = false)
    public int batchUpdate(List list) {
        return dao.batchUpdate(list);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(T t){
        if( ObjectUtils.isNotNull(t.getId()) ){
            t.preUpdate();
            this.updateNull(t);
        }else{
            t.preInsert();
            this.insert(t);
        }
    }

    @Override
    public int count(Map<String, Object> condition){
        return dao.count(condition);
    }

    @Override
    public Object selectMaxId(){
        return dao.selectMaxId();
    }

}
