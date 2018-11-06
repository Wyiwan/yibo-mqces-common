/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: BaseForm.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package cn.yibo.base.controller;

import cn.yibo.common.lang.ObjectUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.common.web.http.ServletUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;

/**
 * 基础参数辅助类<p/>
 * @author kfzx-gaoyun
 * @version 2016-05-12
 */
public class BaseForm<T>{
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 查询参数对象
	 */
	protected Map<String, Object> values = Maps.newLinkedHashMap();

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
				this.set(name, URLDecoder.decode(request.getParameter(name),"UTF-8"));
			}
			this.parsePagingQueryParams();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("BaseControlForm initialize parameters setting error：" +e);
		}
	}
	
	/**
	 * 获取页码
	 * @return
	 */
	public int getPageNo(){
		String page = ObjectUtils.toString( this.get("page") );
		
		if( !StringUtils.isEmpty(page) && StringUtils.isNumeric(page) ){
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
		
		if( !StringUtils.isEmpty(rows) || StringUtils.isNumeric(rows) ){
			this.pageSize = Integer.parseInt(rows);
		}
		return this.pageSize;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		return (getPageNo() - 1) * getPageSize();
	}
	
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
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
    		values = Maps.newLinkedHashMap();
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
		if( StringUtils.isEmpty(orderby) && !StringUtils.isEmpty(sortName) ){
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
		if( !ObjectUtils.isEmpty(value) ){
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