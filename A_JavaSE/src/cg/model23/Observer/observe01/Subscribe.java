package cg.model23.Observer.observe01;

import java.util.Observable;
import java.util.Observer;

public class Subscribe implements Observer{
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("�յ�֪ͨ:" + ((Publish)o).getData());
	}

	public Subscribe(Observable o) {
		 o.addObserver(this);
	}

}
