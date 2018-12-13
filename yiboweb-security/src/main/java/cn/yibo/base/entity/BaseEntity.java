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
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package cn.yibo.base.entity;

import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.security.context.UserContext;
import com.alibaba.fastjson.annotation.JSONField;
import com.yibo.modules.base.entity.User;
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

    @ApiModelProperty(value = "唯一标识")
    protected T id;

    @ApiModelProperty(value = "租户ID")
    @JSONField(serialize = false)
    protected String tenantId;

    @ApiModelProperty(value = "当前登录用户")
    @JSONField(serialize = false)
    protected User currentUser;

    public BaseEntity(){
    }

    public BaseEntity(T id){
        if( id != null ){
            this.setId(id);
        }
    }

    public String getTenantId(){
        if( ObjectUtils.isEmpty(tenantId) && UserContext.getUser() != null ){
            this.tenantId = UserContext.getUser().getTenantId();
        }
        return tenantId;
    }

    public User getCurrentUser(){
        if( this.currentUser == null ){
            this.currentUser = UserContext.getUser();
        }
        return currentUser;
    }

    public abstract void preInsert();

    public abstract void preUpdate();

    public Object clone(){
        return ObjectUtils.cloneBean(this);
    }
}