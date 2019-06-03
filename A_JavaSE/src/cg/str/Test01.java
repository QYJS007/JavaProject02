package cg.str;

public class Test01 {
	public static void main(String[] args) {

		// showStrSub();
		//getStr();

		/*String ss = "";
		//System.out.println(ss);


		String[] split = ss.split(",");
		System.out.println(split.length);
		for (int i = 0; i < split.length; i++) { 
			System.out.println("111"+split[i]); 
		}*/

		Double payprice = 10000d;
		Double paypriceVal = payprice == null? 0.0d :payprice;
		
		
		System.out.println(paypriceVal);
	}
	/**
	 * String 类型的转化
	 */
	private static void getStr() {
		Object obj = null; 
		System.out.println((String)obj);
		System.out.println(obj.toString());//Exception in thread "main" java.lang.NullPointerException
		//System.out.println((productResultMap.get("surplus_number")) instanceof Integer);
	}

	private static void showStrSub() {
		String reachStationName  = "河北市西二旗站";
		System.out.println("reachStationName 01::"+ reachStationName);
		reachStationName= reachStationName.substring(reachStationName.indexOf("市") + 1);
		System.out.println(reachStationName);
	}

}
