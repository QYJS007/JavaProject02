package model23.bridge;

public class SubClass implements SuperClass<String> {

	public String method(String param) {
        return param;
    }

	/*
	 * 
localhost:mikan mikan$
SubClass只声明了一个方法，而从字节码可以看到有三个方法，
	第一个是无参的构造方法（代码中虽然没有明确声明，但是编译器会自动生成），
	第二个是我们实现的接口中的方法，
	第三个就是编译器自动生成的桥接方法。可以看到flags包括了ACC_BRIDGE和ACC_SYNTHETIC，表示是编译器自动生成的方法，参数类型和返回值类型都是Object。
	
	再看这个方法的字节码，它把Object类型的参数强制转换成了String类型，再调用在SubClass类中声明的方法，转换过来其实就是：
	 */
}
