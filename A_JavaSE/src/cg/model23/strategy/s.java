package cg.model23.strategy;

public class s {

	enum EncryptType{AES,RSA};
	public void doRequest(EncryptType  type ,String params){
		switch (type){
		case AES:
			// �˴�����AES�����㷨
			params="����AES���ܹ�����";
			break;
		case RSA:
			// �˴�����RSA�����㷨
			params="����RSA���ܹ�����";
			break;
		}
		//Ȼ������������󽫲������͹�ȥ
	}
}

