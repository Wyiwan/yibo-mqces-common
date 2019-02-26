/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：公用模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-07  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的common包
{*****************************************************************************
*/

package cn.yibo.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * List工具类
 * @version 2018-08-01
 */
@SuppressWarnings("rawtypes")
public class ListUtils{
	/**
     * 是否包含字符串
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */ 	
    public static boolean inString(String str, List<String> strs){
		if (str != null && strs != null){
        	for (String s : strs){
        		if (str.equals(StrUtil.trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
    }
    
	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}
	
	@SafeVarargs
	public static <E> ArrayList<E> newArrayList(E... elements) {
		ArrayList<E> list = new ArrayList<E>(elements.length);
		Collections.addAll(list, elements);
		return list;
	}
	
	public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
		return (elements instanceof Collection) ? new ArrayList<E>(cast(elements)) : newArrayList(elements.iterator());
	}
	
	public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
		ArrayList<E> list = newArrayList();
		addAll(list, elements);
		return list;
	}
	
	public static <E> LinkedList<E> newLinkedList() {
		return new LinkedList<E>();
	}
	
	public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
		LinkedList<E> list = newLinkedList();
		addAll(list, elements);
		return list;
	}
	
	public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
		return new CopyOnWriteArrayList<E>();
	}
	
	public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
		Collection<? extends E> elementsCollection = (elements instanceof Collection)
				? cast(elements) : newArrayList(elements);
		return new CopyOnWriteArrayList<E>(elementsCollection);
	}
	
	private static <T> Collection<T> cast(Iterable<T> iterable) {
		return (Collection<T>) iterable;
	}

	private static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
		boolean wasModified = false;
		while (iterator.hasNext()) {
			wasModified |= addTo.add(iterator.next());
		}
		return wasModified;
	}

	public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
		if (elementsToAdd instanceof Collection) {
			Collection<? extends T> c = cast(elementsToAdd);
			return addTo.addAll(c);
		}
		return addAll(addTo, elementsToAdd.iterator());
	}
	
	/**
	 * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
	 * @param collection 来源集合.
	 * @param keyPropertyName 要提取为Map中的Key值的属性名.
	 * @param valuePropertyName 要提取为Map中的Value值的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap(collection.size());
		try {
			for (Object obj : collection) {
				map.put(BeanUtil.getProperty(obj, keyPropertyName), BeanUtil.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
		return map;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> extractToList(final Collection collection, final String propertyName) {
		if (collection == null){
			return newArrayList();
		}
		List list = new ArrayList(collection.size());
		try {
			for (Object obj : collection) {
				list.add(BeanUtil.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
		return list;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param prefix 符合前缀的信息(为空则忽略前缀)
	 * @param isNotBlank 仅包含不为空值(空字符串也排除)
	 */
	public static List<String> extractToList(final Collection collection, final String propertyName, 
			final String prefix, final boolean isNotBlank) {
		List<String> list = new ArrayList<String>(collection.size());
		try {
			for (Object obj : collection) {
				String value = (String)BeanUtil.getProperty(obj, propertyName);
				if( StrUtil.isEmpty(prefix) || StrUtil.startWith(value, prefix) ){
					if (isNotBlank){
						if (StrUtil.isNotBlank(value)){
							list.add(value);
						}
					}else{
						list.add(value);
					}					
				}				
			}
		} catch (Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
		return list;
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return StrUtil.join(separator, list);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
	 */
	public static String convertToString(final Collection collection, final String separator) {
		return StrUtil.join(separator, collection);
	}

	/**
	 * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
	 */
	public static String convertToString(final Collection collection, final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}

	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * 判断是否为不为空.
	 */
	public static boolean isNotEmpty(Collection collection) {
		return !(isEmpty(collection));
	}

	/**
	 * 取得Collection的第一个元素，如果collection为空返回null.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		return collection.iterator().next();
	}

	/**
	 * 获取Collection的最后一个元素 ，如果collection为空返回null.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		//当类型为List时，直接取得最后一个元素 。
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}
		//其他类型通过iterator滚动到最后一个元素.
		Iterator<T> iterator = collection.iterator();
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * 返回a+b的新List.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	/**
	 * 返回a-b的新List.
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (T element : b) {
			list.remove(element);
		}
		return list;
	}

	/**
	 * 返回a与b的交集的新List.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();
		for (T element : a) {
			if (b.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}

}
