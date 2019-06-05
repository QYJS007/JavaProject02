package cg.model23.state.state1;

public class Main {
	public static void main(String[] args) {
		SafeFrame safeFrame = new SafeFrame("State Sample");
		for(int hour=0;hour<24;hour++){
			safeFrame.setClock(hour);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

	}
}

