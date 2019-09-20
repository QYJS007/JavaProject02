package cg.model23.create.factory01.applicationclass;


public class Circle  implements Shape{

	private XCircle pxc;
	
	public Circle() {
		pxc = new XCircle();
	}

	@Override
	public void display() {
		pxc.displayIt();
	}

}
