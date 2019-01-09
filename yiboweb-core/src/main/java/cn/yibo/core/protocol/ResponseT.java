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
import cn.yibo.core.web.exception.BizException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述: http服务返回的数据包装类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@Data
public class ResponseT<T> implements Serializable {
    // 返回的响应码为000000，说明是正常返回
    private String retcode;

    // 错误信息 有业务异常的时候，来源于BizException；否则（系统异常），使用通用异常
    private String message;

    // 错误说明url 有业务异常的时候，来源于BizException；否则（系统异常），使用通用异常
    private String uri;

    // 返回的业务 有业务异常的时候，来源于BizException；否则（系统异常），使用通用异常
    private T bizdata;

    // data的处理方式
    private StyleEnum style;

    // style处理后的返回数据
    private String styledata;

    // 是否调试模式
    @JSONField(serialize = false)
    private boolean debug;

    // 错误堆栈信息，便于排查问题，用于记录日志
    @JSONField(serialize = false)
    private String exception;

    // 错误堆栈信息，便于排查问题 ，正常是调试模式下该字段才返回信息
    private String devinfo;

    // 时间戳
    private long timestamp = System.currentTimeMillis();

    /**
     * 构造方法
     */
    public ResponseT(){
        this.style = StyleEnum.PLAIN;
    }

    /**
     * 构造方法
     * @param style
     */
    public ResponseT(StyleEnum style){
        this.style = style;
    }

    /**
     * 构造方法
     * @param returnCodeEnum
     */
    public ResponseT(ReturnCodeEnum returnCodeEnum){
        this.retcode = returnCodeEnum.getCode();
        this.message = returnCodeEnum.getDesc();
        this.style = StyleEnum.PLAIN;
    }

    /**
     * 构造方法
     * @param returnCodeEnum
     * @param exception
     * @param debug
     */
    public ResponseT(ReturnCodeEnum returnCodeEnum, String exception, boolean debug){
        this(returnCodeEnum);
        this.exception = exception;
        this.debug = debug;
    }

    /**
     * 构造方法
     * @param bizException
     * @param debug
     */
    public ResponseT(BizException bizException, boolean debug){
        this.debug = debug;
        this.style = StyleEnum.PLAIN;
        this.uri = bizException.getUri();
        this.retcode = bizException.getErrorCode();
        this.message = bizException.getMessage();
        this.exception = bizException.getErrorInfo();
    }

    public void setBizdata(T bizdata){
        if( StyleEnum.PLAIN.equals(style) ){
            this.bizdata = bizdata;
        }else{
            SymmetricCrypto AesUtils = new SymmetricCrypto(SymmetricAlgorithm.AES, SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());

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