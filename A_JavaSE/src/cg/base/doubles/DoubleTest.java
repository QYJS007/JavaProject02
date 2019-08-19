package cg.base.doubles;

import org.junit.Test;

public class DoubleTest {
	
	
	public static void main(String[] args) {
		Double payprice = 10000d;
		Double paypriceVal = payprice == null? 0.0d :payprice;
		System.out.println(paypriceVal); //10000.0 
	}
	@Test
	public  void test01() {
		Double d1 = 2.22d; 
		Double d2 = 2.3d;
		int compare = Double.compare(d1, d2); // -1
		System.out.println(compare);
		int compareTo = d1.compareTo(d2);
		System.out.println(compareTo); // -1
	}
}
