package utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import sys.SystemConf;

public class ExceptionUtils {
	/**
	 * 获取错误原因
	 * @author likaihao
	 * @date 2015年12月17日 上午9:58:48
	 * @param e
	 * @return
	 */
	public static String getErrMsg(Exception e){
		Exception cause = null;
		if(e instanceof java.lang.reflect.InvocationTargetException){
			java.lang.reflect.InvocationTargetException e2 = (java.lang.reflect.InvocationTargetException)e;
			if(e2.getTargetException() instanceof Exception){
				cause = (Exception) e2.getTargetException();
			}else{
				//可能是错误
				return e2.getTargetException().getClass().getName()+":"+e2.getTargetException().getMessage();
			}
			
		}else{
			cause = (Exception) e.getCause();
		}
		
		if(cause == null){
			String errPosition = getErrPosition(e);//异常发生的位置
			return errPosition+e.getClass().getName()+":"+e.getMessage();
		}
		return getErrMsg(cause);
	}
	
	/**
	 * 获得发生异常的位置
	 * @author likaihao
	 * @date 2015年12月17日 上午11:25:15
	 * @param e
	 * @return
	 */
	public static String getErrPosition(Exception e){
		StackTraceElement[] arr = e.getStackTrace();
		List<String> allPackageName = getAllPackageName();
		for(StackTraceElement element : arr){
			String packageName = element.getClassName().substring(0,element.getClassName().lastIndexOf("."));
			if(allPackageName.contains(packageName)){
				return element.getClassName()+"."+element.getMethodName()+"():"+element.getLineNumber()+"<br>\r\n";
			}
		}
		return "";
	}
	
	/**
	 * 获得指定路径下所有包名
	 * @author likaihao
	 * @date 2015年12月17日 上午11:56:55
	 * @return
	 */
	public static List<String> getAllPackageName(){
		//获得所有包路径
		String classPath = SystemConf.getProjectClassPath();
		File file = new File(classPath);
		List<String> list = new ArrayList<String>();
		getPackageName(file,list);
		
		//将包路径转换为包名
		List<String> list2 = new ArrayList<String>();
		int len = classPath.replace("/", ".").replace("\\\\", ".").replace("\\", ".").length();
		for(String packagePath : list){
			list2.add(packagePath.replace("/", ".").replace("\\\\", ".").replace("\\", ".").substring(len));
		}
		return list2;
	}
	
	//获得指定路径下所有包名 递归方法
	private static void getPackageName(File file,List<String> list){
		if(file.isDirectory()){
			//判断是否存在子文件夹
			File[] fileArr = file.listFiles();
			List<File> dirList = new ArrayList<File>();
			for(File f : fileArr){
				if(f.isDirectory()){
					dirList.add(f);
				}
			}
			if(dirList.size()>0){
				//存在子文件夹
				for(File dir : dirList){
					getPackageName(dir,list);
				}
			}else{
				//不存在子文件夹
				list.add(file.getAbsolutePath());
			}
		}
	}
	
	/**
	 * 获得全部异常堆栈信息
	 * @author likaihao
	 * @datetime 2017年3月14日 上午10:56:40
	 * @param e
	 * @return
	 */
	public static String getAllErrStackInfo(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}