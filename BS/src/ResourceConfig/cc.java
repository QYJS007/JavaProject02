package ResourceConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cg.utils.ResourceConfig;

public class cc {
	private static Logger logger = Logger.getLogger(cc.class);  
	public static void main(String[] args) {
		
		String value = ResourceConfig.getValue("project_path");
		System.out.println(value);
		String project_name = ResourceConfig.getValue("project_name");
		System.out.println(project_name);
		String update_path = ResourceConfig.getValue("update_path");
		System.out.println(update_path);
		// 记录debug级别的信息    
        //logger.debug("This is debug message.");    
        // 记录info级别的信息    
       // logger.info("This is info message.");    
        // 记录error级别的信息    
       // logger.error("This is error message.");    

		/*
		 * F:/A_temp/qtrip365/SRC/
			qtrip_business
			d:/AAA_updatefiles/
		 */
		
		//ResourceConfig.cc
		//cg.project.A
		/*
		 * /qtrip_enterprise/app/views/zc/vehicletracking.html
		 * controllers.CommonAction
		 * /qtrip_platform/app/controllers/VehicleBusinessAction.java
		 * controllers.VehicleBusinessAction
		 */
	//	String filePath=" /qtrip_platform/app/controllers/VehicleBusinessAction.java";
		/*
		 * 文本文件读取的大致过程如下：

		1.构建文件对象，
		2.使用文件对象构造Reader对象可以是FileReader、InputStreamReader等
		3.使用Reader对像构建BufferedReader对象(主要使用其readLine()方法，用于按行读取文件)
		4.按行读取文件，将每行获取到的字符串进行处理。
		 */
		
		//public static int[] toArrayByInputStreamReader1(String name) {
			// 使用ArrayList来存储每行读取到的字符串
			List<String> arrayList = new ArrayList<String>();
			try {
				File file = new File("E:\\A_coding\\workspaces01\\BS\\src\\project\\project.txt");
				InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
				BufferedReader bf = new BufferedReader(inputReader);
				// 按行读取字符串
				String str;
				while ((str = bf.readLine()) != null) {
					arrayList.add(str);
				}
				bf.close();
				inputReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(" 读取要打包文件出错:",e );
			}
			//logger.info(arrayList);
			/*for (String string : arrayList) {
				
				System.out.println("dayingd"+string);
			}*/

	}
}
