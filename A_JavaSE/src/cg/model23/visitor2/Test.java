package cg.model23.visitor2;

import java.io.File;


/**
 * 访问者模式的测试类
 * @author wangXgnaw
 *
 */
public class Test {
	public static void main(String[] args) {
		Lunchbox lunchbox = new Lunchbox();

		lunchbox.Attach(new RoastDuck(1));
		lunchbox.Attach(new Meal(2));
		lunchbox.Attach(new Soup(1));

		lunchbox.Accept(new NormalVisitor());
		
		File file = new File("");
		
		
	}
}
/**
 * 
 * visitor模式的一大特点就是，结构是固定死的，你是不能改的，但你可以改一些结构的实现方式，即上述实例中的usb接口，
 * 你可以换成其他接口比如PCI……只要实现规定好的硬件接口interface类即可，这样一来，任凭怎么扩展，都不会修改到底层的结构，不会损坏“框架”了。

不过，这也算是最大的缺点。就是不能扩展里面的内容，就上述内容来说，只能扩展接口，而不能扩展里面硬件，或者修改电脑硬件的实现方式！
 */
