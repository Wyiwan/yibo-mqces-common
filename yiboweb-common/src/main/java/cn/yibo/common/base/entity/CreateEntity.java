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
import cn.yibo.common.constant.StatusEnum;
import com.alibaba.fastjson.annotation.JSONField;
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
public abstract class CreateEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = 8724055578251954450L;

    @ApiModelProperty(value = "数据状态（0删除 1启用 2禁用）")
    protected Integer status;

    @ApiModelProperty(value = "创建人")
    @JSONField(serialize = false)
    protected String createBy;

    @ApiModelProperty(value = "创建人名称")
    @JSONField(serialize = false)
    protected String createByName;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format="yyyy-MM-dd HH:mm")
    protected Date createDate;

    @ApiModelProperty(value = "修改人")
    @JSONField(serialize = false)
    protected String updateBy;

    @ApiModelProperty(value = "修改人名称")
    @JSONField(serialize = false)
    protected String updateByName;

    @ApiModelProperty(value = "修改时间")
    @JSONField(format="yyyy-MM-dd HH:mm")
    protected Date updateDate;

    @ApiModelProperty(value = "备注")
    protected String comments;

    @Override
    public void preInsert(){
        this.updateDate = new Date();
        this.createDate = this.updateDate;

        if( ObjectUtil.isNull(this.status) ){
            this.status = StatusEnum.N.getCode();
        }
    }

    @Override
    public void preUpdate(){
        this.updateDate = new Date();

        if( ObjectUtil.isNull(this.status) ){
            this.status = StatusEnum.N.getCode();
        }
    }

    @Override
    public void enabled(){
        this.status = (this.status == StatusEnum.N.getCode() ? StatusEnum.S.getCode() : StatusEnum.N.getCode());
    }
}
