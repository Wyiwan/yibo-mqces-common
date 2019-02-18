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
{  注：本模块代码为底层基础框架封装的boot包
{*****************************************************************************
*/

package cn.yibo.boot.base.entity;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.entity.User;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    protected String tenantId;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "当前登录用户")
    protected User currentUser;

    public BaseEntity(){
    }

    public BaseEntity(T id){
        if( id != null ){
            this.setId(id);
        }
    }

    public String getTenantId(){
        if( StrUtil.isEmpty(tenantId) && UserContext.getUser() != null ){
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

    public abstract void onBeforeSave();

    public abstract void enabled();

    public Object clone(){
        return ObjectUtil.clone(this);
    }
}