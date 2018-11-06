/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: MethodCallback.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package cn.yibo.common.collect.callback;

/**
 * 方法回调接口
 * @version 2018-08-01
 */
public interface MethodCallback {

	Object execute(Object... params);
	
}
