package utils;

import java.lang.reflect.ParameterizedType;

/**
 * 泛型工具类
 * @author Administrator
 *
 */
public class TUtils {

	/**
	 * 泛型转换:返回指定类 父类第一个类型参数的class对象
	 * @param entity 泛型父类的子类class对象
	 * @return
	 */
	@SuppressWarnings("all")
	public static <T> Class<T> getTGenericSuperClass(Class entity) {
		try {
			ParameterizedType parameterizedType = (ParameterizedType)entity.getGenericSuperclass();
			Class<T> entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
			return entityClass;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
