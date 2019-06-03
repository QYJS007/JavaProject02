package cg.model23.strategy;

public class s {

	enum EncryptType{AES,RSA};
	public void doRequest(EncryptType  type ,String params){
		switch (type){
		case AES:
			// 此处进行AES加密算法
			params="经过AES加密过数据";
			break;
		case RSA:
			// 此处进行RSA加密算法
			params="经过RSA加密过数据";
			break;
		}
		//然后调用网络请求将参数发送过去
	}
}

