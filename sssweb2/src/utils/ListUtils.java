package utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//常用方法工具类
public class ListUtils {

	/**
	 * 将list中的对象按 指定属性进行排序
	 * @author likaihao
	 * @date 2016年10月27日 上午11:07:00
	 * @param list
	 * @param sortField
	 * @param asc 是否是升序排列
	 */
	public static <T> void sort(List<T> list, String sortAttr, final boolean asc){
		sort(list, sortAttr, null, asc);
	}
	
	/**
	 * 将list中的对象按 指定属性进行排序,按valueOrderBy指定的顺序
	 * @author likaihao
	 * @date 2016年12月20日 上午9:58:26
	 * @param list
	 * @param sortAttr
	 * @param valueOrderBy
	 */
	public static <T> void sort(List<T> list, String sortAttr, final List<Object> valueOrderBy){
		sort(list, sortAttr, valueOrderBy, true);
	}
	
	/**
	 * 将list中的对象按 指定属性进行排序
	 * @author likaihao
	 * @date 2016年10月27日 上午11:07:00
	 * @param list
	 * @param sortField 要排序的list中对象的属性,如果为null表示按对象本身排序
	 * @param valueOrderBy 指定排序的list
	 * @param asc 是否是升序排列
	 */
	private static <T> void sort(List<T> list, String sortAttr, final List<Object> valueOrderBy, final boolean asc){
		if(list==null || list.size()==0){
			return;
		}
		
		//获取指定方法
		Method method2 = null;
		if(sortAttr!=null){
			String methodName = StringUtils.getGetMethodName(sortAttr);
			try {
				method2 = list.get(0).getClass().getMethod(methodName);
			} catch (Exception e) {
				LoggerUtils.error("没有找到指定字段,class:"+list.get(0).getClass().getName()+",methodName:"+methodName);
				return;
			}
		}
		
		final Method method = method2;
		
		//排序
		Collections.sort(list,new Comparator<T>(){
			@SuppressWarnings("unchecked")
			public int compare(T o1, T o2) {
				int result = 0;
				try {
					//按指定属性值进行排序
					Object value1 = o1;
					Object value2 = o2;
					if(method!=null){
						method.setAccessible(true);
						value1 = method.invoke(o1);
						value2 = method.invoke(o2);
					}
					
					//如果存在指定顺序,则按指定顺序进行排序
					if(valueOrderBy!=null){
						if(valueOrderBy.contains(value1) && valueOrderBy.contains(value2)){
							return valueOrderBy.indexOf(value1) - valueOrderBy.indexOf(value2);
						}else if(valueOrderBy.contains(value2)){
							return 1;
						}else{
							return -1;
						}
					}
					
					result = ((Comparable<Object>)value1).compareTo(value2);
					
					//如果不是升序,则反转结果
					if(!asc){
						result = -1 * result;
					}
				} catch (Exception e) {
					LoggerUtils.error("排序失败:"+e.getMessage());
				}
				return result;
			}
		});
	}
	
	
	/**
	 * 清除list中对象的指定属性的值
	 * @author likaihao
	 * @date 2016年11月26日 下午5:42:25
	 * @param list
	 * @param keepAttrArr
	 */
	public static <T> void clearListValAttr(List<T> list, String[] keepAttrArr){
		if(list==null || list.size()==0){
			return;
		}
		
		//获取类中的要清理的属性的set方法
		Map<String,Method> methodMap = new HashMap<String,Method>();
		List<String> keepAttrList = Arrays.asList(keepAttrArr);
		Method[] methodArr = list.get(0).getClass().getMethods();
		for(Method method : methodArr){
			if(method.getName().startsWith("set")){
				String attrName = StringUtils.getAttrName(method.getName());
				if(!keepAttrList.contains(attrName)){
					methodMap.put(attrName, method);
				}
			}
		}
		
		for(T t : list){
			for(Method method : methodMap.values()){
				try {
					method.invoke(t, new Object[]{null});
				} catch (Exception e) {
					LoggerUtils.error("调用set方法失败,attrName:"+StringUtils.getAttrName(method.getName()));
				}
			}
		}
	}
}