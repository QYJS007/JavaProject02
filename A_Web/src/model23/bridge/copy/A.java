package model23.bridge.copy;

public class A {
	public static void main(String[] args) {


		//父类实例可以调用功能和接口
		//功能子类可以调用功能和接口
		Display d1 = new Display(new StringDisplayImpl("Hello world"));
		Display d2 = new CountDisplay(new StringDisplayImpl("Hello china"));
		CountDisplay d3 = new CountDisplay(new StringDisplayImpl("hello guys"));
		d1.display();
		d2.display();
		d3.multiDisplay(3);
/*
 * 总结：将类的这两个层次结构离开有利于独立地对它们进行扩展，虽然使用继承很容易扩展类，但是类之间也形成了一种强关联关系，只要不修改代码，就无法改变这种关系。 
	如果想要很轻松地改变类之间的关系，使用继承就不合适了，因为每次改变类之间关系时都需要修改程序，这时，我们可以使用“委托”来代替“继承”关系。 
	继承是强关联关系，但委托是弱关联关系，这时因为只有Display类的实例生成时，才与作为参数被传入的类构成关联。
 */
	}
}
