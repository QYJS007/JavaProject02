package cg.model23.state.state1;

//状态类接口，声明抽象方法
public interface State {
	public abstract void doClock(Context context,int hour );
	public abstract void doUse(Context context);
	public abstract void doAlarm(Context context);
	public abstract void doPone(Context context);
}