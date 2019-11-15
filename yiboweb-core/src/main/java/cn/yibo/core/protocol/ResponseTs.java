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

import cn.hutool.core.util.StrUtil;
import cn.yibo.core.web.exception.BizException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * 描述: ResponseT 工具类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.0
 */
@Slf4j
public class ResponseTs{
    /**
     * 成功返回
     * @return
     */
    public static <T> ResponseT newOK(){
        return new ResponseT(ReturnCodeEnum.SUCCESS);
    }

    /**
     * 正常业务对象的返回
     * @param bizData
     * @return
     */
    public static <T> ResponseT newResponse(T bizData){
        ResponseT<T> responseT = new ResponseT(ReturnCodeEnum.SUCCESS);
        responseT.setBizdata(bizData);
        return responseT;
    }

    /**
     * @param bizException
     * @param debug  是否调试模式  true 调试模式，返回详细的错误堆栈信息
     * @param <T>
     * @return
     */
    public static <T> ResponseT<T> newResponseException(BizException bizException, boolean debug){
        return new ResponseT<T>(bizException, debug);
    }

    /**
     * 业务异常返回
     * @return
     */
    public static <T> ResponseT<T> newResponseException(BizException bizException){
        return newResponseException(bizException, StrUtil.isNotBlank(bizException.getErrorInfo()));
    }

    /**
     * 返回未知异常
     * @return
     */
    public static <T> ResponseT newUnknow(){
        return new ResponseT(ReturnCodeEnum.UNKNOW);
    }

    /**
     * 返回网络异常
     * @return
     */
    public static <T> ResponseT newNetError(){
        return new ResponseT(ReturnCodeEnum.NET_ERROR);
    }

    /**
     * 返回服务异常
     * @return
     */
    public static <T> ResponseT newServerError(){
        return new ResponseT(ReturnCodeEnum.SERVER_ERROR);
    }

    /**
     * 返回请求异常
     * @return
     */
    public static <T> ResponseT newRequestError(){
        return new ResponseT(ReturnCodeEnum.REQUEST_ERROR);
    }

    /**
     * 返回数据校验异常
     * @return
     */
    public static <T> ResponseT<T> newValidateError(){
        return new ResponseT<T>(ReturnCodeEnum.VALIDATE_ERROR);
    }

    /**
     * 使用response直接输出业务数据
     * @param response
     * @param bizData
     */
    public static void outResponse(ServletResponse response, Object bizData){
        PrintWriter out = null;
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            ResponseT<T> responseT = ResponseTs.newResponse(bizData);
            out = response.getWriter();
            out.println(JSON.toJSONString(responseT));
        }catch(Exception e){
            log.error(e + "输出JSON出错");
        }finally{
            if( out!=null ){
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 使用response直接输出业务异常信息
     * @param response
     * @param ex
     */
    public static void outResponseException(ServletResponse response, BizException ex){
        PrintWriter out = null;
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            ResponseT<T> responseT = ResponseTs.newResponseException(ex);
            out = response.getWriter();
            out.println(JSON.toJSONString(responseT));
        }catch(Exception e){
            log.error(e + "输出JSON出错");
        }finally{
            if( out!=null ){
                out.flush();
                out.close();
            }
        }
    }
}
