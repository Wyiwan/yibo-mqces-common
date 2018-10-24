/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: TestData.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.modules.test.entity;

import com.yibo.common.entity.DataEntity;

/**
 * 描述: 实体类
 * 作者: 高云
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public class TestData extends DataEntity<String> {
    private String name;
    private String shortName;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


}
