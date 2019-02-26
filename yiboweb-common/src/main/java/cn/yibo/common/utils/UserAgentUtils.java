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

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户代理字符串识别工具
 * @version 2018-08-01
 */
public class UserAgentUtils {

	/**
	 * 获取用户代理对象
	 * @param request
	 * @return
	 */
	public static UserAgent getUserAgent(HttpServletRequest request){
		return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
	}
	
	/**
	 * 获取设备类型
	 * @param request
	 * @return
	 */
	public static DeviceType getDeviceType(HttpServletRequest request){
		return getUserAgent(request).getOperatingSystem().getDeviceType();
	}
	
	/**
	 * 是否是PC
	 * @param request
	 * @return
	 */
	public static boolean isComputer(HttpServletRequest request){
		return DeviceType.COMPUTER.equals(getDeviceType(request));
	}

	/**
	 * 是否是手机
	 * @param request
	 * @return
	 */
	public static boolean isMobile(HttpServletRequest request){
		return DeviceType.MOBILE.equals(getDeviceType(request));
	}
	
	/**
	 * 是否是平板
	 * @param request
	 * @return
	 */
	public static boolean isTablet(HttpServletRequest request){
		return DeviceType.TABLET.equals(getDeviceType(request));
	}

	/**
	 * 是否是手机和平板
	 * @param request
	 * @return
	 */
	public static boolean isMobileOrTablet(HttpServletRequest request){
		DeviceType deviceType = getDeviceType(request);
		return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
	}
	
	/**
	 * 获取浏览类型
	 * @param request
	 * @return
	 */
	public static Browser getBrowser(HttpServletRequest request){
		return getUserAgent(request).getBrowser();
	}
	
	/**
	 * 是否IE版本是否小于等于IE8
	 * @param request
	 * @return
	 */
	public static boolean isLteIE8(HttpServletRequest request){
		Browser browser = getBrowser(request);
		return Browser.IE5.equals(browser) || Browser.IE6.equals(browser)
				|| Browser.IE7.equals(browser) || Browser.IE8.equals(browser);
	}
	
}
