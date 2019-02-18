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

package cn.yibo.boot.config.security.context;

import cn.yibo.boot.config.security.SecurityUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *  描述: 一句话描述该类的用途
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-28
 *  版本: v1.0
 */
public class UserContext {

	public static SecurityUserDetails getUser(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if( principal != null && principal instanceof SecurityUserDetails ){
			return (SecurityUserDetails)principal;
		}
		return null;
	}

}
