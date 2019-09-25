package utils;

import java.util.Map;

import ognl.Ognl;

public class OgnlUtils {
	
	/**
	 * 执行ognl表达式,返回结果
	 * @author likaihao
	 * @date 2016年7月13日 下午4:33:38
	 * @param expression
	 * @param paramMap
	 * @return
	 */
	public static Object getValue(String expression, Map<String,Object> paramMap){
		try {
			return Ognl.getValue(expression, paramMap);
		} catch (Exception e) {
			throw new RuntimeException("执行表达式出错:"+expression,e);
		}
	}
}