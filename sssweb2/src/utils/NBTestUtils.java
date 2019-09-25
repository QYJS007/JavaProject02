package utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

public class NBTestUtils {
	
	/**
	 * 调用修改过的方法(按标识行修改)
	 * @author likaihao
	 * @date 2016年4月21日 下午7:56:14
	 * @param className 要调用的方法所在类名
	 * @param methodName 要调用的方法名称
	 * @param params 要调用的方法的参数
	 * @param lastSrc 定位代码
	 * @param diff 定位代码的偏移量(0代表前一行,1代表后一行)
	 * @param insertSrc 需要插入的代码
	 */
	public static Object callModifyMethod(String className,String methodName,Object[] params,String newMethodBody){
		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(getClassPath());
			CtClass ctClass = pool.get(className);
			
			//获得目标方法
			CtMethod[] ctMethodArr = ctClass.getMethods();
			CtMethod ctMethod = null;
			for(CtMethod cm : ctMethodArr){
				if(cm.getName().equals(methodName)){
					if(ctMethod==null){
						ctMethod = cm;
					}else{
						throw new RuntimeException("存在多个方法的重载,停止调用");
					}
				}
			}
			
			Loader loader = new Loader(pool);
			ctMethod.setBody("{"+newMethodBody+"}");
			
			Class<?> clazz = loader.loadClass(className);
			Method method = null;
			Method[] methodArr = clazz.getMethods();
			for(Method m : methodArr){
				if(m.getName().equals(ctMethod.getName())){
					method = m;
					break;
				}
			}
			ctClass.detach();
			
			Object returnValue = method.invoke(clazz.newInstance(), params);
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 调用修改过的方法(按标识行修改)
	 * @author likaihao
	 * @date 2016年4月21日 下午7:56:14
	 * @param className 要调用的方法所在类名
	 * @param methodName 要调用的方法名称
	 * @param params 要调用的方法的参数
	 * @param lastSrc 定位代码
	 * @param diff 定位代码的偏移量(0代表前一行,1代表后一行)
	 * @param insertSrc 需要插入的代码
	 */
	public static Object callModifyMethod(String className,String methodName,Object[] params,String lastSrc,int diff,String insertSrc){
		List<String> classSrcList = getClassSrc(className);
		
		lastSrc = StringUtils.getRegexEscapeString(lastSrc);
		List<Integer> lastIndexList = new ArrayList<Integer>();
		for(int i=0;i<classSrcList.size();i++){
			if(classSrcList.get(i).trim().matches("^"+lastSrc+"$")){
				lastIndexList.add(i+1+diff);
			}
		}
		if(lastIndexList.size()<1){
			throw new RuntimeException("没有找到指定内容的行:"+lastSrc);
		}
		if(lastIndexList.size()>1){
			throw new RuntimeException("找到多个指定内容的行:"+lastSrc+","+lastIndexList);
		}
		int lineNum = lastIndexList.get(0);
		return callModifyMethod(className,methodName,params,lineNum,insertSrc);
	}
	
	/**
	 * 调用修改过的方法(按行号修改)
	 * @author likaihao
	 * @date 2016年4月21日 上午9:57:00
	 * @param className 类名
	 * @param methodName 方法名
	 * @param params 参数
	 * @param lineNum 插入代码的行号
	 * @param insertStr 需要插入的代码
	 */
	public static Object callModifyMethod(String className,String methodName,Object[] params,int lineNum,String insertStr){
		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(getClassPath());
			CtClass ctClass = pool.get(className);
			
			//获得目标方法
			CtMethod[] ctMethodArr = ctClass.getMethods();
			CtMethod ctMethod = null;
			for(CtMethod cm : ctMethodArr){
				if(cm.getName().equals(methodName)){
					if(ctMethod==null){
						ctMethod = cm;
					}else{
						throw new RuntimeException("存在多个方法的重载,停止调用");
					}
				}
			}
			
			Loader loader = new Loader(pool);
			ctMethod.insertAt(lineNum, insertStr);
			
			Class<?> clazz = loader.loadClass(className);
			Method method = null;
			Method[] methodArr = clazz.getMethods();
			for(Method m : methodArr){
				if(m.getName().equals(ctMethod.getName())){
					method = m;
					break;
				}
			}
			ctClass.detach();
			
			Object returnValue = method.invoke(clazz.newInstance(), params);
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 返回指定类的源码
	 * @author likaihao
	 * @date 2016年4月21日 下午8:29:55
	 * @param className
	 * @return
	 */
	public static List<String> getClassSrc(String className){
		try {
			String srcPath = getSrcPath();
			String path = srcPath + "/" + className.replace(".", "/") + ".java";
			return IOUtils.readFileReturnList(path);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获得src路径
	 * @author likaihao
	 * @date 2016年4月22日 上午10:19:40
	 * @return
	 */
	public static String getSrcPath(){
		try {
			String classPath = getClassPath();
			String srcPath = classPath+"/../../../test";
			return new File(srcPath).getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获得classpath路径
	 * @author likaihao
	 * @date 2016年4月22日 上午10:19:48
	 * @return
	 */
	public static String getClassPath(){
		String classPath = System.getProperty("java.class.path").split(";")[0];
		return classPath;
	}
	
}