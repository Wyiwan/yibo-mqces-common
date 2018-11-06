/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：SecurityUser.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package cn.yibo.security.annotation;

import java.lang.annotation.*;

/**
 *  描述: 获取当前用户
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-29
 *  版本: v1.0
 *  @see SecurityUserArgumentResolver
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityUser {
}
