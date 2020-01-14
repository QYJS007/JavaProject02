package cg.afterTimePack;

import java.io.File;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		 
		List<File> filePathList = new  MainTest("2018-12-04-08-00-00").getPath();
		for (File file : filePathList) {
			System.out.println(file);
		}
	}
}
