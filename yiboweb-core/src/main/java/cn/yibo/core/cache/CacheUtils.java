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

package cn.yibo.core.cache;

import java.util.List;

/**
 * 描述: 缓存工具类
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-13
 * 版本: v1.0
 */
public class CacheUtils {
    private static ICache defaultCacheFactory = new EhcacheFactory();

    private static final String SYS_CACHE = "sysCache";

    public static void put(String cacheName, Object key, Object value) {
        defaultCacheFactory.put(cacheName, key, value);
    }

    public static <T> T get(String cacheName, Object key) {
        return defaultCacheFactory.get(cacheName, key);
    }

    public static <T> T get(String cacheName, Object key, ILoader iLoader) {
        return defaultCacheFactory.get(cacheName, key, iLoader);
    }

    public static <T> T get(String cacheName, Object key, Class<? extends ILoader> iLoaderClass) {
        return defaultCacheFactory.get(cacheName, key, iLoaderClass);
    }

    @SuppressWarnings("rawtypes")
    public static List getKeys(String cacheName) {
        return defaultCacheFactory.getKeys(cacheName);
    }

    public static void remove(String cacheName, Object key) {
        defaultCacheFactory.remove(cacheName, key);
    }

    public static void removeAll(String cacheName) {
        defaultCacheFactory.removeAll(cacheName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //--------- SYS_CACHE  ----------
    //------------------------------------------------------------------------------------------------------------------
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    public static List getKeys() {
        return defaultCacheFactory.getKeys(SYS_CACHE);
    }
}
