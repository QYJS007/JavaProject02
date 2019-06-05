package cg.model23.state.state1;

///实现状态接口的类，实现了所有的抽象方法，在生成状态实例时，单例模式*/

public class DayState implements State {
	private static DayState singleton = new DayState();
	private DayState() {
	}
	public static State getInstance(){
		return singleton;
	}
	
	
	//时间变化时，改变当前状态对象
	@Override
	public void doClock(Context context, int hour) {
		if(hour<9||hour>=17){
			context.changeState(NightState.getInstance());
		}

	}

	@Override
	public void doUse(Context context) {
		context.recordLog("使用金库（白天）");

	}

	@Override
	public void doAlarm(Context context) {
		context.callSecurityCenter("按下警铃（白天）");

	}

	@Override
	public void doPone(Context context) {
		context.callSecurityCenter("正常通话（白天）");

	}
	public String toString(){
		return "[白天]";
	}

}

