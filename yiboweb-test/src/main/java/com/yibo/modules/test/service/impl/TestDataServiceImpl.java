/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: TestDataServiceImpl.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.modules.test.service.impl;

import com.yibo.base.service.impl.AbstractBaseService;
import com.yibo.modules.test.dao.TestDataDao;
import com.yibo.modules.test.entity.TestData;
import com.yibo.modules.test.service.TestDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述: 实现层
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@Service
@Transactional(readOnly=true)
public class TestDataServiceImpl extends AbstractBaseService<TestDataDao, TestData> implements TestDataService {

}
