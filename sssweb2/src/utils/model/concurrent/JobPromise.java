package utils.model.concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 描述:job执行信息
 * @author:李凯昊
 * @date:2017年6月1日 下午4:16:50   
 * @param <T> 参数类型
 * @param <V> 返回值类型
 */
public class JobPromise<T,V> {
	//线程池对象, 用来中断任务
	private ScheduledThreadPoolExecutor executor;
	//任务执行信息
	private List<Future<JobResult<T, V>>> futureList;
	
	public JobPromise(ScheduledThreadPoolExecutor executor, List<Future<JobResult<T, V>>> futureList){
		this.executor = executor;
		this.futureList = futureList;
	}
	
	/**
	 * 描述:获取job的参数和执行结果(会阻塞到所有任务完成)
	 * @author: 李凯昊
	 * @date:2017年6月1日 下午4:13:40
	 * @return
	 */
	public List<JobResult<T,V>> getParamValueList(){
		try {
			List<JobResult<T,V>> jobResultList = new ArrayList<JobResult<T,V>>();
			for(Future<JobResult<T, V>> future : futureList){
				JobResult<T, V> jobResult = future.get(); //有阻塞作用
				jobResultList.add(jobResult);
			}
			return jobResultList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 描述:获得job的参数和执行结果 (会阻塞到所有任务完成)
	 * @author: 李凯昊
	 * @date:2017年7月1日 下午6:20:46
	 * @return
	 */
	public Map<T,V> getParamValueMap(){
		List<JobResult<T,V>> list = getParamValueList();
		Map<T,V> map = new HashMap<T,V>();
		for(JobResult<T,V> result : list){
			map.put(result.getParam(), result.getValue());
		}
		return map;
	}
	
	/**
	 * 描述:获得job的执行结果(会阻塞到所有任务完成)
	 * @author: 李凯昊
	 * @date:2017年6月1日 下午4:15:27
	 * @return
	 */
	public List<V> getValueList(){
		List<V> valueList = new ArrayList<V>();
		List<JobResult<T,V>> jobResultList = getParamValueList();
		for(JobResult<T,V> jobResult : jobResultList){
			if(jobResult.getValue()!=null){
				valueList.add(jobResult.getValue());
			}
		}
		return valueList;
	}
	
	/**
	 * 描述:中断所有job(会取消未开始的任务,并中断休眠中的任务. 正在运行的任务中断不了)
	 * @author: 李凯昊
	 * @date:2017年6月1日 下午4:16:18
	 */
	public void aboutJob(){
		if(executor==null){
			throw new RuntimeException("executor为空,不能中断任务");
		}
		executor.shutdownNow();
	}
}
