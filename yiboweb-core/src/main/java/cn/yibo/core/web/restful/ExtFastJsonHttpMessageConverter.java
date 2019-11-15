/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：核心模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-10  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的core包
{*****************************************************************************
*/

package cn.yibo.core.web.restful;

import cn.yibo.common.utils.ServletUtils;
import cn.yibo.core.protocol.ResponseMap;
import com.alibaba.fastjson.JSON;
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
        ServletUtils.getResponse().setStatus(200);

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
