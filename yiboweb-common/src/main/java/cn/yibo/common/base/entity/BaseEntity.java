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

package cn.yibo.common.base.entity;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  描述: 实体抽象类
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
@Data
public abstract class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 7924850300269237733L;

    @ApiModelProperty(value = "主键ID")
    protected T id;

    public BaseEntity(){
    }

    public abstract void preInsert();

    public abstract void preUpdate();

    public abstract void enabled();

    public Object clone(){
        return ObjectUtil.clone(this);
    }
}