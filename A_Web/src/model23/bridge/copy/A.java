package model23.bridge.copy;

public class A {
	public static void main(String[] args) {


		//����ʵ�����Ե��ù��ܺͽӿ�
		//����������Ե��ù��ܺͽӿ�
		Display d1 = new Display(new StringDisplayImpl("Hello world"));
		Display d2 = new CountDisplay(new StringDisplayImpl("Hello china"));
		CountDisplay d3 = new CountDisplay(new StringDisplayImpl("hello guys"));
		d1.display();
		d2.display();
		d3.multiDisplay(3);
/*
 * �ܽ᣺�������������νṹ�뿪�����ڶ����ض����ǽ�����չ����Ȼʹ�ü̳к�������չ�࣬������֮��Ҳ�γ���һ��ǿ������ϵ��ֻҪ���޸Ĵ��룬���޷��ı����ֹ�ϵ�� 
	�����Ҫ�����ɵظı���֮��Ĺ�ϵ��ʹ�ü̳оͲ������ˣ���Ϊÿ�θı���֮���ϵʱ����Ҫ�޸ĳ�����ʱ�����ǿ���ʹ�á�ί�С������桰�̳С���ϵ�� 
	�̳���ǿ������ϵ����ί������������ϵ����ʱ��Ϊֻ��Display���ʵ������ʱ��������Ϊ������������๹�ɹ�����
 */
	}
}
