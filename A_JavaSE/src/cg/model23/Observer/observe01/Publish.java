package cg.model23.Observer.observe01;

import java.util.Observable;

public class Publish extends Observable {
	private String data = "";

	public String getData() {
		return data;
	}

	public void setData(String data) {
		if (!this.data.equals(data)) {
			this.data = data;
			 setChanged();    //�ı�֪ͨ�ߵ�״̬
		}
		notifyObservers();    //���ø���Observable������֪ͨ���й۲���
	}
	

}
