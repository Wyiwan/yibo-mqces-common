/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：字典管理模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2019-01-25  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的系统模块
{*****************************************************************************
*/
package cn.yibo.boot.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.common.constant.CacheConstant;
import cn.yibo.boot.modules.base.entity.DictItem;
import cn.yibo.boot.modules.base.entity.DictKind;
import cn.yibo.boot.modules.base.service.DictItemService;
import cn.yibo.boot.modules.base.service.DictKindService;
import cn.yibo.boot.base.entity.TreeBuild;
import cn.yibo.common.constant.StatusEnum;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.core.cache.CacheUtils;
import cn.yibo.core.web.context.SpringContextHolder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 描述: 字典工具类
 * 作者：高云
 * 时间: 2019-01-25
 * 版本: v1.0
 */
public class DictUtils {
    private static DictKindService dictKindService = SpringContextHolder.getBean(DictKindService.class);
    private static DictItemService dictItemService = SpringContextHolder.getBean(DictItemService.class);

    /**
     * 清除字典缓存
     */
    public static void clearDictCache(){
        CacheUtils.remove(CacheConstant.CACHE_DICT_MAP);
    }

    /**
     * 重新加载字典缓存
     * @return
     */
    public static Map<String, List> dictCacheReload(){
        Map<String, List> dictMap = MapUtil.newHashMap();

        List<DictKind> list = dictKindService.findList("status", StatusEnum.N.getCode());
        if( !CollUtil.isEmpty(list) ){
            Iterator<DictKind> iterator = list.iterator();
            while( iterator.hasNext() ){
                dictMap.put(iterator.next().getDictKind(), ListUtils.newArrayList());
            }

            List<DictItem> itemList = dictItemService.findList("i.status", StatusEnum.N.getCode());
            if( !CollUtil.isEmpty(itemList) ){
                Iterator<DictItem> itemIterator = itemList.iterator();

                while( itemIterator.hasNext() ){
                    DictItem dictItem = itemIterator.next();
                    String tmpDictKind = dictItem.getDictKind();

                    List tmpList;
                    if( (tmpList = dictMap.get(tmpDictKind)) != null ){
                        tmpList.add(dictItem);
                    }
                }
            }
        }
        CacheUtils.put(CacheConstant.CACHE_DICT_MAP, dictMap);
        return dictMap;
    }

    /**
     * 根据字典项名称获取字典项键值
     * @param dictKind
     * @param itemName
     * @param defaultValue
     * @return
     */
    public static String getItemValue(String dictKind, String itemName, String defaultValue) {
        if( StrUtil.isNotBlank(dictKind) && StrUtil.isNotBlank(itemName) ){
            List<DictItem> dictItemList = getDictList(dictKind);

            if( !CollUtil.isEmpty(dictItemList) ){
                Iterator<DictItem> iterator = dictItemList.iterator();

                while( iterator.hasNext() ){
                    DictItem dictItem = iterator.next();
                    if( StrUtil.equals(itemName, dictItem.getItemName()) ){
                        return dictItem.getItemValue();
                    }
                }
            }
        }
        return defaultValue;
    }

    /**
     * 根据字典项键值获取字典项名称
     * @param dictKind
     * @param itemValue
     * @param defaultValue
     * @return
     */
    public static String getItemName(String dictKind, String itemValue, String defaultValue) {
        if( StrUtil.isNotBlank(dictKind) && StrUtil.isNotBlank(itemValue) ){
            List<DictItem> dictItemList = getDictList(dictKind);

            if( !CollUtil.isEmpty(dictItemList) ){
                Iterator<DictItem> iterator = dictItemList.iterator();

                while( iterator.hasNext() ){
                    DictItem dictItem = iterator.next();
                    if( StrUtil.equals(itemValue, dictItem.getItemValue()) ){
                        return dictItem.getItemName();
                    }
                }
            }
        }
        return defaultValue;
    }

    /**
     * 根据字典类别获取字典数据(无层级结构)
     * @param dictKind
     * @return
     */
    public static List<DictItem> getDictList(String dictKind){
        Map<String, List> dictMap = (Map<String, List>)CacheUtils.get(CacheConstant.CACHE_DICT_MAP);

        if( dictMap == null ){
            dictMap = dictCacheReload();
        }
        return dictMap.get(dictKind) == null ? ListUtils.newArrayList() : dictMap.get(dictKind);
    }

    /**
     * 根据字典类别获取字典数据(有层级结构)
     * @param dictKind
     * @return
     */
    public static List<DictItem> getDictTreeList(String dictKind){
        List dictList = getDictList(dictKind);
        return dictList == null ? ListUtils.newArrayList() : (List<DictItem>)new TreeBuild(dictList).getTreeList();
    }
}