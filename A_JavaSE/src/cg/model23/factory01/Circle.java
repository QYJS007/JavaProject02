package cg.model23.factory01;

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
