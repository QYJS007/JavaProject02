package thread;

public class MainTest {

	public static void main(String[] args) {

//6. （线程同步）设计4个线程，其中两个线程每次对j增加1，另外两个线程对j每次减少1。
		/*
		 * 线程之间共享数据的安全性问题:::加锁..
		 * 
		 */
		//创建一个存放ThreadAddSub对象的数组
		ThreadAddSub[] tSub=new ThreadAddSub[4];
		for (int i = 0; i < tSub.length; i++) {

			//把实例化ThreadAddSub对象赋值到数组内
			//第一三个是true，二四个是false
			tSub[i]=new ThreadAddSub(i%2==0?true:false);

			//让线程开始工作
			tSub[i].start();
		}
		System.out.println(1-5);
	}

}
