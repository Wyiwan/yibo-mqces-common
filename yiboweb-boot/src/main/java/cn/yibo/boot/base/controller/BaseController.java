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

package cn.yibo.boot.base.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.common.web.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  描述: 控制器抽象类
 *  作者: gogo163gao@163.com
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public abstract class BaseController{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String SAVE_SUCCEED   = "保存成功";

    protected String UPDATE_SUCCEED = "修改成功";

    protected String DELETE_SUCCEED = "删除成功";

    protected String OPERATE_SUCCEED   = "操作成功";

    protected HttpServletRequest getHttpServletRequest(){
        return ServletUtils.getRequest();
    }

    /**
     * 获得所有请求参数
     * @return Map
     */
    protected Map<String, String[]> getParams(){
        ServletRequest request = this.getHttpServletRequest();
        if( request != null ){
            final Map<String, String[]> map = request.getParameterMap();
            return Collections.unmodifiableMap(map);
        }
        return MapUtil.newHashMap();
    }

    /**
     * 获得所有请求参数
     * @return Map
     */
    public Map<String, Object> getParamMap(){
        Map<String, Object> params = new HashMap<String, Object>();
        for( Map.Entry<String, String[]> entry : this.getParams().entrySet() ){
            params.put(entry.getKey(), ArrayUtil.join(entry.getValue(), StrUtil.COMMA));
        }
        return params;
    }

}
