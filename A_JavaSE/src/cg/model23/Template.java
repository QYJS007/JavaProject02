package cg.model23;

import cg.model23.template.Benchmark;
import cg.model23.template.MethodBenchmark;

public class Template {
	public static void main(String[] args) {
		Benchmark operation = new MethodBenchmark();
		long duration = operation.repeat(Integer.parseInt("11".trim()));
		System.out.println("The operation took " + duration + " milliseconds");
	}
}


/*至此,Template模式已经完成,是不是很简单?看看如何使用:

Benchmark operation = new MethodBenchmark();
long duration = operation.repeat(Integer.parseInt(args[0].trim()));
System.out.println("The operation took " + duration + " milliseconds");

也许你以前还疑惑抽象类有什么用,现在你应该彻底明白了吧? 至于这样做的好处,
		很显然啊,扩展性强,以后Benchmark内容变化,我只要再做一个继承子类就可以,不必修改其他应用代码.*/
