package thread;

public class ThreadAddSub extends Thread{
	//判断要进行的操作
	boolean operate = true;
	//要操作的数
	static   Integer sum = 0;

	// 把操作运算通过构造方法传进来
	public ThreadAddSub(boolean operate) {
		super();
		this.operate = operate;
	}

	@Override
	public void run() {
		super.run();
		while (true) {
			operator();
			try {
				Thread.sleep(1000);// 睡眠0.5秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private  void operator() {// 互斥的,4个线程之间的锁;应该是同一把锁====>类的字节码对象, 线程之间对同一数据才互斥,安全;;;
		synchronized (ThreadAddSub.class) {
			if (operate) {
				sum += 5;
				System.out.println(Thread.currentThread().getName()
						+ "加 5  后，sum=" + sum);
			} else {
				sum -= 4;
				System.out.println(Thread.currentThread().getName()
						+ "减 4  后，sum=" + sum);
			}
		}
	}
}
