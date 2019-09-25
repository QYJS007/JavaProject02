package utils.model.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * 描述:默认threadFactory实现类
 * @author:李凯昊
 * @date:2017年6月1日 下午5:12:29
 */
public class DefaultThreadFactory implements ThreadFactory{

	private Integer num = 0;//计数器
	private String func = "myThread";//功能名称
	
	public DefaultThreadFactory(String func){
		this.func = func;
	}
	
	public Thread newThread(Runnable r) {
		num++;
		Thread thread = new Thread(r);
		thread.setName(func+"-thread-"+num);
		return thread;
	}
	
}
