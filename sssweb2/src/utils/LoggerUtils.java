package utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LoggerUtils {
	private static Log logger = LogFactory.getLog(LoggerUtils.class);
	
	//打印info日志
	public static void info(Object obj){
		logger.info(obj);
	}
	
	//打印debug日志
	public static void debug(Object obj){
		logger.debug(obj);
	}
	
	//打印error日志
	public static void error(String msg){
		logger.error(msg);
	}
	
	//打印error日志
	public static void error(String msg,Exception e){
		logger.error(msg,e);
	}
	
}
