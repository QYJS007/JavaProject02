package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;

public class CompressUtils {
	/**
	 * 压缩文件zip
	 * @param filePath 要压缩的文件路径
	 * @param savePath 压缩文件的保存路径
	 */
	public static void compressZip(String filePath,String savePath){
		File file = new File(filePath);
		if(!file.exists()){
			throw new RuntimeException("文件不存在:"+filePath);
		}
		Project prj = new Project();	
		Zip zip = new Zip();	
		zip.setProject(prj);	
		zip.setDestFile(new File(savePath));	
		FileSet fileSet = new FileSet();	
		fileSet.setProject(prj);	
		fileSet.setDir(file);	
		//fileSet.setIncludes("**/*.java"); //包括哪些文件或文件夹 eg:zip.setIncludes("*.java");	
		//fileSet.setExcludes(...); //排除哪些文件或文件夹    
		zip.addFileset(fileSet);	
		zip.execute();
	}
	
	/**
	 * 解压文件zip
	 * @param filePath 要压缩的文件路径
	 * @param savePath 压缩文件的保存路径
	 */
	public static void deCompressZip(String filePath,String savePath){
		File file = new File(filePath);
		if(!file.exists()){
			throw new RuntimeException("文件不存在:"+filePath);
		}
		Project prj = new Project();	
		Expand expand = new Expand();
		expand.setProject(prj);
		expand.setTaskType("unzip");
		expand.setTaskName("unzip");

		expand.setSrc(new File(filePath));
		expand.setDest(new File(savePath));  
		expand.execute();
	}
	
	/**
	 * 打包文件,或文件夹
	 * @author likaihao
	 * @date 2016年6月16日 上午11:49:35
	 * @param inputPath 要打包的文件或文件夹路径
	 * @param targetPath tar包名称
	 */
	private static void tar(String inputPath, String targetPath, boolean isGzip) {
		File inputFile = new File(inputPath);
		if(!inputFile.exists()){
			throw new RuntimeException("文件不存在:"+inputPath);
		}
		File targetFile = new File(targetPath);
		if(targetFile.exists()){
			throw new RuntimeException("文件已存在:"+targetPath);
		}
		
		TarOutputStream out = null;
		try {
			if(isGzip){
				out = new TarOutputStream(new GZIPOutputStream(new FileOutputStream(targetFile)));
			}else{
				out = new TarOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)));
			}
			
			out.setLongFileMode(TarOutputStream.LONGFILE_GNU);// 如果不加下面这段，当压缩包中的路径字节数超过100 byte时，就会报错
			
			//如果压缩文件需要最外层文件夹名称,则在tar2的第二个参数中加上base
			//String base = inputPath.substring(inputPath.lastIndexOf("/") + 1);
			tar2(inputFile, out, "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

	//打包文件,私有递归方法
	private static void tar2(File inputFile, TarOutputStream out, String base) throws Exception {
		if(inputFile.isDirectory()){
			File[] fileList = inputFile.listFiles();
			base = base.length() == 0 ? "" : base + "/";
			for (File file : fileList) {
				tar2(file, out, base + file.getName());
			}
		}else{
			TarEntry tarEntry = new TarEntry(base);
			//如果base为空,代表只压缩一个文件,设置文件名称
			if(base.length()==0){
				tarEntry.setName(inputFile.getName());
			}
			// 设置打包文件的大小，如果不设置，打包有内容的文件时，会报错
			tarEntry.setSize(inputFile.length());
			out.putNextEntry(tarEntry);
			FileInputStream in = new FileInputStream(inputFile);
			try {
				byte[] bytes = new byte[1024*200];
				int len = 0;
				while ((len = in.read(bytes)) != -1) {
					out.write(bytes, 0, len);
				}
			} finally {
				in.close();
				out.closeEntry();
			}
		}
	}
	
	/**
	 * 解压tar
	 * @author likaihao
	 * @date 2016年6月16日 下午3:37:54
	 * @param tarPath tar文件位置
	 * @param targetPath 目标位置,不带文件名
	 * @throws IOException
	 */
	private static void unTar(String tarPath, String targetPath, boolean isUnGzip) {
		File tarFile = new File(tarPath);
		if(!tarFile.exists()){
			throw new RuntimeException("文件不存在:"+tarFile);
		}
		File targetFile = new File(targetPath);
		if(!targetFile.exists()){
			throw new RuntimeException("目标位置不存在:"+targetPath);
		}
		
		TarInputStream tarIn = null;
		try{
			if(isUnGzip){
				tarIn = new TarInputStream(new GZIPInputStream(new FileInputStream(tarFile)));
			}else{
				tarIn = new TarInputStream(new FileInputStream(tarFile));
			}
			
			TarEntry entry = null;
			while( (entry = tarIn.getNextEntry()) != null ){
				if(entry.isDirectory()){
					//是目录
					new File(targetPath + "/" + entry.getName()).mkdir();
				}else{
					//是文件
					File tmpFile = new File(targetPath + "/" + entry.getName());
					//先创建父文件夹
					if(!tmpFile.getParentFile().exists()){
						tmpFile.getParentFile().mkdirs();
					}
					//写入文件
					OutputStream out = null;
					try{
						out = new FileOutputStream(tmpFile);
						int len = 0;
						byte[] bytes = new byte[1024 * 200];
						while((len = tarIn.read(bytes)) != -1){
							out.write(bytes, 0, len);
						}
					} finally{
						if(out!=null){
							out.close();
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			try{
				tarIn.close();
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 打包文件或文件夹,gzip
	 * @author likaihao
	 * @date 2016年6月16日 上午11:49:35
	 * @param inputPath 要打包的文件或文件夹路径
	 * @param gzipPath tar包名称
	 */
	public static void compressGzip(String inputPath, String gzipPath) {
		tar(inputPath,gzipPath,true);
	}
	
	/**
	 * 解压gzip
	 * @author likaihao
	 * @date 2016年6月16日 下午4:29:33
	 * @param gzipPath
	 * @param targetPath
	 */
	public static void deCompressGzip(String gzipPath,String targetPath){
		unTar(gzipPath,targetPath,true);
	}

	public static void main(String[] args) {
//		String inputPath = IOUtils.getHomeDirectoryPath()+"/123";
//		String gzipPath = IOUtils.getHomeDirectoryPath()+"/test.tar.gz";
//		String targetPath = IOUtils.getHomeDirectoryPath()+"/123_ungzip";
		
//		if(new File(gzipPath).exists()){
//			new File(gzipPath).delete();
//		}
		//compressGzip(inputPath, gzipPath);
//		deCompressGzip(gzipPath,targetPath);
	}
}