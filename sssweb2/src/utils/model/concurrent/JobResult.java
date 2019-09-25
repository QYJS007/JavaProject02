package utils.model.concurrent;

/**
 * 描述:job结果
 * @author:李凯昊
 * @date:2017年6月1日 下午4:23:42   
 * @param <T>
 * @param <V>
 */
public class JobResult<T,V>{
	private T param;
	private V value;
	public JobResult(T param, V value) {
		this.param = param;
		this.value = value;
	}
	
	public T getParam() {
		return param;
	}
	public void setParam(T param) {
		this.param = param;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
}
