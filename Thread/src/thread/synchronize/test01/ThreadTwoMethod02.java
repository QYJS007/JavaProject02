package thread.synchronize.test01;

public class ThreadTwoMethod02 {
	public static void main(String[] args) {
		new Thread(
				new Runnable(){
					public void run() {
						while(true){
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("runnable :" + Thread.currentThread().getName());

						}							
					}
				}
				// Runnable 是不执行的, 子类继承父类 重写父类的 方法 , 父类原有方法就作废了. 
				){
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread :" + Thread.currentThread().getName());
				}	
			}
		}.start();
	}
}
