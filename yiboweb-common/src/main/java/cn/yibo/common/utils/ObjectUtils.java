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

package cn.yibo.common.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 对象操作工具类, 继承cn.hutool.core.util.ObjectUti类
 * 作者：高云
 * @version 2019-01-03
 */
public class ObjectUtils extends cn.hutool.core.util.ObjectUtil {
	public static String mapToString(Map<String, Object> paramMap){
		if( paramMap == null ){
			return "";
		}
		Map<String, Object> params = new HashMap<>(16);
		for( Map.Entry<String, Object> param : paramMap.entrySet() ){
			String key = param.getKey();
			Object paramValue = param.getValue();
			Object obj = StrUtil.containsAnyIgnoreCase(key, "password") ? "你是看不见我的" : paramValue;
			params.put(key,obj);
		}
		return JSON.toJSONString(params);
	}

	/**
	 * map转String(密码特殊处理)
	 * @param paramMap
	 * @return
	 */
	public static String mapToString2(Map<String, String[]> paramMap){
		if( paramMap == null ){
			return "";
		}
		Map<String, Object> params = new HashMap<>(16);
		for( Map.Entry<String, String[]> param : paramMap.entrySet() ){
			String key = param.getKey();
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			String obj = StrUtil.containsAnyIgnoreCase(key, "password") ? "你是看不见我的" : paramValue;
			params.put(key,obj);
		}
		return JSON.toJSONString(params);
	}

	/**
	 * map转String
	 * @param paramMap
	 * @return
	 */
	public static String mapToStringAll(Map<String, String[]> paramMap){
		if( paramMap == null ){
			return "";
		}
		Map<String, Object> params = new HashMap<>(16);
		for( Map.Entry<String, String[]> param : paramMap.entrySet() ){

			String key = param.getKey();
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			params.put(key, paramValue);
		}
		return JSON.toJSONString(params);
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(final Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.parseDouble(StrUtil.trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(final Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(final Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(final Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 转换为Boolean类型 'true', 'on', 'y', 't', 'yes' or '1' (case insensitive) will return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtil.toBoolean(val.toString()) || "1".equals(val.toString());
	}

	/**
	 * 转换为字符串
	 * @param obj
	 * @return
	 */
	public static String toString(final Object obj) {
		return toString(obj, StrUtil.EMPTY);
	}

	/**
	 * 如果对象为空，则使用defaultVal值
	 * @param obj
	 * @param defaultVal
	 * @return
	 */
	public static String toString(final Object obj, final String defaultVal) {
		return obj == null ? defaultVal : obj.toString();
	}

	/**
	 * 空转空字符串（"" to "" ; null to "" ; "null" to "" ; "NULL" to "" ; "Null" to ""）
	 * @param val 需转换的值
	 * @return 返回转换后的值
	 */
	public static String toStringIgnoreNull(final Object val) {
		return ObjectUtils.toStringIgnoreNull(val, StrUtil.EMPTY);
	}

	/**
	 * 空对象转空字符串 （"" to defaultVal ; null to defaultVal ; "null" to defaultVal ; "NULL" to defaultVal ; "Null" to defaultVal）
	 * @param val 需转换的值
	 * @param defaultVal 默认值
	 * @return 返回转换后的值
	 */
	public static String toStringIgnoreNull(final Object val, String defaultVal) {
		String str = ObjectUtils.toString(val);
		return !"".equals(str) && !"null".equals(str.trim().toLowerCase()) ? str : defaultVal;
	}
	
	/**
	 * 判断某个对象是否为空 集合类、数组做特殊处理
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		if( obj == null ){
			return true;
		}
		// 字符
		if( obj instanceof String){
			return obj.equals("");
			// 集合
		}else if( obj instanceof Collection){
			Collection coll = (Collection) obj;
			return CollectionUtils.isEmpty(coll);
			// Map
		}else if( obj instanceof Map){
			Map map = (Map) obj;
			return map.size() == 0;
			// 数组
		}else if( obj.getClass().isArray() ){
			return Array.getLength(obj) == 0;
			// 其他类型
		}else{
			return false;
		}
	}

	/**
	 * 获取给定对象值为null的属性数组
	 * @param source
	 * @return
	 */
	public static String[] getNullPropertyNames(Object source){
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for(java.beans.PropertyDescriptor pd : pds){
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
}
