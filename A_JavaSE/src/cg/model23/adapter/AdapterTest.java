package cg.model23.adapter;

public class AdapterTest {

	public static void main(String[] args) {
		/*
		 * 适配器就是一种适配中间件，它存在于不匹配的二者之间，用于连接二者，
		 * 将不匹配变得匹配，简单点理解就是平常所见的转接头，转换器之类的存在。
		 * 适配器模式有两种：类适配器、对象适配器、接口适配器
		 * 前二者在实现上有些许区别，作用一样，第三个接口适配器差别较大。
		 * 1、类适配器模式：
		 *		原理：通过继承来实现适配器功能。
		 *		当我们要访问的接口A中没有我们想要的方法 ，却在另一个接口B中发现了合适的方法，
		 *		我们又不能改变访问接口A，在这种情况下，我们可以定义一个适配器p来进行中转，
		 *		这个适配器p要实现我们访问的接口A，这样我们就能继续访问当前接口A中的方法
		 *		（虽然它目前不是我们的菜），然后再继承接口B的实现类BB，
		 *
		 *		这样我们可以在适配器P中访问接口B的方法了，这时我们在适配器P中的接口A方法中直接引用BB中的合适方法，
		 *		这样就完成了一个简单的类适配器。

　　				详见下方实例：我们以ps2与usb的转接为例

		 */
		// Ps2 接口 使用 usb 接口
		Ps2 p = new Adapter();
		p.isPs2();

	}
}

interface Ps2 {
	void isPs2();
}
interface Usb {
	void isUsb();
}

class Usber implements Usb {
	@Override
	public void isUsb() {
		System.out.println("USB口");
	}
}


class Adapter extends Usber implements Ps2 {
	@Override
	public void isPs2() {
		isUsb();
	}

}