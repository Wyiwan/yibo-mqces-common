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

package cn.yibo.boot.modules.base.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.yibo.boot.common.aync.ClearUserCacheThread;
import cn.yibo.boot.modules.base.dao.DeptDao;
import cn.yibo.boot.modules.base.entity.Dept;
import cn.yibo.boot.modules.base.service.DeptService;
import cn.yibo.common.base.service.impl.AbstractBaseService;
import cn.yibo.common.utils.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 科室表服务实现层
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
    public void insert(Dept dept) {
        super.insert(dept);
        dao.updateAncestor(dept);
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        dao.deleteCascade(list);
        clearUsersCacheByDeptId(list);
    }

    /**
     * 重写更新
     * @param dept
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void updateNull(Dept dept){
        super.updateNull(dept);
        dao.updateAncestor(dept);
        clearUsersCacheByDeptId(ListUtils.newArrayList(dept.getId()));
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
     * 根据科室ID清除用户缓存
     * @param deptIdList
     */
    public void clearUsersCacheByDeptId(List deptIdList){
        if( !ListUtils.isEmpty(deptIdList) ){
            ClearUserCacheThread clearUserCacheThread = new ClearUserCacheThread();
            clearUserCacheThread.setDeptIdList(deptIdList);
            ThreadUtil.execute(clearUserCacheThread);
        }
    }
}