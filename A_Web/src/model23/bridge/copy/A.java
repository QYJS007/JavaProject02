package model23.bridge.copy;

public class A {
	public static void main(String[] args) {

		Display d1 = new Display(new StringDisplayImpl("Hello world"));
		Display d2 = new CountDisplay(new StringDisplayImpl("Hello china"));
		CountDisplay d3 = new CountDisplay(new StringDisplayImpl("hello guys"));
		d1.display();
		d2.display();
		d3.multiDisplay(3);
 
	}
}
