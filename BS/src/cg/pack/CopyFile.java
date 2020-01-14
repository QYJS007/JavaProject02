package cg.pack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import cg.utils.ResourceConfig;

public class CopyFile {

	/**
	 * 拷贝文件
	 * 
	 * @throws IOException
	 */
	public   void doCopy(File file) throws IOException {
		File toFile = this.getToFile(file);
		//F:\G_workspace\qtrip365\SRC\qtrip_business\app\controllers\login\zx\OrderInfoAction.java
		//d:\AAA_updatefiles\qtrip_business\app\controllers\login\zx\OrderInfoAction.java
		//String path = this.tranPath(file.getPath());
		
		//toFile.getParentFile().mkdirs();
		copyFile(file, toFile);

	}

	@SuppressWarnings("resource")
	private void copyFile(File srcFile, File destFile) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		if ((destFile.getParentFile() != null) && (!destFile.getParentFile().exists()) && (!destFile.getParentFile().mkdirs())) {
			throw new IOException("Destination '" + destFile + "' directory cannot be created");
		}

		if ((destFile.exists()) && (!destFile.canWrite())) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}

		if ((destFile.exists()) && (destFile.isDirectory())) {
			throw new IOException("Destination '" + destFile + "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size();// 字节数此通道的文件的当前大小，以字节为单位 
			long pos = 0L;
			long count = 0L;
			while (pos < size) {
				/*
				 * 输入 流 input-的字节的大小  大于52428800L ; 每次52428800L大; 小于52428800L 就以实际的大小为主
				 * 
				  * src - 源通道
					position - 文件中的位置，从此位置开始传输；必须为非负数
					count - 要传输的最大字节数；必须为非负数 
					返回：
					实际已传输的字节数，可能为零 

				 */
				count = size - pos > 52428800L ? 52428800L : size - pos;
				pos += output.transferFrom(input, pos, count);
				
				
				//FileInputStream fis = new FileInputStream("致青春.mp3");				//创建输入流对象,关联致青春.mp3
				//FileOutputStream fos = new FileOutputStream("copy.mp3");			//创建输出流对象,关联copy.mp3
				BufferedInputStream bis = new BufferedInputStream(fis);				//创建缓冲区对象,对输入流进行包装让其变得更加强大
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				
				int b;
				while((b = bis.read()) != -1) {
					bos.write(b);
				}
			}
		} finally {
			if (output != null)
				output.close();
			if (fos != null)
				fos.close();
			if (input != null)
				input.close();
			if (fis != null)
				output.close();
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
		}

		destFile.setLastModified(srcFile.lastModified());
	}


	/**
	 * 获取目的文件对象
	 * 
	 * @param fromFile
	 * @return
	 * @throws IOException
	 */
	public File getToFile(File fromFile) throws IOException {
		String project_path = ResourceConfig.getValue("project_path");
		System.out.println(project_path);
		//String project_name = ResourceConfig.getValue("project_name");
		//System.out.println(project_name);
		String update_path = ResourceConfig.getValue("update_path");
		String path = this.tranPath(fromFile.getPath()).replaceFirst(project_path, update_path);
		return new File(path);
	}

	/**
	 * 转换路径, 将\转换为/
	 * 
	 * @return
	 */
	private String tranPath(String oldPath) {
		return oldPath.replace(File.separator, "/");
	}
}
