package cg.model23.strategy.strategy1;

public class Test {
	public static void main(String[] args) {


		IEncryptStrategy rsaStrategy=new RSAStrategy();
		String doRequest = doRequest(rsaStrategy,"test");
	}

	public static String doRequest(IEncryptStrategy encryptStrategy , String params){
		params=encryptStrategy.encryptStr(params);
		//然后调用网络请求将参数发送过去
		return params;
	}
	/*
	 * 通过上面的例子我们总结一下策略模式的优缺点：

优点：

1、 简化了单元测试，因为每个算法都有自己的类，可以通过自己的接口单独测试。 
2、 避免程序中使用多重条件转移语句，使系统更灵活，并易于扩展。 
3、 遵守大部分GRASP原则和常用设计原则，高内聚、低偶合。

缺点： 
1、 因为每个具体策略类都会产生一个新类，所以会增加系统需要维护的类的数量。
2、 在基本的策略模式中，选择所用具体实现的职责由客户端对象承担，并转给策略模式的Context对象，没有解除客户端需要选择判断的压力
总结：
    通过使用策略模式很好的解决了之前项目中遇到的请求加密需求，而且做到了更加容易的扩展性。

	 */
}