package cg.model23.flyweight;

public class AdapterTest04 {

	public static void main(String[] args) {

		Address address = new Address();
		DutchAddressAdapter dutchAddressAdapter = new DutchAddressAdapter(address);
		
		//System.out.println(\\\\\\\\\\\\\);
	
	}
}
class Address{
	public void street() {}
	public void zip() {	}
	public void city() {}
}
class DutchAddress{
	public void staat(){};
	public void postcode(){};
	public void plaats(){};
}

class DutchAddressAdapter extends DutchAddress{

	private Address address;
	protected DutchAddressAdapter(Address address) {
		super();
		this.address = address;
	}
	public void staat(){
		
		address.street();
	}
	
	public void postcode(){
		address.zip();
		
	}
	
	public void plaats(){
		
		address.city();
	}
	
	
}
/*
 * 类�?�配器与对象适配器的使用场景�?致，仅仅是实现手段稍有区别，二�?�主要用于如下场景：
 
�?�?�?1）想要使用一个已经存在的类，但是它却不符合现有的接口规范，导致无法直接去访问，这时创建一个�?�配器就能间接去访问这个类中的方法�??
 
�?�?�?2）我们有�?个类，想将其设计为可重用的类（可被多处访问），我们可以创建�?�配器来将这个类来�?�配其他没有提供合�?�接口的类�??
 
�?�?以上两个场景其实就是从两个角度来描述�?类问题，那就是要访问的方法不在合适的接口里，�?个从接口出发（被访问），�?个从访问出发（主动访问）�?
 
接口适配器使用场景：
 
�?�?�?1）想要使用接口中的某个或某些方法，但是接口中有太多方法，我们要使用时必须实现接口并实现其中的�?有方法，可以使用抽象类来实现接口，并不对方法进行实现（仅置空），然后我们再继承这个抽象类来�?�过重写想用的方法的方式来实现�??
这个抽象类就是�?�配器�??

*/