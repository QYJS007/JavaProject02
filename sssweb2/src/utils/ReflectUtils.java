package utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ReflectUtils {
	
	/**
	 * 描述:获取类中指定名称的方法 
	 * @author: 李凯昊
	 * @date:2017年8月9日 下午7:46:02
	 * @param className
	 * @param methodName
	 * @return
	 */
	public static Method getUniqueMethod(String className, String methodName){
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("没有找到指定类:"+className);
		}
		Method[] methodArr = clazz.getMethods();
		
		List<Method> list = new ArrayList<Method>();
		for(Method m : methodArr){
			if(m.getName().equals(methodName)){
				list.add(m);
			}
		}
		
		if(list.size()==0){
			throw new RuntimeException("没有找到指定方法,className:"+className+",methodName:"+methodName);
		}else if(list.size()>1){
			throw new RuntimeException("指定方法存在多个重载,className:"+className+",methodName:"+methodName);
		}
		return list.get(0);
	}
	
}
