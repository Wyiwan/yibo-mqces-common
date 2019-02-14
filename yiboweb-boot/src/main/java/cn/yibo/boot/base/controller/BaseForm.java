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

package cn.yibo.boot.base.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.common.web.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基础参数辅助类<p/>
 * @author kfzx-gaoyun
 * @version 2016-05-12
 */
@Slf4j
public class BaseForm<T>{
	/**
	 * 查询参数对象
	 */
	protected Map<String, Object> values = new LinkedHashMap<String, Object>();

	/**
	 * 当前页码
	 */
	private int pageNo = 1;

	/**
	 * 页大小
	 */
	private int pageSize = 10;

	/**
	 * 构造方法
	 */
	public BaseForm(){
		try{
			HttpServletRequest request = ServletUtils.getRequest();
			Enumeration<String> params = request.getParameterNames();
			while( params.hasMoreElements() ){
				String name = params.nextElement();
				String value = StrUtil.trim(request.getParameter(name));
				this.set(name, URLDecoder.decode(value,"UTF-8"));
			}
			this.parsePagingQueryParams();
		}catch(Exception e){
			e.printStackTrace();
			log.error("BaseControlForm initialize parameters setting error：" +e);
		}
	}
	
	/**
	 * 获取页码
	 * @return
	 */
	public int getPageNo(){
		String page = ObjectUtils.toString( this.get("page") );
		
		if( !StrUtil.isEmpty(page) && NumberUtil.isNumber(page) ){
			this.pageNo = Integer.parseInt(page);
		}
		return this.pageNo;
	}

	/**
	 * 获取页大小
	 * @return
	 */
	public int getPageSize(){
		String rows = ObjectUtils.toString( this.get("rows") );
		
		if( !StrUtil.isEmpty(rows) || NumberUtil.isNumber(rows) ){
			this.pageSize = Integer.parseInt(rows);
		}
		return this.pageSize;
	}
	
	/**
	 * 获得参数信息对象
	 * @return
	 */
	public Map<String, Object> getParameters(){
		return values;
	}
	
	/**
	 * 根据key获取values中的值
	 * @param name
	 * @return
	 */
    public Object get(String name){
    	if( values == null ){
    		values = new LinkedHashMap<String, Object>();
    		return null;
    	}
    	return this.values.get(name);
	}
    
	/**
	 * 根据key获取values中String类型值
	 * @param key
	 * @return String
	 */
    public String getString(String key) {
		return ObjectUtils.toString( get(key) );
	}
    
    /**
     * 获取排序字段
     * @return
     */
    public String getSort(){
    	return ObjectUtils.toString( this.values.get("sort") );
    }
    
    /**
     * 获取排序
     * @return
     */
    public String getOrder(){
    	return ObjectUtils.toString( this.values.get("order") );
    }
    
    /**
     * 获取排序
     * @return
     */
    public String getOrderby(){
    	return ObjectUtils.toString( this.values.get("orderby") );
    }

	/**
	 * 解析分页排序参数
	 */
	public void parsePagingQueryParams(){
		// 排序字段解析
		String orderby = ObjectUtils.toString( this.get("orderby") ).trim();
		String sortName = ObjectUtils.toString( this.get("sort") ).trim();
		String sortOrder = ObjectUtils.toString( this.get("order") ).trim().toLowerCase();

		if( StrUtil.isEmpty(orderby) && !StrUtil.isEmpty(sortName) ){
			if ( !sortOrder.equals("asc") && !sortOrder.equals("desc") ) {
				sortOrder = "asc";
			}
			this.set("orderby", sortName + " " + sortOrder);
		}
	}

	/**
     * 设置参数
     * @param name 参数名称
     * @param value 参数值
     */
	public void set(String name, Object value){
		if( ObjectUtil.isNotNull(value) ){
			this.values.put(name, value);
		}
	}
	
	/**
	 * 移除参数
	 * @param name
	 */
	public void remove(String name){
	    this.values.remove(name);
	}

	/**
	 * 清除所有参数
	 */
	public void clear(){
		if( values != null ){
			values.clear();
		}
	}

}