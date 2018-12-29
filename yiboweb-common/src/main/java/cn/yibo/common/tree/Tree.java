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
{  2018-12-03  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package cn.yibo.common.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 一棵树
 * @author abo
 * @param <N>树节点
 * @param <K>树节点id(唯一标识)(K须具有有效的hashCode与equals方法)
 */
public abstract class Tree<N, K> {
    // 树根
    private N root;

    // 树上所有节点的列表
    private List<N> allNodes;

    // 节点到其子节点的映射
    private Map<K, List<N>> childMap;

    // 节点到自身的映射
    private Map<K, N> selfMap;

    // 树集合
    private List<N> treeList;

    /**
     * 构造方法，传入所有树节点来构造一棵树
     * @param allNodes
     */
    public Tree(Collection<N> allNodes) {
        this.setAllNodes(new ArrayList<N>(allNodes));
    }

    public List<N> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(List<N> allNodes) {
        this.allNodes = allNodes;
    }

    public N getRoot() {
        if (root == null) {
            // 随便找一个的最上级的祖先，就是根节点
            List<N> tempList = baseTree.getParentsNode(this.getAllNodes().get(0), this.getSelfMap(), true);
            root = tempList.get(tempList.size() - 1);
        }
        return root;
    }

    public void setRoot(N root) {
        this.root = root;
    }

    public Map<K, List<N>> getChildMap() {
        if (childMap == null) {
            // 初始化
            childMap = baseTree.getAllNodeAsMap(this.getAllNodes());
        }
        return childMap;
    }

    public void setChildMap(Map<K, List<N>> childMap) {
        this.childMap = childMap;
    }

    public Map<K, N> getSelfMap() {
        if (selfMap == null) {
            selfMap = baseTree.getAllNodeSelfMap(this.getAllNodes());
        }
        return selfMap;
    }

    public void setSelfMap(Map<K, N> selfMap) {
        this.selfMap = selfMap;
    }

    public List<N> getTreeList() {
        if( treeList == null ){
            treeList = baseTree.getTreeList(this.getAllNodes());
        }
        return treeList;
    }

    /**
     * 获取节点唯一标识方法
     * @param node
     * @return
     */
    protected abstract K getKey(N node);

    /**
     * 获取节点父节点唯一标识方法
     * @param node
     * @return
     */
    protected abstract K getParentKey(N node);

    /**
     * 设置子节点
     * @param node
     */
    protected abstract void setChildren(N node, N children);

    /**
     * 树形操作的工具
     */
    private final BaseTree<N, K> baseTree = new BaseTreeImpl<N, K>() {
        @Override
        protected K getKey(N node) {
            return Tree.this.getKey(node);
        }
        @Override
        protected K getParentKey(N node) {
            return Tree.this.getParentKey(node);
        }
        @Override
        protected void setChildren(N node, N children) {
            Tree.this.setChildren(node, children);
        }
    };

    /**
     * 从某节点开始往上级搜索
     * @param node		当前节点
     * @param searcher		自定义搜索条件
     * @param needSelf		是否搜索自己
     * @return		匹配条件的节点集合(按层级从大到小排列)
     */
    public List<N> findUp(N node, BaseTreeSearcher<N> searcher, boolean needSelf) {
        return baseTree.findUp(node, searcher, needSelf, this.getSelfMap());
    }

    /**
     * 从某节点开始往上级搜索，找到一个符合条件的就返回
     * @param node		当前节点
     * @param searcher		自定义搜索条件
     * @param needSelf		是否搜索自己
     * @return		匹配条件的第一个节点(层级大的优先)	找不到则返回null
     */
    public N findUpFirst(N node, BaseTreeSearcher<N> searcher, boolean needSelf) {
        return baseTree.findUpFirst(node, searcher, needSelf, this.getSelfMap());
    }

    /**
     * 从某节点开始往下级搜索
     * @param node		当前节点
     * @param searcher		自定义搜索条件
     * @param needSelf		是否搜索自己
     * @return		匹配条件的节点集合(按层级从小到大排列)
     */
    public List<N> findDown(N node, BaseTreeSearcher<N> searcher, boolean needSelf) {
        return baseTree.findDown(node, searcher, needSelf, this.getChildMap());
    }

    /**
     * 从某节点开始往下级搜索，找到一个符合条件的就返回
     * @param node		当前节点
     * @param searcher		自定义搜索条件
     * @param needSelf		是否搜索自己
     * @return		匹配条件的第一个节点(层级小的优先)	找不到则返回null
     */
    public N findDownFirst(N node, BaseTreeSearcher<N> searcher, boolean needSelf) {
        return baseTree.findDownFirst(node, searcher, needSelf, this.getChildMap());
    }

    /**
     * 获得某节点的所有祖先节点
     * @param node		当前节点
     * @param needSelf		是否包含自己
     * @return		祖先节点集合(按层级从大到小排列)
     */
    public List<N> getParentsNode(N node, boolean needSelf) {
        return baseTree.getParentsNode(node, this.getSelfMap(), needSelf);
    }

    /**
     * 获取某节点的所有后代节点
     * @param node	当前节点
     * @param needSelf	是否包含自己
     * @return		后代节点集合(按层级从小到大排列)
     */
    public List<N> getSubNode(N node, boolean needSelf) {
        return baseTree.getSubNode(node, this.getChildMap(), needSelf);
    }

    /**
     * 获取某节点下面的所有叶子
     * @param node	当前节点
     * @return		叶子节点集合(如果自己是叶子，则集合中为自己)
     */
    public List<N> getSubLeaf(N node) {
        return baseTree.getSubLeaf(node, this.getChildMap());
    }

    /**
     * 获取某节点的父节点(直接上级)
     * @param node
     * @return		没有则返回null
     */
    public N getParent(N node) {
        return baseTree.getParent(node, this.getSelfMap());
    }

    /**
     * 获取某节点的所有孩子(直接下级)
     * @param node
     * @return		没有则返回null
     */
    public List<N> getChildren(N node) {
        return baseTree.getChildren(node, this.getChildMap());
    }

    /**
     * 判断一个节点是不是叶子
     * @param node
     * @return
     */
    public boolean isLeaf(N node) {
        return baseTree.isLeaf(node, this.getChildMap());
    }

}