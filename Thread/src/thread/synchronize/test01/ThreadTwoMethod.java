package thread.synchronize.test01;

public class ThreadTwoMethod {
	public static void main(String[] args) {

		/*new Thread(new Runnable() {
				public void run() {
					System.out.println("绾跨▼:"+ Thread.currentThread().getName() );
				}
			}).start();


			new Thread(){
				public void run() {
					System.out.println("绾跨▼:"+ Thread.currentThread().getName() );
				};
			}.start();*/
/*
 * 绾跨▼鐨勪咯绉嶆柟娉�
 */
		Thread thread = new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());
					// System.out.println("2:" + this.getName());
				}
			}
		};
		thread.start();
		// 绗簩绉� 鍒涘缓绾跨▼鐨勬柟娉�
		Thread thread2 = new Thread(new Runnable(){
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());

				}				
				
			}
		});
		thread2.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
