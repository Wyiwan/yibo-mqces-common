/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：系统管理模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-12-12  高云        新建
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.service.impl;

import cn.yibo.base.service.impl.AbstractBaseService;
import com.yibo.modules.base.dao.DeptDao;
import com.yibo.modules.base.entity.Dept;
import com.yibo.modules.base.service.DeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 科室表实体服务实现层类(Dept)
 * @author 高云
 * @since 2018-12-12
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class DeptServiceImpl extends AbstractBaseService<DeptDao, Dept> implements DeptService {
    /**
     * 重写新增
     * @param dept
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int insert(Dept dept) {
        int result = super.insert(dept);
        dao.updateAncestor(dept);
        return result;
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteByIds(List list) {
        return dao.deleteCascade(list);
    }

    /**
     * 重写更新
     * @param dept
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int update(Dept dept) {
        int result = super.update(dept);
        dao.updateAncestor(dept);
        return result;
    }

    /**
     * 重写列表查询
     * @param condition
     * @return
     */
    @Override
    public List queryList(Map<String, Object> condition) {
        return dao.queryListExt(condition);
    }

    /**
     * 查询树结构数据
     * @return
     */
    @Override
    public List<Dept> findTree(Dept dept) {
        return dao.findTree(dept);
    }
}