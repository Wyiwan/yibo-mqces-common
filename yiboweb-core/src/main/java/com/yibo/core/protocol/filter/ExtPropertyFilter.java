/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: ExtPropertyFilter.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.core.protocol.filter;

import com.alibaba.fastjson.serializer.PropertyFilter;
import com.yibo.core.protocol.RequestT;
import com.yibo.core.protocol.ResponseT;
import com.yibo.core.protocol.StyleEnum;

/**
 * 描述: 一句话描述该类的用途
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-08
 * 版本: v1.0
 */
public class ExtPropertyFilter implements PropertyFilter {
    public static String[] filterParams = new String[]{"data", "bizdata"};

    @Override
    public boolean apply(Object object, String name, Object value) {
        if (object instanceof RequestT) {
            RequestT re = (RequestT) object;
            StyleEnum style = re.getStyle();
            if (!StyleEnum.PLAIN.equals(style) && contains(name)) {
                return false;
            }
        }
        if (object instanceof ResponseT) {
            ResponseT re = (ResponseT) object;
            StyleEnum style = re.getStyle();
            if (!StyleEnum.PLAIN.equals(style) && contains(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean contains(String name){
        for(String filerParam : filterParams){
            if(filerParam.equals(name)){
                return true;
            }
        }
        return false;
    }
}
