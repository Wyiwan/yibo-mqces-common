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

package cn.yibo.base.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述: 树型实体抽象类
 * 作者：高云
 * 时间: 2019-01-23
 * 版本: v1.0
 */
@Data
public abstract class TreeEntity<D extends TreeEntity, T> extends DataEntity<T>{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父级ID")
    private String parentId;

    @ApiModelProperty(value = "所有父级ID")
    private String ancestorId;

    @ApiModelProperty(value = "所有子级")
    private List<D> children;

    @ApiModelProperty(value = "是否叶子节点 前端所需")
    private Boolean isLeaf = true;

    @ApiModelProperty(value = "是否选中 前端所需")
    private Boolean selected;

    @ApiModelProperty(value = "是否禁用 前端所需")
    private Boolean disabled;
}
