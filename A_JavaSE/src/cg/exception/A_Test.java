package cg.exception;

public class A_Test {

	public static void main(String[] args) {
		
		
		int a = 1; 
		
		try {
			System.out.println(a/0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
