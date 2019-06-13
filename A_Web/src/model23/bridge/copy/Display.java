package model23.bridge.copy;

public class Display {

	//连接功能层次结构和实现层次结构的桥梁，使用委托这种弱联系的方式
	private DisplayImpl impl;
	public Display(DisplayImpl impl) {
		this.impl = impl;
	}
	
	//利用实现层次类的实例调用其中的方法
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
