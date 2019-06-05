package cg.model23.state.state1;

//实现状态接口的类，生成状态实例时使用了单例模式
public class NightState implements State{
	
	private static NightState singleton = new NightState();
	private NightState() {
	}
	public static NightState getInstance(){
		return singleton;
	}
	//当状态变化时，改变当前状态
	@Override
	public void doClock(Context context, int hour) {
		if(hour>=9&&hour<17){
			context.changeState(DayState.getInstance());
		}

	}

	@Override
	public void doUse(Context context) {
		context.callSecurityCenter("紧急：晚上使用金库");

	}

	@Override
	public void doAlarm(Context context) {
		context.callSecurityCenter("按下警铃（晚上）");

	}

	@Override
	public void doPone(Context context) {
		context.recordLog("晚上的通话录音");
	}
	public String toString(){
		return "[晚上]";
	}
}

