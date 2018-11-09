/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: TestData.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */
package com.yibo.modules.test.entity;

import cn.yibo.base.entity.DataEntity;
import lombok.Data;

/**
 * 描述: 实体类
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@Data
public class TestData extends DataEntity<String> {
    private String name;

    private String shortName;

    private Integer age;

}
