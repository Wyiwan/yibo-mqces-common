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

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.yibo.core.protocol.*;
import cn.yibo.core.protocol.filter.ExtPropertyFilter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 描述: 一句话描述该类的用途
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public class FastJsonHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {
    public final static Charset UTF8 = Charset.forName("UTF-8");

    private Charset charset  = UTF8;

    private SerializerFeature[] features = new SerializerFeature[0];

    private ITypeReference iTypeReference;

    private String dataStyleType;

    private SymmetricCrypto AesUtils = new SymmetricCrypto(SymmetricAlgorithm.AES, SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());

    public FastJsonHttpMessageConverter(){
        super(new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8));
        features = new SerializerFeature[1];
        features[0] = SerializerFeature.DisableCircularReferenceDetect;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public SerializerFeature[] getFeatures() {
        return features;
    }

    public void setFeatures(SerializerFeature... features) {
        this.features = features;
    }

    public ITypeReference getiTypeReference() {
        return iTypeReference;
    }

    public void setiTypeReference(ITypeReference iTypeReference) {
        this.iTypeReference = iTypeReference;
    }

    public String getDataStyleType() {
        return dataStyleType;
    }

    public void setDataStyleType(String dataStyleType) {
        this.dataStyleType = dataStyleType;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
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
        //String url = ((ServletServerHttpRequest) inputMessage).getURI().getPath();
        String url = ((ServletServerHttpRequest)((ServletServerHttpRequest) inputMessage)).getServletRequest().getServletPath();
        //支持 url包含？参数
        int size = url.indexOf("?");
        if(size != -1){
            url = url.substring(0, size);
        }

        T t = JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(), iTypeReference.getTypeReference(url).getType());

        //请求数据是RequestT对象时styledData处理
        t = resetRequestData(url, t);
        return t;
    }

    @Override
    protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException{
        ResponseT<T> response = null;
        if( t instanceof ResponseErrorMap){
            response = (ResponseT)((ResponseErrorMap)t).get("responseT");
            if( response.isDebug() ) response.setDevop(response.getException());
        }else{
            response = ResponseTs.newResponse(t);
        }

        // 响应数据是ResponseT对象时style处理
        resetResponseData(response);

        OutputStream out = outputMessage.getBody();
        String text = JSON.toJSONString(response, getSerializeFilter(response), features);
        byte[] bytes = text.getBytes(charset);
        out.write(bytes);
    }

    // 获取序列化过滤器
    private SerializeFilter[] getSerializeFilter(ResponseT<T> response){
        if(response.getStyle() != null && !StyleEnum.PLAIN.equals(response.getStyle())){
            return SerializeFilterBuilder.instance;
        }
        return new SerializeFilter[0];
    }

    private static class SerializeFilterBuilder{
        private static SerializeFilter[] instance = new SerializeFilter[]{new ExtPropertyFilter()};
    }

    /**
     * 请求中的数据解密并设置到业务对象<br/>
     */
    private T resetRequestData(String url, T t){
        if( t instanceof RequestT ){
            RequestT requestT = (RequestT)t;

            if( requestT.getStyle() != null && !StyleEnum.PLAIN.equals(requestT.getStyle()) ){
                Type[] types = ((ParameterizedTypeImpl)iTypeReference.getTypeReference(url).getType()).getActualTypeArguments();
                Type type = ArrayUtil.isNotEmpty(types) ? types[0] : null;

                if( type != null ){
                    Object data = requestT.getData();
                    if( requestT.getData() instanceof JSONObject ){
                        data = JSON.parseObject(((JSONObject) requestT.getData()).toJSONString(), type);
                    }

                    StyleEnum style = requestT.getStyle();
                    requestT.setStyle(StyleEnum.PLAIN);
                    requestT.setData(data);
                    requestT.setStyledData(null);
                    requestT.setStyle(style);
                    return (T)requestT;
                }
            }
        }
        return t;
    }

    /**
     * 响应数据是ResponseT对象时处理<br/>
     * bizData --> styledData
     * @param response
     */
    private void resetResponseData(ResponseT<T> response){
        if( response.getBizdata() != null ){
            StyleEnum style = StyleEnum.codeOf(dataStyleType);

            if( style != null && !StyleEnum.PLAIN.equals(style) ){
                response.setStyle(style);
                String jsonData = JSON.toJSONString(response.getBizdata());
                String styledData = null;

                if( StyleEnum.GZIP.equals(style) ){
                    styledData = HexUtil.encodeHexStr(ZipUtil.gzip(jsonData,"utf-8"));
                }else if( StyleEnum.AES.equals(style) ){
                    styledData = StrUtil.str(AesUtils.encrypt(jsonData),"uft-8");
                }

                if( !StrUtil.isEmpty(styledData) ){
                    response.setStyledata(styledData);
                    response.setBizdata(null);
                }
            }
        }
    }

}