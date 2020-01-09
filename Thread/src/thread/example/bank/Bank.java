package thread.example.bank;


/**
 * ．示例二：两个人    AB通过一个账户           A在柜台取钱         和B在ATM机取钱！
	程序分析：钱的数量要设置成一个静态的变量。两个人要取的同一个对象值
 */
public class Bank {
	public  Integer account ;
	public Bank() {
		super();
	}
	public Bank(Integer account) {
		super();
		this.account = account;
	}
	
	public synchronized void lookMoney(){  
		System.out.println(Thread.currentThread().getName()+"账户余额："+account);  
	}
	
	public  void addMoney(Integer money){
		account=account+money;
		System.out.println(Thread.currentThread().getName()+"存进："+money);  
	}

	public synchronized void subtractingMoney(Integer money){
		if(account>=money){
			account=account-money;
		}
		System.out.println(Thread.currentThread().getName() +"取出："+money);  
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
