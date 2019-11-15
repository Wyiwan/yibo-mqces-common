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

package cn.yibo.core.protocol.filter;

import cn.yibo.core.protocol.RequestT;
import cn.yibo.core.protocol.ResponseT;
import com.alibaba.fastjson.serializer.PropertyFilter;
import cn.yibo.core.protocol.StyleEnum;

/**
 * 描述: 一句话描述该类的用途
 * 作者：高云
 * 邮箱: gogo163gao@163.com
 * 时间: 2018-08-08
 * 版本: v1.0
 */
public class ExtPropertyFilter implements PropertyFilter {
    public static String[] filterParams = new String[]{"data", "bizdata"};

    @Override
    public boolean apply(Object object, String name, Object value) {
        if (object instanceof RequestT) {
            RequestT re = (RequestT) object;
            StyleEnum style = re.getStyle();
            if (!StyleEnum.PLAIN.equals(style) && contains(name)) {
                return false;
            }
        }
        if (object instanceof ResponseT) {
            ResponseT re = (ResponseT) object;
            StyleEnum style = re.getStyle();
            if (!StyleEnum.PLAIN.equals(style) && contains(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean contains(String name){
        for(String filerParam : filterParams){
            if(filerParam.equals(name)){
                return true;
            }
        }
        return false;
    }
}
