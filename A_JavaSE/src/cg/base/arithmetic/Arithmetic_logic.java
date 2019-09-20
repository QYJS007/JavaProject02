package cg.base.arithmetic;

public class Arithmetic_logic {

	public static void main(String[] args) {
		int a=2;
	       int b=3;
	       a=a^b;
	       System.out.println(a);
	       b=a^b;
	       System.out.println("b:" + b);
	       a=a^b;
	       System.out.println("a="+ a);
	       System.out.println("a="+a+",b="+b);
		/**
		 * ^是异或运算符，异或的规则是转换成二进制比较，相同为0，不同为1.
一个数a与另一个数b异或的结果等于a^b，用结果（ a^b)异或a，就会得到b；
上面的结果，我们用代码来验证。代码( a=a^b; b=a^b; a=a^b;)可以转换成这样。
int c=a^b ; ------------ 01=10^11 得到结果C（ a^b)
b=c^b; ------------ 10=01^11 用结果（ a^b)异或a，就会得到b；
a=c^a; -------------11=01^10 用结果（ a^b)异或b，就会得到a；
————————————————
版权声明：本文为CSDN博主「keepons」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/lkpklpk/article/details/81287115
		 */
		System.out.println(true ^true );
		 
		
		
		
		
	}
}
