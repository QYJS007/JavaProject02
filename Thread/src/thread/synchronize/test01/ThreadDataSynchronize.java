package thread.synchronize.test01;


public class ThreadDataSynchronize {
	/*
	 * 数据同步: 线程间数据共享
	 */
	private Integer  account= 1000; 
	public static void main(String[] args) {  
		final ThreadDataSynchronize synchronize = new ThreadDataSynchronize();
		new Thread(new Runnable() {

			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronize.addMoney(200);
					synchronize.lookMoney(); 
				}
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				while(true){
					synchronize.subtractingMoney(500);
					synchronize.lookMoney();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	//查询  
	public void lookMoney(){  
		System.out.println("账户余额："+account);  
	}
	public void addMoney(Integer money){

		account+=money;
		System.out.println(System.currentTimeMillis()+"存进："+money);  
	}

	public void subtractingMoney(Integer money){
		account-=money;
		System.out.println(System.currentTimeMillis()+"取出："+money);  
	}
}
