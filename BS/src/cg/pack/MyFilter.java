package cg.pack;

import java.io.File;
import java.io.FilenameFilter;

public class MyFilter implements FilenameFilter {

	private String realName;
	public MyFilter(String realName) {
		this.realName = realName;
	}
	public boolean accept(File dir, String name) {
		//if(name.lastIndexOf('.')>0){
			//int lastIndex = name.lastIndexOf('.');
			//String str = name.substring(0,lastIndex);
			//if(str.startsWith(realName)){
			if(realName.indexOf(name)>-1){
				System.out.println("准备打印的文件:"+ realName);
				return true;
			}
		//}
		return false;
	}


}
