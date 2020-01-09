package thread.example.bank;

public class Person  implements Runnable{

	private String name ;
	private Bank bank;


	public Person(String name, Bank bank) {
		super();
		this.name = name;
		this.bank = bank;
	}


	public void run() {
		while(bank.account>0){
			bank.lookMoney();
			bank.subtractingMoney(200);
			bank.lookMoney();
		}
	}

}
