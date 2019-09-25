package sys;

import java.util.ResourceBundle;


public class SystemConf {
	
	//项目名称和项目路径,在拦截器中赋值
	protected static String projectName;
	protected static String projectPath;
	//项目class文件存放的位置
	protected static String projectClassPath = SystemConf.class.getClassLoader().getResource("").getPath();
	
	//配置文件
	private static ResourceBundle rb = ResourceBundle.getBundle("resource");
	
	/**
	 * 获得项目名称
	 * @author likaihao
	 * @date 2016年7月8日 下午11:39:27
	 * @return
	 */
	public static String getProjectName(){
		return projectName;
	}
	
	/**
	 * 获得项目路径
	 * @author likaihao
	 * @date 2016年7月8日 下午11:39:39
	 * @return
	 */
	public static String getProjectPath(){
		return projectPath;
	}
	
	/**
	 * 获得访问项目的根URL
	 * @author likaihao
	 * @date 2016年7月8日 下午11:55:01
	 * @return
	 */
	public static String getProjectBaseUrl(){
		return "/"+projectName;
	}
	
	/**
	 * 获得项目class文件存放的路径
	 * @author likaihao
	 * @date 2016年7月9日 上午10:54:18
	 * @return
	 */
	public static String getProjectClassPath(){
		return projectClassPath;
	}
	
	/**
	 * 获得classPath下resource.properties文件中配置的值
	 * @author likaihao
	 * @date 2016年7月21日 下午8:56:32
	 * @param name
	 * @return
	 */
	public static String getResourceValue(String name){
		rb = ResourceBundle.getBundle("resource");//开发时,每次重新读取配置文件
		return rb.getString(name);
	}
}