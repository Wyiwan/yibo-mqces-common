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
{  2018-12-04  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************
*/

package com.yibo.modules.base.entity;

import cn.yibo.common.tree.Tree;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 描述: 一句话描述该类的用途
 * 作者：高云
 * 时间: 2018-12-13
 * 版本: v1.0
 */
public class DeptTree extends Tree<Dept, String> {
    public DeptTree(List<Dept> deptList){
        super(deptList);
    }

    @Override
    protected String getKey(Dept node) {
        return node.getId();
    }

    @Override
    protected String getParentKey(Dept node) {
        return node.getParentId();
    }

    @Override
    protected void setChildren(Dept node, Dept children) {
        List<Dept> currChildren = node.getChildren();
        if( currChildren == null ){
            node.setChildren(Lists.newArrayList());
        }
        node.getChildren().add(children);
        node.setIsLeaf(true);
    }
}
