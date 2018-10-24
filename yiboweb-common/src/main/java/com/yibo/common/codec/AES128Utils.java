/*
 * Copyright (c) 2018-2020 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName: yiboweb-framework
 * FileName: AES128Utils.java
 * @author: gogo163gao@163.com
 * @version: 1.0
 */

package com.yibo.common.codec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 描述: 一句话描述该类的用途
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-10
 * 版本: v1.0
 */
public class AES128Utils {
    public static final String BM = "UTF-8";

    /**
     * 密钥算法
     * java6支持56位密钥，bouncycastle支持64位
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加密/解密算法/工作模式/填充方式
     * <p/>
     * JAVA6 支持PKCS5PADDING填充方式
     * Bouncy castle支持PKCS7Padding填充方式
     */
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static String defaultKey = "ah,god is daddy!";//key，可自行修改
    private static String ivParameter = "0392039203920300";//偏移量,可自行修改

    // 加密
    public static String encrypt2str(String data) throws Exception {
        return new String(encrypt(data, defaultKey), BM);
    }

    public static byte[] encrypt(String data) throws Exception {
        return encrypt(data, defaultKey);
    }

    public static byte[] encrypt(String data, String key) throws Exception {
        return encrypt(data, key, ivParameter);
    }

    public static byte[] encrypt(String encData, String key, String vector) throws Exception {
        return encrypt(encData.getBytes(BM), key.getBytes(), vector.getBytes());
    }

    public static byte[] encrypt(byte[] data) throws Exception {
        return encrypt(data, defaultKey.getBytes());
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, ivParameter.getBytes());
    }

    public static byte[] encrypt(byte[] data, byte[] key, byte[] vector) throws Exception {
        if (key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(vector);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        return Base64Code.encodeString(cipher.doFinal(data)).getBytes();
    }

    // 解密
    public static String decrypt(String data) throws Exception {
        return decrypt(data, defaultKey, ivParameter);
    }

    public static String decrypt(String data, String key) throws Exception {
        return decrypt(data, key, ivParameter);
    }

//    public static String decrypt2str(String data) throws Exception {
//        return new String(decrypt(data, defaultKey), BM);
//    }

    public static String decrypt(String data, String key, String ivs) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64Code.decode(data);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, BM);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
}
