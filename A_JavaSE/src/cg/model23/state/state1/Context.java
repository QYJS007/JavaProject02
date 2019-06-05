package cg.model23.state.state1;

public interface Context {

	public abstract void setClock(int hour);
	public abstract void changeState(State nightState);
	public abstract void callSecurityCenter(String msg);
	public abstract void recordLog(String msg);

}
