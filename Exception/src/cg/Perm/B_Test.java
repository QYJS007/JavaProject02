package cg.Perm;

import java.io.File;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class B_Test {

	/**
	 * 
	 * @ClassName:OOMTest
	 * @Description:模拟类加载溢出（元空间oom）
	 * @author diandian.zhang
	 * @date 2017年4月27日上午9:45:40
	 */
	//public class OOMTest {  
		public static void main(String[] args) {  
			try {  
				//准备url  
				//URL url = new File("D:/58workplace/11study/src/main/java/jdk8").toURI().toURL();  
				//URL url = new File("E:/A_coding/workspaces01/BS/build/classes/cg").toURI().toURL();
				URL url = new File("F:/G_workspace/qtrip365/SRC/qtrip_platform/app/bp").toURI().toURL();
				URL[] urls = {url};  
				//获取有关类型加载的JMX接口  
				ClassLoadingMXBean loadingBean = ManagementFactory.getClassLoadingMXBean();  
				//用于缓存类加载器  
				List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();  
				while (true) {  
					//加载类型并缓存类加载器实例  
					ClassLoader classLoader = new URLClassLoader(urls);  
				//	ClassLoader classLoader = B_Test.class.getClassLoader();
					classLoaders.add(classLoader);  
					classLoader.loadClass("cg.Perm.A_Test");  //cg.Perm.A_Test
					classLoader.loadClass("cg.Perm.Person");  //cg.Perm.A_Test
					
					//显示数量信息（共加载过的类型数目，当前还有效的类型数目，已经被卸载的类型数目）  
					System.out.println("total: " + loadingBean.getTotalLoadedClassCount());  
					System.out.println("active: " + loadingBean.getLoadedClassCount());  
					System.out.println("unloaded: " + loadingBean.getUnloadedClassCount());  
				}  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
	}  