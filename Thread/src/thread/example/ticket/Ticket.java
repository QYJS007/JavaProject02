package thread.example.ticket;
/*
 * 示例一：三个售票窗口同时出售20张票;
	程序分析：
		1. 票数要使用同一个静态值
 		2. 为保证不会出现卖出同一个票数，要java多线程同步锁。

	设计思路：
		1.创建一个站台类Station，继承Thread，重写run方法，在run方法里面执行售票操作！
				售票要使用同步锁：即有一个站台卖这张票时，其他站台要等这张票卖完！
		2.创建主方法调用类

 */
public class Ticket {

	public static Integer ticket = 20; // 20张票
	// 
	public static synchronized  void  sellTicket(){
		//synchronized (Ticket.class){
			if(ticket>0){
				System.out.println(Thread.currentThread().getName()+"售出: "+ticket+"号票" );
				ticket--;
			}else{
				System.out.println(Thread.currentThread().getName()+": 车票已经售罄" );
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //休息一秒

		// }

	}

}
