package cg.integer;

public class CopyOfIntegerTest {

	public static void main(String[] args) {

		Integer datetype =1;
		Integer datetype2 =128;
		System.out.println(new Integer(1).equals(datetype));// true 

		System.out.println(new Integer(128).equals(datetype2));// true 
		System.out.println(new Integer(128)==datetype2);// true 


		Integer a=128;
		Integer b=128;
		Integer c=127;
		Integer d=127;
		System.out.println(a==b);//false 
		System.out.println(a.equals(b));//true
		System.out.println(a==Integer.valueOf(128));//false
		System.out.println(c==Integer.valueOf(127));//true
		System.out.println(c==d);//true
		System.out.println(c.equals(d));//true
		
		
		/*
		 * Java常量池
 
常量池在java中用于保存编译期已经确定的，它包括了关于类，方法，接口中的常量，也包括字符串常量。例如
 
String s = "Java" 这种声明的方式。产生的这种"常量"就会被放到常量池，常量池是JVM的一块特殊的内存空间。
 
使用Java常量池技术，是为了方便快捷地创建某些对象，当你需要一个对象时候，就去这个池子里面找，找不到就在池子里面创建一个。但是必须注意如果对象是用new 创建的。那么不管是什么对像，它是不会放到池子里的，而是向堆申请新的空间存储。
 
java中基本类型的包装类的大部分都实现了常量池技术，这些类是Byte,Short,Integer,Long,Character,Boolean,另外两种浮点数类型的包装类则没有实现。另外Byte,Short,Integer,Long,Character这5种整型的包装类也只是在对应值小于等于127时才可使用对象池。超过了就要申请空间创建对象了
 





在-128~127的Integer值并且以Integer x = value;的方式赋值的Integer值在进行==和equals比较时，都会返回true，因为Java里面对处在在-128~127之间的Integer值，用的是原生数据类型int，此时调用的是Integer.valueOf()方法，会在内存里供重用，也就是说这之间的Integer值进行==比较时只是进行int原生数据类型的数值比较，而超出-128~127的范围，进行==比较时是进行地址及数值比较。
 
==和equals的区别，==是进行地址及值比较，无法对==操作符进行重载，而对于equals方法，
Integer里面的equals方法重写了Object的equals方法，查看Integer源码可以看出equals方法进行的是数值比较。
   public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return value == ((Integer)obj).intValue();
        }
        return false;
    }


		 */
	}
}
