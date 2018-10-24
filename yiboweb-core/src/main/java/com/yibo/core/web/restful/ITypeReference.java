/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: ITypeReference.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.core.web.restful;

import com.alibaba.fastjson.TypeReference;

/**
 *  描述: 一句话描述该类的用途
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-08-08
 *  版本: v1.0
 */
public interface ITypeReference {
    /**
     * 根据url获取requestT的泛型实例
     * @param url
     * @return
     */
    public TypeReference getTypeReference(String url);
}
