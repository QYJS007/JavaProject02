package utils.model.concurrent;

/**
 * 描述:指定执行的任务
 * @author:李凯昊
 * @date:2017年6月1日 下午5:10:08   
 * @param <T> 参数类型
 * @param <V> 返回值类型
 */
public interface Job<T,V> {
	V doJob(T param);
}
