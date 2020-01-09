package thread.example.ticket;

public class MainTest {
	public static void main(String[] args) {
			for (int i = 1; i <=3; i++) {
				new Station("窗口"+i).start();;
			}
	}
}
