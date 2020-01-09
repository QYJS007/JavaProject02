package thread.synchronize.test01;


public class TraditionalThreadSynchronized {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TraditionalThreadSynchronized().init();
	}
	
	private void init(){
		final Outputer outputer = new Outputer();
		new Thread(new Runnable(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output2("zhangxiaoxiang");
				}
				
			}
		}).start();
		
		new Thread(new Runnable(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output3("lihuoming");
				}
				
			}
		}).start();
	}

	
	
	static class Outputer{
		public void output(String name){
			int len = name.length();
			synchronized (Outputer.class) //监视器为 字节码文件 Outputer.class
			{
				for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
		// 使用的锁为 this 调用这个方法的对象 
		public synchronized void output2(String name){
			int len = name.length();
			for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
			}
			System.out.println();
		}
		
		// 静态方法的监视器 为字节码文件
		public static synchronized void output3(String name){
			int len = name.length();
			for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
			}
			System.out.println();
		}	
	}

}
