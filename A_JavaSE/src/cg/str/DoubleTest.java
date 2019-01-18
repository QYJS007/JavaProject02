package cg.str;

public class DoubleTest {

	public static void main(String[] args) {
		//		compare(double d1, double d2) 
		//        比较两个指定的 double 值。 
		//int compareTo(Double anotherDouble) 


		Double d1 = 2.22d; 
		Double d2 = 2.3d;
		int compare = Double.compare(d1, d2);
		System.out.println(compare);
		int compareTo = d1.compareTo(d2);
		System.out.println(compareTo);
	}
}
