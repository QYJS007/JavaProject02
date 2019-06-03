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
����һ���ܹ����������ݵĲ�������Ĳ�ͬ�����в�ͬ��Ϊ�ķ���

Ҫִ�е��㷨�̶����䣬��װ��һ����(Context)��
���Ծ��Ǵ��ݽ�ȥ�Ĳ�������������ִ�д���

���Խӿ�
	 */
}