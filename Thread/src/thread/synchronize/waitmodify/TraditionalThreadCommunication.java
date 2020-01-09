package thread.synchronize.waitmodify;

public class TraditionalThreadCommunication {
	/**
	 * 线程交替执行: 主线程执行100次;  线程sub 执行10次 ; 切换回来主线程在执行. 往复50次
	 * 
	 */
	public static void main(String[] args) {
		final Business business = new Business();// 定义一个类 : 俩线程交替执行
		new Thread(
				new Runnable() {
					public void run() {
						for(int i=1;i<=50;i++){
							business.sub(i);
						}
					}
				}
				).start();

		for(int i=1;i<=50;i++){
			business.main(i);
		}
	}
}

// 这个类中的俩个方法是 互斥的;    只能有一个线程  来 执行..这个方法 ;; 
// 执行时机::::

class Business {	// synchronized 同一一个锁对象 
	private boolean bShouldSub = true;
	public synchronized void sub(int i){
		while(!bShouldSub){// 默认不执行 
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 先循环 
		for(int j=1;j<=10;j++){
			System.out.println("sub thread sequence of " + j + ",loop of " + i);
		}
		bShouldSub = false;// 进入等待状态
		this.notify();// 唤醒其他的线程;;;;
	}
	
	// synchronized 同一一个锁对象 
	public synchronized void main(int i){  // 主线程
		while(bShouldSub){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int j=1;j<=100;j++){
			System.out.println("main thread sequence of " + j + ",loop of " + i);
		}
		bShouldSub = true;
		this.notify();
	}
}
