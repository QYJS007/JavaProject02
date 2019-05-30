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


/*����,Templateģʽ�Ѿ����,�ǲ��Ǻܼ�?�������ʹ��:

Benchmark operation = new MethodBenchmark();
long duration = operation.repeat(Integer.parseInt(args[0].trim()));
System.out.println("The operation took " + duration + " milliseconds");

Ҳ������ǰ���ɻ��������ʲô��,������Ӧ�ó��������˰�? �����������ĺô�,
		����Ȼ��,��չ��ǿ,�Ժ�Benchmark���ݱ仯,��ֻҪ����һ���̳�����Ϳ���,�����޸�����Ӧ�ô���.*/
