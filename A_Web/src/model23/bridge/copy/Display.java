package model23.bridge.copy;

public class Display {

	//���ӹ��ܲ�νṹ��ʵ�ֲ�νṹ��������ʹ��ί����������ϵ�ķ�ʽ
	private DisplayImpl impl;
	public Display(DisplayImpl impl) {
		this.impl = impl;
	}
	
	//����ʵ�ֲ�����ʵ���������еķ���
	public void open(){
		impl.rawOpen();
	}
	
	public void print(){
		impl.rawPrint();
	}
	
	public void close(){
		impl.rawClose();
	}
	
	public void display(){
		open();
		print();
		close();
	}

}