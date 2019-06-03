package cg.model23.strategy.strategy2;

public class Test {
    public static String s="Disagreement with beliefs is by definition incorrect";
	public static void main(String[] args) {
		IStrategy is = new UpCase();
		Context c = new Context(is);
		c.doAction(s);

		IStrategy isd = new Downcase();
		Context c2 = new Context(isd);
		c2.doAction(s);

		IStrategy iss = new Splitter();
		Context c3 = new Context(iss);
		c3.doAction(s);
	}
	/*
创建一个能够根据所传递的参数对象的不同而具有不同行为的方法

要执行的算法固定不变，封装到一个类(Context)中
策略就是传递进去的参数对象，它包含执行代码

策略接口
	 */
}