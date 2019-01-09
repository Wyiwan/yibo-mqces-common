/*
{*****************************************************************************
{  广州医博-基础框架 v1.0													
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.					
{  创建人：  高云
{  审查人：
{  模块：系统管理模块										
{  功能描述:										
{		 													
{  ---------------------------------------------------------------------------	
{  维护历史:													
{  日期        维护人        维护类型						
{  ---------------------------------------------------------------------------	
{  2019-01-07  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package com.yibo.modules.base.entity;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.base.entity.DataEntity;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * 操作日志表实体类(Log)
 * @author 高云
 * @since 2019-01-07
 * @version v1.0
 */
@Data
@ApiModel(value = "操作日志表实体类(Log)")
public class Log extends DataEntity<String>{
    // 日志类型（access：访问日志；update：修改日志；delete：删除日志；select：查询日志；login：登录；logout：登出；error：异常）
    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_INSERT = "insert";
    public static final String TYPE_UPDATE = "update";
    public static final String TYPE_DELETE = "delete";
    public static final String TYPE_SELECT = "select";
    public static final String TYPE_LOGIN = "login";
    public static final String TYPE_LOGOUT = "logout";
    public static final String TYPE_ERROR = "error";

    @NotEmpty(message="日志类型不能为空")
    @ApiModelProperty(value = "日志类型")
    private String logType;
    
    @NotEmpty(message="日志标题不能为空")
    @ApiModelProperty(value = "日志标题")
    private String logTitle;
    
    @NotEmpty(message="用户名称不能为空")
    @ApiModelProperty(value = "用户名称")
    private String createByName;
    
    @ApiModelProperty(value = "请求URI")
    private String requestUri;
    
    @ApiModelProperty(value = "操作方式")
    private String requestMethod;
    
    @ApiModelProperty(value = "操作提交的数据")
    private Object requestParams;
    
    @ApiModelProperty(value = "新旧数据比较结果")
    private String diffModifyData;
    
    @ApiModelProperty(value = "业务主键")
    private String bizKey;
    
    @ApiModelProperty(value = "业务类型")
    private String bizType;
    
    @NotEmpty(message="操作IP地址不能为空")
    @ApiModelProperty(value = "操作IP地址")
    private String remoteAddr;
    
    @NotEmpty(message="请求服务器地址不能为空")
    @ApiModelProperty(value = "请求服务器地址")
    private String serverAddr;
    
    @ApiModelProperty(value = "是否异常")
    private String isException;
    
    @ApiModelProperty(value = "异常信息")
    private String exceptionInfo;
    
    @ApiModelProperty(value = "用户代理")
    private String userAgent;
    
    @ApiModelProperty(value = "设备名称/操作系统")
    private String deviceName;
    
    @ApiModelProperty(value = "浏览器名称")
    private String browserName;
    
    @ApiModelProperty(value = "执行时间")
    private Long executeTime;

    @JSONField(serialize = false)
    private boolean sqlCommand;

    // 操作提交的数据，临时存储用
    private Map<String, String[]> paramsMap;

    /**
     * 设置请求参数
     * @param paramsMap
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setRequestParams(Map paramsMap){
        if (paramsMap == null){
            return;
        }
        if (this.paramsMap == null){
            this.paramsMap = MapUtil.newHashMap();
        }
        StringBuilder params = new StringBuilder();
        for( Map.Entry<String, String[]> param : ((Map<String, String[]>)paramsMap).entrySet() ){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");

            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StrUtil.brief(StrUtil.endWithIgnoreCase(param.getKey(), "password") ? "*" : paramValue, 1000));
            this.paramsMap.put(param.getKey(), param.getValue());
        }
        this.requestParams = params.toString();
    }

    /**
     * 根据名称获取参数（只有先执行setParams(Map)后才有效）
     * @param name
     * @return
     */
    public String getRequestParam(String name){
        if (paramsMap == null){
            return null;
        }
        String[] values = (String[])paramsMap.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }
}