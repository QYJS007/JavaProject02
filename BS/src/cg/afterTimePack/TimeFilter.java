package cg.afterTimePack;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

public class TimeFilter implements FilenameFilter{

	private Date deadline; 


	public TimeFilter(Date deadline) {
		this.deadline = deadline;
	}


	public boolean accept(File file, String name) {
		// 不是隐藏文件且,文件 设置的日期之后的文件或者文件目录文件夹
		if(file.isDirectory()){
			return this.validFile(file);
		}else{
			//if(){
			return this.validFile(file); 
			//}
		}
		//return !file.isHidden() && this.validFile(file);
		//return false;
	}


	private boolean validFile(File file) {
		Date lastEdit = new Date(file.lastModified());
		if (lastEdit.after(deadline)) {
			////System.out.println("lastEdit: "+lastEdit );
			//System.out.println("deadline: "+deadline );
			return true;
		}
		return false;
	}

}
