/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: ExtFastJsonHttpMessageConverter.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.core.web.restful;

import com.alibaba.fastjson.JSON;
import com.yibo.common.web.http.ServletUtils;
import com.yibo.core.protocol.ResponseMap;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 描述: 一句话描述该类的用途
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-10
 * 版本: v1.0
 */
public class ExtFastJsonHttpMessageConverter<T> extends FastJsonHttpMessageConverter<T> {

    protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        InputStream in = inputMessage.getBody();

        byte[] buf = new byte[1024];
        for (;;) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }

            if (len > 0) {
                baos.write(buf, 0, len);
            }
        }

        byte[] bytes = baos.toByteArray();
        return JSON.parseObject(bytes, 0, bytes.length, getCharset().newDecoder(), clazz);
    }

    protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String uri = ServletUtils.getRequest().getRequestURL().toString();
        ServletUtils.getResponse().setStatus(HttpStatus.SC_OK);

        if( (t != null && (t instanceof ResponseMap)) || (uri.contains("swagger-resources") || uri.contains("api-docs")) ){
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(t, this.getFeatures());
            byte[] bytes = text.getBytes(this.getCharset());
            out.write(bytes);
        }else{
            super.writeInternal(t, outputMessage);
        }
    }
}
