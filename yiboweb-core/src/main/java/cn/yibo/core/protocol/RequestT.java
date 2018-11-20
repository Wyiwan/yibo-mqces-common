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

import cn.yibo.common.codec.AES128Utils;
import cn.yibo.common.codec.HexUtils;
import cn.yibo.common.lang.StringGZIPUtils;
import cn.yibo.common.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.io.Serializable;
import java.util.Map;

/**
 * 描述: http服务请求的数据包装类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-08
 * 版本: v1.0
 */
public class RequestT<T> implements Serializable {
    private static final long serialVersionUID = -2044372424437191905L;

    /**
     * style!=plain时用FastJson转换json串时用到
     */
    public static SimplePropertyPreFilter FILTER = new SimplePropertyPreFilter(RequestT.class, "style", "styledData", "clientInfo");

    /*
    data的处理方式
     */
    private StyleEnum style;

    /*
    请求数据，明文
     */
    private T data;

    /*
    请求数据，style处理后的
     */
    private String styledData;

    /*
    请求头
     */
    private Map<String, Object> clientInfo;

    public RequestT() {
        style = StyleEnum.PLAIN;
    }

    public RequestT(StyleEnum style) {
        this.style = style;
    }

    public StyleEnum getStyle() {
        return style;
    }

    public void setStyle(StyleEnum style) {
        this.style = style;
    }

    public T getData() {
        if(StyleEnum.PLAIN.equals(style)){
            return data;
        }else {
            if (data != null && !"".equals(data.toString())) {
                return data;
            } else {
                //unwrapper data with styled data
                if( !StringUtils.isEmpty(styledData) ){
                    String jsonData = null;
                    if(StyleEnum.GZIP.equals(style)){
                        jsonData = StringGZIPUtils.uncompressToString(HexUtils.HexString2Bytes(styledData));
                    }else if(StyleEnum.AES.equals(style)){
                        try {
                            jsonData = AES128Utils.decrypt(styledData);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(jsonData != null){
                        data = (T) JSON.parse(jsonData);
                        styledData = null;
                        return data;
                    }
                }
            }
        }
        return null;
    }

    public void setData(T data) {
        if(StyleEnum.PLAIN.equals(style)){
            this.data = data;
        }else {
            if(data == null || "".equals(data.toString())){
                this.data = data;
            }else {
                //wrapper data with style
                String jsonData = JSON.toJSONString(data);
                String hexData = null;
                if(StyleEnum.GZIP.equals(style)){
                    hexData = HexUtils.Bytes2HexString(StringGZIPUtils.compressToByte(jsonData));
                }else if(StyleEnum.AES.equals(style)){
                    try {
                        hexData = AES128Utils.encrypt2str(jsonData);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(hexData != null && !"".equals(hexData)){
                    this.styledData = hexData;
                    this.data = null;
                }
            }
        }
    }

    public String getStyledData() {
        return styledData;
    }

    public void setStyledData(String styledData) {
        this.styledData = styledData;
    }

    public Map<String, Object> getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Map<String, Object> clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public String toString() {
        return "RequestT{" +
                "style=" + style +
                ", data=" + data +
                ", styledData='" + styledData + '\'' +
                ", clientInfo=" + clientInfo +
                '}';
    }
}
