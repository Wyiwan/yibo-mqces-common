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

import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.common.base.entity.CreateEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  描述: 实体抽象类
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
@Data
public abstract class DataEntity<T> extends CreateEntity<T> {
    private static final long serialVersionUID = -2284090858176882512L;

    @ApiModelProperty(value = "租户ID")
    protected String tenantId;

    public String getTenantId(){
        if( StrUtil.isEmpty(tenantId) && UserContext.getUser() != null ){
            this.tenantId = UserContext.getUser().getTenantId();
        }
        return tenantId;
    }

    @Override
    public void preInsert(){
        super.preInsert();

        if( UserContext.getUser() != null ){
            this.updateBy = UserContext.getUser().getId();
            this.createBy = this.updateBy;
        }
    }

    @Override
    public void preUpdate(){
        super.preUpdate();

        if( UserContext.getUser() != null ){
            this.updateBy = UserContext.getUser().getId();
        }
    }

}
