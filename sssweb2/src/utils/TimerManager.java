package utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import utils.model.concurrent.DefaultThreadFactory;
import utils.model.concurrent.Job;
import utils.model.concurrent.JobCallable;
import utils.model.concurrent.JobResult;

public class TimerManager<T,V> {
	
	//控制所有timeout执行的线程池
	private static ScheduledThreadPoolExecutor timerExecutor = new ScheduledThreadPoolExecutor(10, new DefaultThreadFactory("timer"));
	
	private T param;
	private Job<T,V> job;
	private int afterMilliSeconds;
	private String timerName;
	private Future<?> future;
	
	private TimerManager(T param, Job<T,V> job, int afterMilliSeconds, String timerName, Future<?> future){
		this.param = param;
		this.job = job;
		this.afterMilliSeconds = afterMilliSeconds;
		this.timerName = timerName;
		this.future = future;
	}
	
	
	/**
	 * 描述: 延迟执行任务
	 * @author: 李凯昊
	 * @date:2017年6月2日 上午9:28:49
	 * @param param 任务参数
	 * @param job 任务
	 * @param afterMilliSeconds 延迟时间(单位:毫秒)
	 * @return
	 */
	public static <T,V> TimerManager<T,V> setTimeout(T param, Job<T, V> job, int afterMilliSeconds, String timerName){
		//提交任务
		JobCallable<T,V> callable = new JobCallable<T, V>(job, param, timerName); //要执行的任务信息
		Future<JobResult<T, V>> future = timerExecutor.schedule(callable, afterMilliSeconds, TimeUnit.MILLISECONDS); //添加任务
		return new TimerManager<T,V>(param,job,afterMilliSeconds,timerName,future);
	}
	
	/**
	 * 描述:获得定时器的名称 
	 * @author: 李凯昊
	 * @date:2017年6月2日 上午10:35:41
	 * @return
	 */
	public String getTimerName(){
		return timerName;
	}
	
	
	/**
	 * 描述: 取消延时任务
	 * @author: 李凯昊
	 * @date:2017年6月2日 上午9:43:43
	 * @return 是否取消成功
	 */
	public boolean cancel(){
		if(future==null){
			throw new RuntimeException("取消失败,future为空");
		}
		return future.cancel(true);
	}
	
	/**
	 * 描述: 重置任务
	 * @author: 李凯昊
	 * @date:2017年6月2日 上午10:18:55
	 */
	public void reset(){
		cancel();
		TimerManager<T,V> timerManager = setTimeout(param,job,afterMilliSeconds,timerName);
		this.future = timerManager.future;
	}
	
	/**
	 * 描述:获得定时器执行信息 
	 * @author: 李凯昊
	 * @date:2017年6月2日 上午11:14:56
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getStatus(){
		BlockingQueue<Runnable> queue = timerExecutor.getQueue();
		
		StringBuffer buffer = new StringBuffer();
		for(Runnable r : queue){
			ScheduledFuture future = (ScheduledFuture) r;
			if(!future.isDone()){
				JobCallable jobCallable = (JobCallable) extractUnderlyingCallable((FutureTask)future);
				String name = jobCallable.getName();
				Date nextRunTime = new Date(new Date().getTime() + future.getDelay(TimeUnit.MILLISECONDS));
				buffer.append(name+" --> "+DateUtils.dateToStringYMdHms(nextRunTime)+"\n");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 描述:获得任务中的callable对象 (从play拿过来的)
	 * @author: 李凯昊
	 * @date:2017年6月2日 上午11:14:18
	 * @param futureTask
	 * @return
	 */
	private static Object extractUnderlyingCallable(FutureTask<?> futureTask) {
		try {
			Field syncField = FutureTask.class.getDeclaredField("sync");
			syncField.setAccessible(true);
			Object sync = syncField.get(futureTask);
			Field callableField = sync.getClass().getDeclaredField("callable");
			callableField.setAccessible(true);
			Object callable = callableField.get(sync);
			if (callable.getClass().getSimpleName().equals("RunnableAdapter")) {
				Field taskField = callable.getClass().getDeclaredField("task");
				taskField.setAccessible(true);
				return taskField.get(callable);
			}
			return callable;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
