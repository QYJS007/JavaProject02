package cg.model23.strategy.strategy1;

public class Test {
	public static void main(String[] args) {


		IEncryptStrategy rsaStrategy=new RSAStrategy();
		String doRequest = doRequest(rsaStrategy,"test");
	}

	public static String doRequest(IEncryptStrategy encryptStrategy , String params){
		params=encryptStrategy.encryptStr(params);
		//Ȼ������������󽫲������͹�ȥ
		return params;
	}
	/*
	 * ͨ����������������ܽ�һ�²���ģʽ����ȱ�㣺

�ŵ㣺

1�� ���˵�Ԫ���ԣ���Ϊÿ���㷨�����Լ����࣬����ͨ���Լ��Ľӿڵ������ԡ� 
2�� ���������ʹ�ö�������ת����䣬ʹϵͳ������������չ�� 
3�� ���ش󲿷�GRASPԭ��ͳ������ԭ�򣬸��ھۡ���ż�ϡ�

ȱ�㣺 
1�� ��Ϊÿ����������඼�����һ�����࣬���Ի�����ϵͳ��Ҫά�������������
2�� �ڻ����Ĳ���ģʽ�У�ѡ�����þ���ʵ�ֵ�ְ���ɿͻ��˶���е�����ת������ģʽ��Context����û�н���ͻ�����Ҫѡ���жϵ�ѹ��
�ܽ᣺
    ͨ��ʹ�ò���ģʽ�ܺõĽ����֮ǰ��Ŀ������������������󣬶��������˸������׵���չ�ԡ�

	 */
}