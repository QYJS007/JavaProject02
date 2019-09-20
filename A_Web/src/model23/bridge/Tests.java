package model23.bridge;

public class Tests {
	public static void main(String[] args) {
		SuperClass<String> superClass = new SubClass();

		System.out.println(superClass.method("abc123")); 

//		System.out.println(superClass.method(new Object())); 
	}
}
