/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：工具类模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的common包
{*****************************************************************************
*/

package cn.yibo.common.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 描述: GZIP压缩工具
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-07
 * 版本: v1.0
 */
public class StringGZIPUtils {
    private static String encode = "utf-8";

    /**
     * 字符串压缩为字节数组
     * @param str
     * @param encoding
     * @return
     */
    public static byte[] compressToByte(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 字符串压缩为字节数组, 使用默认UTF-8编码
     * @param str
     * @return
     */
    public static byte[] compressToByte(String str) {
        return compressToByte(str, encode);
    }

    /**
     * 字节数组解压缩后返回字符串
     * @param b
     * @param encoding
     * @return
     */
    public static String uncompressToString(byte[] b, String encoding) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节数组解压缩后返回字符串, 使用默认UTF-8编码
     * @param b
     * @return
     */
    public static String uncompressToString(byte[] b) {
        return uncompressToString(b, encode);
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        StringGZIPUtils.encode = encode;
    }
}
