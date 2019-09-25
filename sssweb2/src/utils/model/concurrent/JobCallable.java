package utils.model.concurrent;

import java.util.concurrent.Callable;

/**
 * 描述: callable实现类
 * @author:李凯昊
 * @date:2017年6月1日 下午5:12:01   
 * @param <T> 参数类型
 * @param <V> 返回值类型
 */
public class JobCallable<T,V> implements Callable<JobResult<T,V>>{
	
	private Job<T,V> job;
	private T param;
	private String name;
	
	public JobCallable(Job<T,V> job, T param){
		this.job = job;
		this.param = param;
	}
	
	public JobCallable(Job<T,V> job, T param, String name){
		this.job = job;
		this.param = param;
		this.name = name;
	}
	
	public JobResult<T,V> call() throws Exception {
		V result = job.doJob(param);
		return new JobResult<T,V>(param, result);
	}
	
	public String getName(){
		return name;
	}
	
	public T getParam(){
		return param;
	}
	
}
