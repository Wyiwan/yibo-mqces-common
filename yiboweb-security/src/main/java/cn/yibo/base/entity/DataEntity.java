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
import com.yibo.modules.base.constant.CommonConstant;
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

    @ApiModelProperty(value = "状态(0删除 1启用 2禁用)")
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

        if( UserContext.getUser() != null ){
            this.updateBy = UserContext.getUser().getId();
            this.createBy = this.updateBy;
        }
        this.status = ObjectUtils.isEmpty(this.status) ? CommonConstant.STATUS_NORMAL : this.status;
    }

    @Override
    public void preUpdate(){
        this.updateDate = new Date();

        if( UserContext.getUser() != null ){
            this.updateBy = UserContext.getUser().getId();
        }
    }

    public void enabled(){
        this.status = (this.status == CommonConstant.STATUS_NORMAL ? CommonConstant.STATUS_DISABLE : CommonConstant.STATUS_NORMAL);
    }
}
