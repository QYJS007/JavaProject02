package cg.pack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cg.utils.ResourceConfig;

public class PackMain {
	private static Logger logger = Logger.getLogger(PackMain.class);  
	/*
	 * 需求: 把项目中指定的文件摘出来; 进行增量部署. 
		 1. 读取配置文件; (项目路径,项目要放的路径)
	 	 2. 读取项目中指定文件
	 	 		指定文件: 写在文件中; 
	 	 		读取文件中的文件路径; 
		 3. 拷贝到指定文件夹,
	 */
	public static void main(String[] args) {
		// 
		String fromFile = ResourceConfig.getValue("now_project").trim()+ResourceConfig.getValue("fileName").trim();
		List<String> fileNameList = getFileName(fromFile);
		logger.info(fileNameList);
		for (String fileName : fileNameList) {
			List<File> path = getPath(fileName);
			for (File file : path) {
				CopyFile copyFile = new CopyFile();
				try {
					copyFile.doCopy(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		logger.info("同步结束");
	}

	/**
	 * txt中准备打包的文件读到list集合中去.
	 * @param fromFile
	 * @return
	 */

	private static List<String> getFileName(String fromFile) {
		List<String> arrayList = new ArrayList<String>();
		try {
			File file = new File(fromFile);
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
		return arrayList;
	}

	public static List<File> getPath(String fileName) {
		String firstStr = null;
		String realName = null;
		if(fileName.indexOf("/")>0){
			//切割路径和文件明; 
			int lastIndex = fileName.lastIndexOf('/');
			realName = fileName.substring(lastIndex+1);
			firstStr = fileName.substring(0,lastIndex+1);
		}else{
			logger.error("文件路径有误");
		}
		String project_path = ResourceConfig.getValue("project_path");
		String  filePath = project_path.trim()+firstStr.trim();

		File f = null;
		File[] paths = null;
		List<File> filePathList = new ArrayList<File>();
		try{      
			// create new file
			f = new File(filePath.trim());
			MyFilter myFilter = new  MyFilter(realName);
			paths = f.listFiles(myFilter);
			if(paths!=null){

				for(File path:paths){
					//  System.out.println(path);
					filePathList.add(path);
				}
			}
			return filePathList;
		}catch(Exception e){
			logger.error("获得文件路径错误", e);
			return null;
		}
	}
}
