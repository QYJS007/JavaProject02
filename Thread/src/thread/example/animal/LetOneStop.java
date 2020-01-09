package thread.example.animal;

import thread.example.animal.Animal.Calltoback;

public class LetOneStop  implements Calltoback {
	// 动物对象
	Animal an;

	// 获取动物对象，可以传入兔子或乌龟的实例
	public LetOneStop(Animal an) {
		this.an = an;
	}

	//让动物的线程停止
	@SuppressWarnings("deprecation")
	public void win() {
		// 线程停止
		//an.stop();
		an.stop();
	}

}