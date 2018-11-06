/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: ResponseT.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package cn.yibo.core.protocol;

import cn.yibo.common.codec.AES128Utils;
import cn.yibo.common.codec.HexUtils;
import cn.yibo.common.lang.StringGZIPUtils;
import com.alibaba.fastjson.JSON;
import cn.yibo.core.web.exception.BizException;

import java.io.Serializable;

/**
 * 描述: http服务返回的数据包装类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public class ResponseT<T> implements Serializable {
    /** 返回的响应码为000000，说明是正常返回 */
    private String retcode;

    /** 错误信息 有业务异常的时候，来源于BizException；否则（系统异常），使用通用异常 */
    private String message;

    /** 错误堆栈信息，便于排查问题   正常是调试模式下该字段才返回信息 */
    private String devops;

    /** 错误说明url 有业务异常的时候，来源于BizException；否则（系统异常），使用通用异常 */
    private String uri;

    /** 时间戳 */
    private long timestamp = System.currentTimeMillis();

    /** 返回的业务 有业务异常的时候，来源于BizException；否则（系统异常），使用通用异常 */
    private T bizdata;

    /** data的处理方式 */
    private StyleEnum style;

    /* style处理后的返回数据 */
    private String styledata;

    public ResponseT(){
        this.style = StyleEnum.PLAIN;
    }

    public ResponseT(StyleEnum style) {
        this.style = style;
    }

    public ResponseT(ReturnCodeEnum returnCodeEnum){
        this.retcode = returnCodeEnum.getCode();
        this.message = returnCodeEnum.getDesc();
        this.style = StyleEnum.PLAIN;
    }

    public ResponseT(ReturnCodeEnum returnCodeEnum, String devops, boolean debug){
        this(returnCodeEnum);
        if( debug ) {
            this.devops = devops;
        }
    }

    public ResponseT(BizException bizException){
        this(bizException, false);
    }

    public ResponseT(BizException bizException, boolean debug){
        this.retcode = bizException.getErrorCode();
        this.message = bizException.getMsg();
        this.uri = bizException.getUri();
        this.style = StyleEnum.PLAIN;
        if( debug ){
            this.devops = bizException.getDevops();
        }
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public StyleEnum getStyle() {
        return style;
    }

    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    public String getDevops() {
        return devops;
    }

    public void setDevops(String devops) {
        this.devops = devops;
    }

    public String getStyledata() {
        return styledata;
    }

    public void setStyledata(String styledata) {
        this.styledata = styledata;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getBizdata() {
        return bizdata;
    }

    public void setBizdata(T bizdata) {
        if( StyleEnum.PLAIN.equals(style) ){
            this.bizdata = bizdata;
        }else {
            if( bizdata == null || "".equals(bizdata.toString()) ){
                this.bizdata = bizdata;
            }else {
                //wrapper data with style
                String jsonData = JSON.toJSONString(bizdata);
                String hexData = null;
                if(StyleEnum.GZIP.equals(style)){
                    hexData = HexUtils.Bytes2HexString(StringGZIPUtils.compressToByte(jsonData));
                }else if(StyleEnum.AES.equals(style)){
                    try {
                        hexData = AES128Utils.encrypt2str(jsonData);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if( hexData != null && !"".equals(hexData) ){
                    this.styledata = hexData;
                    this.bizdata = null;
                }
            }
        }
    }

}