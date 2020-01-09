package thread.example.bank;

public class MainTest {

	public static void main(String[] args) {

		final Bank bank = new Bank(3000);
		Person person1 = new Person("lisi", bank);
		Person person2 = new Person("zhangsan", bank);
		
		new Thread(person1).start();
		new Thread(person2).start();
	}
}