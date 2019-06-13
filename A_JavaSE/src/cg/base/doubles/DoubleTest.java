package cg.base.doubles;

public class DoubleTest {
	
	
	public static void main(String[] args) {
		//		compare(double d1, double d2) 
		//        姣旇緝涓や釜鎸囧畾鐨� double 鍊笺�� 
		//int compareTo(Double anotherDouble) 


		// test01();
		
		// 浠ｇ爜浼樺寲锛� 
		Double payprice = 10000d;
		Double paypriceVal = payprice == null? 0.0d :payprice;
		 
		
		System.out.println(paypriceVal);
	}

	public static void test01() {
		Double d1 = 2.22d; 
		Double d2 = 2.3d;
		int compare = Double.compare(d1, d2);
		System.out.println(compare);
		int compareTo = d1.compareTo(d2);
		System.out.println(compareTo);
	}
}
