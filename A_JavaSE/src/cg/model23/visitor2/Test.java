package cg.model23.visitor2;

import java.io.File;


/**
 * ������ģʽ�Ĳ�����
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
 * visitorģʽ��һ���ص���ǣ��ṹ�ǹ̶����ģ����ǲ��ܸĵģ�������Ը�һЩ�ṹ��ʵ�ַ�ʽ��������ʵ���е�usb�ӿڣ�
 * ����Ի��������ӿڱ���PCI����ֻҪʵ�ֹ涨�õ�Ӳ���ӿ�interface�༴�ɣ�����һ������ƾ��ô��չ���������޸ĵ��ײ�Ľṹ�������𻵡���ܡ��ˡ�

��������Ҳ��������ȱ�㡣���ǲ�����չ��������ݣ�������������˵��ֻ����չ�ӿڣ���������չ����Ӳ���������޸ĵ���Ӳ����ʵ�ַ�ʽ��
 */
