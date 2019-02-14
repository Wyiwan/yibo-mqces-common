/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：安全控制模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2019-01-23  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package cn.yibo.boot.base.entity;

import cn.hutool.core.collection.CollUtil;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.common.tree.Tree;

import java.util.Collection;
import java.util.List;

/**
 * 描述: 树型构建工具类
 * 作者：高云
 * 时间: 2019-01-23 TreeBuild
 * 版本: v1.0
 */
public class TreeBuild extends Tree<TreeEntity, String> {
    /**
     * 构造方法，传入所有树节点来构造一棵树
     * @param allNodes
     */
    public TreeBuild(Collection<? extends TreeEntity> allNodes) {
        super(allNodes);
    }

    @Override
    protected String getKey(TreeEntity node) {
        return ObjectUtils.toString(node.getId());
    }

    @Override
    protected String getParentKey(TreeEntity node) {
        return ObjectUtils.toString(node.getParentId());
    }

    @Override
    protected void setChildren(TreeEntity parent, TreeEntity children) {
        List<TreeEntity> currChildren = parent.getChildren();
        if( currChildren == null ){
            parent.setChildren(CollUtil.newArrayList());
        }
        parent.getChildren().add(children);
        parent.setIsLeaf(false);
    }
}
