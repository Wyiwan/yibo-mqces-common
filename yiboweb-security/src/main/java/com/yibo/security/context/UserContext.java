/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：UserContext.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package com.yibo.security.context;

import com.yibo.security.SecurityUserDetails;
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
		return (SecurityUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
