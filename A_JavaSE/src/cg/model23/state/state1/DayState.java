package cg.model23.state.state1;

///ʵ��״̬�ӿڵ��࣬ʵ�������еĳ��󷽷���������״̬ʵ��ʱ������ģʽ*/

public class DayState implements State {
	private static DayState singleton = new DayState();
	private DayState() {
	}
	public static State getInstance(){
		return singleton;
	}
	
	
	//ʱ��仯ʱ���ı䵱ǰ״̬����
	@Override
	public void doClock(Context context, int hour) {
		if(hour<9||hour>=17){
			context.changeState(NightState.getInstance());
		}

	}

	@Override
	public void doUse(Context context) {
		context.recordLog("ʹ�ý�⣨���죩");

	}

	@Override
	public void doAlarm(Context context) {
		context.callSecurityCenter("���¾��壨���죩");

	}

	@Override
	public void doPone(Context context) {
		context.callSecurityCenter("����ͨ�������죩");

	}
	public String toString(){
		return "[����]";
	}

}

