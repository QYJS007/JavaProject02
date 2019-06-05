package cg.model23.state.state1;

//ʵ��״̬�ӿڵ��࣬����״̬ʵ��ʱʹ���˵���ģʽ
public class NightState implements State{
	
	private static NightState singleton = new NightState();
	private NightState() {
	}
	public static NightState getInstance(){
		return singleton;
	}
	//��״̬�仯ʱ���ı䵱ǰ״̬
	@Override
	public void doClock(Context context, int hour) {
		if(hour>=9&&hour<17){
			context.changeState(DayState.getInstance());
		}

	}

	@Override
	public void doUse(Context context) {
		context.callSecurityCenter("����������ʹ�ý��");

	}

	@Override
	public void doAlarm(Context context) {
		context.callSecurityCenter("���¾��壨���ϣ�");

	}

	@Override
	public void doPone(Context context) {
		context.recordLog("���ϵ�ͨ��¼��");
	}
	public String toString(){
		return "[����]";
	}
}

