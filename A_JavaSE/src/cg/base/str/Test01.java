package cg.base.str;

import org.junit.Test;

public class Test01 {
	public static void main(String[] args) {

		showStrSub();
		//getStr();

		/*String ss = "";
		//System.out.println(ss);


		String[] split = ss.split(",");
		System.out.println(split.length);
		for (int i = 0; i < split.length; i++) { 
			System.out.println("111"+split[i]); 
		}*/

		
	}
	/**
	 * String 类型的转化
	 */
	@Test
	public void getStr() {
		Object obj = null; 
		System.out.println((String)obj);
		System.out.println(obj.toString());//Exception in thread "main" java.lang.NullPointerException
		//System.out.println((productResultMap.get("surplus_number")) instanceof Integer);
	}

	private static void showStrSub() {
		String reachStationName  = "河北市西二旗站";
		System.out.println("reachStationName 01::"+ reachStationName);
		// substring(index); 返回 index 后边的所有的字符串
 		reachStationName= reachStationName.substring(reachStationName.indexOf("市") + 1);
		System.out.println(reachStationName);
	}

}
