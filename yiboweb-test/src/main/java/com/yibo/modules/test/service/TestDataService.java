/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: TestDataService.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.modules.test.service;

import cn.yibo.base.service.IBaseService;
import com.yibo.modules.test.entity.TestData;
import com.yibo.modules.test.dao.TestDataDao;

/**
 *  描述: 服务接口层
 *  作者: 高云
 *  时间: 2018-08-07
 *  版本: v1.0
 */
public interface TestDataService extends IBaseService<TestDataDao, TestData> {

}