package model23.bridge;

public class SubClass implements SuperClass<String> {

	public String method(String param) {
        return param;
    }

	/*
	 * 
localhost:mikan mikan$
SubClassֻ������һ�������������ֽ�����Կ���������������
	��һ�����޲εĹ��췽������������Ȼû����ȷ���������Ǳ��������Զ����ɣ���
	�ڶ���������ʵ�ֵĽӿ��еķ�����
	���������Ǳ������Զ����ɵ��Žӷ��������Կ���flags������ACC_BRIDGE��ACC_SYNTHETIC����ʾ�Ǳ������Զ����ɵķ������������ͺͷ���ֵ���Ͷ���Object��
	
	�ٿ�����������ֽ��룬����Object���͵Ĳ���ǿ��ת������String���ͣ��ٵ�����SubClass���������ķ�����ת��������ʵ���ǣ�
	 */
}
