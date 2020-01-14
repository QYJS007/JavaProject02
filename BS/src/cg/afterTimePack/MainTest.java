package cg.afterTimePack;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cg.utils.DateUtils;
import cg.utils.ResourceConfig;

public class MainTest {
	// 要copy 的文件
	static List<File> filePathList = new ArrayList<File>();
	static final String ROOT_PATH = "F:/A_temp/qtrip365/SRC"; // 项目所在路径
	static List<String> IGNORE_DIR_PATH = new ArrayList<String>();// 忽略的目录路径
	static List<String> CONTAINS_FILE_TYPE = new ArrayList<String>();// 包含的文件类型
	static {
		// 忽略的目录
		//IGNORE_DIR_PATH.add(ROOT_PATH + "/template");
		//IGNORE_DIR_PATH.add(ROOT_PATH + "/spring");

		CONTAINS_FILE_TYPE.add(".java");
		CONTAINS_FILE_TYPE.add(".txt");
		CONTAINS_FILE_TYPE.add(".sql");
		CONTAINS_FILE_TYPE.add(".js");
		CONTAINS_FILE_TYPE.add(".html");
		CONTAINS_FILE_TYPE.add(".properties");
	}
	 String lastdate; 
	/*public static void main(String[] args) {
	 * 不用读取文件中的路径;
	 * 根据时间节点,去获取某个时间节点之后的文件; 

		getPath("2018-12-04-08-00-00");
		for (File file : filePathList) {
			System.out.println(file);
		}


	}*/

	
	public MainTest(String lastdate) {
		this.lastdate = lastdate;
	}

	public  List<File>  getPath() {
		String project_path = ResourceConfig.getValue("project_path");

		IGNORE_DIR_PATH.add(project_path+"template");
		IGNORE_DIR_PATH.add(project_path+"qtrip_task");
		IGNORE_DIR_PATH.add(project_path+"qtrip_superviseservice");
		IGNORE_DIR_PATH.add(project_path+"qtrip_pub");
		IGNORE_DIR_PATH.add(project_path+"config");
		IGNORE_DIR_PATH.add(project_path+"qtrip_enterprise");
		Date strToDateTime = null;
		try {
			strToDateTime = DateUtils.parseDate(lastdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		File f =  new File(project_path);
		TimeFilter timeFilter = new TimeFilter(strToDateTime);
		getFilePath(f,timeFilter);
		return filePathList;

	}


	public  void getFilePath(File parent, FilenameFilter timeFilter){

		File[] files = parent.listFiles(timeFilter);
		for (File file : files) {
			if (file.isDirectory()) {
				String path = tranPath(file.getPath());
				if ( !IGNORE_DIR_PATH.contains(path)) {
					getFilePath(file,timeFilter);
				}
			} else {
				String fileName = file.getName();
				Date lastEdit = new Date(file.lastModified());
				Date parseDate = new Date();
				try {
					parseDate = DateUtils.parseDate(lastdate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (lastEdit.after(parseDate)) {
					if(verifyFileType(fileName)){
						filePathList.add(file);
					}
				}
			}
		}
	}
	
	private static String tranPath(String oldPath) {
		return oldPath.replace(File.separator, "/");
	}
	
	private static boolean verifyFileType(String fileName) {
		Boolean flag = false; 
		for (String string : CONTAINS_FILE_TYPE) {
			if(flag){
				return flag;
			}
			flag = fileName.endsWith(string);
		}
		return flag;
	}
}

