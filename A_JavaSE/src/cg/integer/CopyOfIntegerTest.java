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
		 * Java������
 
��������java�����ڱ���������Ѿ�ȷ���ģ��������˹����࣬�������ӿ��еĳ�����Ҳ�����ַ�������������
 
String s = "Java" ���������ķ�ʽ������������"����"�ͻᱻ�ŵ������أ���������JVM��һ��������ڴ�ռ䡣
 
ʹ��Java�����ؼ�������Ϊ�˷����ݵش���ĳЩ���󣬵�����Ҫһ������ʱ�򣬾�ȥ������������ң��Ҳ������ڳ������洴��һ�������Ǳ���ע�������������new �����ġ���ô������ʲô�������ǲ���ŵ�������ģ�������������µĿռ�洢��
 
java�л������͵İ�װ��Ĵ󲿷ֶ�ʵ���˳����ؼ�������Щ����Byte,Short,Integer,Long,Character,Boolean,�������ָ��������͵İ�װ����û��ʵ�֡�����Byte,Short,Integer,Long,Character��5�����͵İ�װ��Ҳֻ���ڶ�ӦֵС�ڵ���127ʱ�ſ�ʹ�ö���ء������˾�Ҫ����ռ䴴��������
 





��-128~127��Integerֵ������Integer x = value;�ķ�ʽ��ֵ��Integerֵ�ڽ���==��equals�Ƚ�ʱ�����᷵��true����ΪJava����Դ�����-128~127֮���Integerֵ���õ���ԭ����������int����ʱ���õ���Integer.valueOf()�����������ڴ��﹩���ã�Ҳ����˵��֮���Integerֵ����==�Ƚ�ʱֻ�ǽ���intԭ���������͵���ֵ�Ƚϣ�������-128~127�ķ�Χ������==�Ƚ�ʱ�ǽ��е�ַ����ֵ�Ƚϡ�
 
==��equals������==�ǽ��е�ַ��ֵ�Ƚϣ��޷���==�������������أ�������equals������
Integer�����equals������д��Object��equals�������鿴IntegerԴ����Կ���equals�������е�����ֵ�Ƚϡ�
   public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return value == ((Integer)obj).intValue();
        }
        return false;
    }


		 */
	}
}
