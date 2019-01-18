package cg.str;

import java.util.HashMap;
import java.util.Map;

public class Test02 {

	public static void main(String[] args) {
		// testInteger();

		/*String expressNo = null; 
		Integer invoicetype = 1;

		//发票类型(纸质0 /电子1 )
		if(expressNo !=null&&"0".equals(invoicetype)){
			//Logger.applogErrorDetail(null, null, "确认开票", "发票确认开票,纸质票订单号不能为空");
			//return new Result(false, "纸质票,订单号不能为空");
		System.out.println("执行了"+invoicetype);*/

		/*	String ss  = "2131"; 
		//String ss  = :; 
		String[] split = ss.split(",");
	//	System.out.println(split.length);
		//for (String string : split) {
			System.out.println(split[0]);
		//}
		 */		
		
		String[] notificationurlbyte = null;
		// 满足前者就  不 运行 后者 notificationurlbyte.length < 1
		if(notificationurlbyte  == null || notificationurlbyte.length < 1){
			
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		}else{
			System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnn");
		}
		
		
		/*Integer dug  = null;
		 *不  满足前者    就  不 运行 后者 2==dug
		if(dug!=null&&2==dug){
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		}else{
			System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnn");
		}*/

		Integer c =0;
		System.out.println(Integer.valueOf("0").equals(c ));

		Integer a = 200;
		Integer b = 200;
		/*if(a==b){
			System.out.print("a等于b");  
		}else{
			System.out.print("a不等于b");  
		}*/
		if(a.intValue() == b.intValue()){
			System.out.print("a等于b");  
		}else{
			System.out.print("a不等于b");  
		}
	}

	public static void testInteger() {
		Map  resultInvoiceMap = new HashMap<String, Object>();
		for (int i = 0; i < 5; i++) {
			//行程数量
			Integer  tripnum = (Integer) resultInvoiceMap.get("tripnum");
			//Integer  tripnum = 0;
			if(tripnum!=null){
				tripnum = ++tripnum;
			}else{
				tripnum = 0; 
			}
			resultInvoiceMap.put("tripnum", tripnum);
			System.out.println(tripnum);
		}
	}
}
