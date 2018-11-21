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

import cn.yibo.security.constant.CommonConstant;
import cn.yibo.security.context.UserContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *  描述: 实体抽象类
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
@Data
public abstract class DataEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = 8724055578251954450L;

    @ApiModelProperty(value = "状态：0删除 1启用 2禁用")
    protected Integer status;

    @ApiModelProperty(value = "创建人")
    @JsonIgnore
    protected String createBy;

    @ApiModelProperty(value = "创建人名称")
    @JsonIgnore
    protected String createByName;

    @ApiModelProperty(value = "创建时间")
    @JsonIgnore
    protected Date createDate;

    @ApiModelProperty(value = "修改人")
    @JsonIgnore
    protected String updateBy;

    @ApiModelProperty(value = "修改人名称")
    @JsonIgnore
    protected String updateByName;

    @ApiModelProperty(value = "修改时间")
    @JsonIgnore
    protected Date updateDate;

    @ApiModelProperty(value = "备注")
    protected String comments;

    @Override
    public void preInsert(){
        this.status = CommonConstant.STATUS_NORMAL;
        this.updateDate = new Date();
        this.createDate = this.updateDate;

        if( UserContext.getUser() != null ){
            this.createBy = UserContext.getUser().getId();
            this.updateBy = this.createBy;
        }
    }

    @Override
    public void preUpdate(){
        this.updateDate = new Date();

        if( UserContext.getUser() != null ){
            this.updateBy = UserContext.getUser().getId();
        }
    }

}
