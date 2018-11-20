/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：工具类模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的common包
{*****************************************************************************
*/

package cn.yibo.common.network;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Request请求过滤包装
 * @version  2018-08-01
 */
public class WafWrapperUtils extends HttpServletRequestWrapper {

	private boolean filterXSS = true;

	private boolean filterSQL = true;


	public WafWrapperUtils(HttpServletRequest request, boolean filterXSS, boolean filterSQL) {
		super(request);
		this.filterXSS = filterXSS;
		this.filterSQL = filterSQL;
	}


	public WafWrapperUtils(HttpServletRequest request) {
		this(request, true, true);
	}


	/**
	 * @Description 数组参数过滤
	 * @param parameter
	 * 				过滤参数
	 * @return
	 */
	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if ( values == null ) {
			return null;
		}

		int count = values.length;
		String[] encodedValues = new String[count];
		for ( int i = 0 ; i < count ; i++ ) {
			encodedValues[i] = filterParamString(values[i]);
		}

		return encodedValues;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getParameterMap() {
		Map<String, String[]> primary = super.getParameterMap();
		Map<String, String[]> result = new HashMap<String, String[]>(primary.size());
		for ( Map.Entry<String, String[]> entry : primary.entrySet() ) {
			result.put(entry.getKey(), filterEntryString(entry.getValue()));
		}
		return result;

	}
	
	protected String[] filterEntryString(String[] rawValue) {
		for ( int i = 0 ; i < rawValue.length ; i++ ) {
			rawValue[i] = filterParamString(rawValue[i]);
		}
		return rawValue;
	}

	/**
	 * @Description 参数过滤
	 * @param parameter
	 * 				过滤参数
	 * @return
	 */
	@Override
	public String getParameter(String parameter) {
		return filterParamString(super.getParameter(parameter));
	}


	/**
	 * @Description 请求头过滤 
	 * @param name
	 * 				过滤内容
	 * @return
	 */
	@Override
	public String getHeader(String name) {
		return filterParamString(super.getHeader(name));
	}


	/**
	 * @Description Cookie内容过滤
	 * @return
	 */
	@Override
	public Cookie[] getCookies() {
		Cookie[] existingCookies = super.getCookies();
		if (existingCookies != null) {
			for (int i = 0 ; i < existingCookies.length ; ++i) {
				Cookie cookie = existingCookies[i];
				cookie.setValue(filterParamString(cookie.getValue()));
			}
		}
		return existingCookies;
	}

	/**
	 * @Description 过滤字符串内容
	 * @param rawValue
	 * 				待处理内容
	 * @return
	 */
	protected String filterParamString(String rawValue) {
		if (null == rawValue) {
			return null;
		}
		String tmpStr = rawValue;
		if (this.filterXSS) {
			tmpStr = WafUtils.stripXSS(rawValue);
		}
		if (this.filterSQL) {
			tmpStr = WafUtils.stripSqlInjection(tmpStr);
		}
		return tmpStr;
	}
}
