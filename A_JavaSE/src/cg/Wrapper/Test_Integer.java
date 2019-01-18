package cg.Wrapper;

public class Test_Integer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int a = 1; 
		Integer integer = new Integer(300);
		Integer d = new Integer(300);
		Integer c = 300;
		Integer b = 129;

		System.out.println(a==integer);//true
		System.out.println(1==integer);//true

		System.out.println(b==129); //true
		System.out.println(b.equals(129)); //true
		int compareTo = b.compareTo(129);

		System.out.println(compareTo); // 0
		
		
		System.out.println(c == integer);
		System.out.println(d == integer);
	}

}
