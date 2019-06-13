package model23.bridge;

public class Tests {
	public static void main(String[] args) {
		SuperClass superClass = new SubClass();

		System.out.println(superClass.method("abc123"));// 调用的是实际的方法

		System.out.println(superClass.method(new Object()));// 调用的是桥接
	}
}
