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

package cn.yibo.core.protocol;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.yibo.core.web.exception.BusinessException;
import com.alibaba.fastjson.JSON;

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

    /* 构建加密工具 */
    private SymmetricCrypto AesUtils = new SymmetricCrypto(SymmetricAlgorithm.AES, SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());

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

    public ResponseT(BusinessException businessException){
        this(businessException, false);
    }

    public ResponseT(BusinessException businessException, boolean debug){
        this.retcode = businessException.getErrorCode();
        this.message = businessException.getMsg();
        this.uri = businessException.getUri();
        this.style = StyleEnum.PLAIN;
        if( debug ){
            this.devops = businessException.getDevops();
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

    public void setBizdata(T bizdata){
        if( StyleEnum.PLAIN.equals(style) ){
            this.bizdata = bizdata;
        }else{
            if( bizdata == null || "".equals(bizdata.toString()) ){
                this.bizdata = bizdata;
            }else{
                // wrapper data with style
                String jsonData = JSON.toJSONString(bizdata);
                String hexData = null;

                if( StyleEnum.GZIP.equals(style) ){
                    hexData = HexUtil.encodeHexStr(ZipUtil.gzip(jsonData,"utf-8"));
                }else if( StyleEnum.AES.equals(style) ){
                    hexData = StrUtil.str(AesUtils.encrypt(jsonData),"uft-8");
                }

                if( hexData != null && !"".equals(hexData) ){
                    this.styledata = hexData;
                    this.bizdata = null;
                }
            }
        }
    }

}