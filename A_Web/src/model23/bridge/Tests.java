package model23.bridge;

public class Tests {
	public static void main(String[] args) {
		SuperClass superClass = new SubClass();

		System.out.println(superClass.method("abc123"));// ���õ���ʵ�ʵķ���

		System.out.println(superClass.method(new Object()));// ���õ����Ž�
	}
}
