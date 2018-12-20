package cg.enums.singleton;

public class SingletonTest  implements Runnable{

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new SingletonTest());
			System.out.println("sdsds");
			t.start();
		}
	}
	public void run() {
		SingletonEnum.INSTANCE.setName("lisi");
		String name = SingletonEnum.INSTANCE.getName();
		System.out.println(name);
	}
}
