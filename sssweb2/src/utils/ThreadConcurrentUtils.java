package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import utils.model.concurrent.DefaultThreadFactory;
import utils.model.concurrent.Job;
import utils.model.concurrent.JobCallable;
import utils.model.concurrent.JobPromise;
import utils.model.concurrent.JobResult;


public class ThreadConcurrentUtils {
	
	/**
	 * 描述:多线程共同执行某个任务列表
	 * @author: 李凯昊
	 * @date:2017年6月1日 下午5:18:16
	 * @param paramList 参数列表
	 * @param job 任务
	 * @param threadCount 线程数
	 * @param runInterval 每个任务的间隔时间,单位毫秒
	 * @return 任务控制对象,可获取返回值 或 中断任务
	 */
	public static <T,V> JobPromise<T,V> common(List<T> paramList, Job<T, V> job, int threadCount, int runInterval){
		//最大线程30个
		if(threadCount>30){
			threadCount = 30;
		}
		//创建线程池
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadCount, new DefaultThreadFactory("common"));
		try {
			//添加任务
			List<Future<JobResult<T, V>>> futureList = new ArrayList<Future<JobResult<T, V>>>();
			int delay = 0;
			for(T param : paramList){
				JobCallable<T,V> callable = new JobCallable<T, V>(job, param); //要执行的任务信息
				Future<JobResult<T, V>> future = executor.schedule(callable, delay, TimeUnit.MILLISECONDS); //添加任务
				futureList.add(future); //接收返回值
				
				//计算下一个任务的延迟时间
				delay += runInterval;
			}
			
			return new JobPromise<T,V>(executor,futureList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			//关闭线程池
			executor.shutdown();
		}
	}
	
}

