package cg.model23.state.state1;

//״̬��ӿڣ��������󷽷�
public interface State {
	public abstract void doClock(Context context,int hour );
	public abstract void doUse(Context context);
	public abstract void doAlarm(Context context);
	public abstract void doPone(Context context);
}