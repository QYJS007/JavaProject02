package thread.synchronize.ShareData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadScopeShareData {
	/*
	 * 需求:  一个数据在同样的一个线程中的数据 始终是 一制的.. 
	 */
	private static int data = 0;// 共享数据
	private static Map<Thread, Integer> threadData = new HashMap<Thread, Integer>();
	
	public static void main(String[] args) {
		for(int i=0;i<2;i++){
			new Thread(new Runnable(){
				public void run() {
					int data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() + " has put data :" + data);
					threadData.put(Thread.currentThread(), data);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}
	
	// 俩个任务模块 A
	static class A{
		public void get(){
			int data = threadData.get(Thread.currentThread());
			System.out.println("A from " + Thread.currentThread().getName() + " get data :" + data);
		}
	}
	// 俩个任务模块 B
	static class B{
		public void get(){
			int data = threadData.get(Thread.currentThread());			
			System.out.println("B from " + Thread.currentThread().getName() + " get data :" + data);
		}		
	}
}
