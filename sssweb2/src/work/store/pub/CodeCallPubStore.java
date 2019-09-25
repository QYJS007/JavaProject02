package work.store.pub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.vo.RemoteServer;
import utils.IOUtils;
import utils.LoggerUtils;
import utils.SSHUtils;
import utils.StringUtils;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class CodeCallPubStore {
	
	
	/**
  	 * 分割大的文本文件
  	 * @author likaihao
  	 * @date 2016年3月3日 上午11:37:51
  	 * @param path 文本文件路径
  	 * @param savePath 保存路径
  	 * @param blocksize 每个文件的大小
  	 */
  	public void splitBigText(String path,String savePath,long blocksize){
		try {
			blocksize = blocksize * 1024 * 1024;
			File file = new File(path);
			FileInputStream in = new FileInputStream(file);
			String suffix = IOUtils.getSuffix(path);
			
			int i = 1; //第一个文件
			String newFilePath = savePath + "/" + i + "."+suffix;
			OutputStream out = new FileOutputStream(newFilePath);
		
			int size = 0;//未写入的大小
			int len = -1;
			byte[] b = new byte[1024 * 100];
			while( (len = in.read(b))!=-1){
				out.write(b,0,len);
				size += len;
				
				if(size >= blocksize){
					i++;
					out.close();
					newFilePath = savePath + "/" + i + "."+suffix;
					out = new FileOutputStream(newFilePath);
					System.out.println("保存完成"+ (i-1) + "." + suffix + "("+(size/1024/1024)+"MB)");
					size -= blocksize;
				}
			}
			
			in.close();
			out.close();
			System.out.println("保存完成"+ i + "." + suffix + "("+(size/1024)+"KB)");
			System.out.println("完成");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
  	
  	/**
	 * 删除.svn文件夹
	 * @author likaihao
	 * @date 2016年3月23日 下午5:20:11
	 * @param path
	 */
	public void deleteSVNFile(String path){
		File file = new File(path);
		if(file.isDirectory()){
			for(File f : file.listFiles()){
				if(f.getName().equals(".svn")){
					LoggerUtils.info("删除:"+f.getAbsolutePath());
					IOUtils.deleteFileOrDir(f.getAbsolutePath());
				}else{
					//递归
					deleteSVNFile(f.getAbsolutePath());
				}
			}
		}
	}
	
	/**
	 * 按文件名称比较两个文件夹的区别
	 * @author likaihao
	 * @date 2016年4月7日 上午9:40:53
	 * @param path1
	 * @param path2
	 */
	public void diffDirFileName(String path1,String path2,boolean isExport){
		List<File> list1 = IOUtils.getFileList(path1, false, null, null, null);
		List<File> list2 = IOUtils.getFileList(path2, false, null, null, null);
		Map<String,File> fileMap1 = new HashMap<String,File>();
		Map<String,File> fileMap2 = new HashMap<String,File>();
		
		String exportPath = IOUtils.getHomeDirectoryPath()+"/比较导出_"+StringUtils.getRandomStr();
		String exportPath1 = exportPath + "/path1多余的";
		String exportPath2 = exportPath + "/path2多余的";
		
		List<String> list1_name = new ArrayList<String>();
		for(File f : list1){
			list1_name.add(f.getName());
			fileMap1.put(f.getName(), f);
		}
		
		List<String> list2_name = new ArrayList<String>();
		for(File f : list2){
			list2_name.add(f.getName());
			fileMap2.put(f.getName(), f);
		}
		
		//查看list1多的
		System.out.println(path1+"多余的:");
		List<String> list1_name_2 = new ArrayList<String>(list1_name);
		list1_name_2.removeAll(list2_name);
		for(String name : list1_name_2){
			System.out.println(name);
			//导出
			if(isExport){
				IOUtils.copyFile(fileMap1.get(name).getAbsolutePath(), exportPath1+"/"+name);
			}
		}
		
		
		System.out.println("\n\n\n\n\n\n---------------------------------------\n\n\n\n\n\n");
		
		//查看list2多的
		System.out.println(path2+"多余的:");
		list2_name.removeAll(list1_name);
		for(String name : list2_name){
			System.out.println(name);
			//导出
			if(isExport){
				IOUtils.copyFile(fileMap2.get(name).getAbsolutePath(), exportPath2+"/"+name);
			}
		}
	}
	
	/**
	 * 在服务器之间拷贝文件
	 * @author likaihao
	 * @date 2016年10月17日 上午10:22:53
	 * @param sendServer 发送端服务器信息
	 * @param receiveServer 接收端服务器信息
	 * @param sendFileParent 发送端目标所在目录
	 * @param sendFileList 发送端目标list
	 * @param receiveFileParent 接收端目标存放目录
	 * @param isDeleteReceiveFile 是否提前删除接收端目标
	 */
	public void serverCopy(RemoteServer sendServer, RemoteServer receiveServer, String sendFileParent, String sendFileStr, String receiveFileParent, boolean isDeleteReceiveFile){
		String[] sendFileArr = sendFileStr.split(",");
		
		//压缩文件名称
		String gzFileName = "serverCopy_"+StringUtils.getRandomStr()+".tar.gz";
		
		//准备发送端执行的命令
		List<String> sendServerCommondList = new ArrayList<String>();
		sendServerCommondList.add("cd "+sendFileParent);
		sendServerCommondList.add("tar -czvf "+gzFileName+" "+StringUtils.join(sendFileArr, " "));
		sendServerCommondList.add("scp "+gzFileName+" "+receiveServer.getUsername()+"@"+receiveServer.getHostname()+":"+receiveFileParent);
		sendServerCommondList.add(receiveServer.getPassword());
		sendServerCommondList.add("rm -f "+gzFileName);
		
		//准备接收端执行的命令
		List<String> receiveServerCommondList = new ArrayList<String>();
		receiveServerCommondList.add("cd "+receiveFileParent);
		if(isDeleteReceiveFile){
			receiveServerCommondList.add("rm -rf "+StringUtils.join(sendFileArr, " "));
		}
		receiveServerCommondList.add("tar -xzvf "+gzFileName);
		receiveServerCommondList.add("rm -f "+gzFileName);
		
		//执行发送端的命令,并打印执行过程
		String returnStr = execCommandByClient(sendServer.getHostname(), sendServer.getUsername(), sendServer.getPassword(), System.out, sendServerCommondList);
		IOUtils.writeFileReplace(IOUtils.getHomeDirectoryPath()+"/sendServer.txt", returnStr);
		
		System.out.println("\r\n\r\n-----------------------\r\n\r\n");
		
		//执行接收端的命令,并打印执行过程
		returnStr = execCommandByClient(receiveServer.getHostname(), receiveServer.getUsername(), receiveServer.getPassword(), System.out, receiveServerCommondList);
		IOUtils.writeFileReplace(IOUtils.getHomeDirectoryPath()+"/receiveServer.txt", returnStr);
		
		System.out.println("\r\n执行完成,服务器输出保存在桌面");
	}
	
	/**
	 * 描述:服务器复制项目
	 * @author: 李凯昊
	 * @date:2017年3月29日 下午4:17:37
	 * @param ip
	 * @param username
	 * @param password
	 * @param projectPath
	 */
	public void serverCopyProject(String ip,String username,String password,String projectPath){
		File projectDirFile = new File(projectPath);
		String projectParentPath = projectDirFile.getParent().replace("\\", "/");
		String newProjectName = projectDirFile.getName()+"_test_"+(int)(Math.random()*1000);
		String newProjectPath = projectParentPath + "/" + newProjectName;
		
		List<String> commandList = new ArrayList<String>();
		commandList.add("mkdir "+newProjectPath);
		commandList.add("cd "+projectPath);
		commandList.add("cp -r app conf dbnode key lib public "+newProjectPath);
		
		new CodeCallPubStore().execCommandByClient(ip, username, password, System.out, commandList);
		System.out.println("\r\n执行完成,日志保存在桌面,新项目路径为:"+newProjectPath);
	}
	
	/**
	 * 通过模拟一个终端执行命令列表
	 * @author likaihao
	 * @date 2016年4月27日 下午6:52:14
	 * @param hostname 地址
	 * @param username 用户名
	 * @param password 密码
	 * @param out 输出流,会打印服务器返回内容,为空表示不打印
	 * @param commandList 命令列表
	 * @param abortRe 退出字符串的正则(遇到匹配的字符串就退出)
	 * @return linux输出
	 */
	private String execCommandByClient(String hostname,String username,String password, OutputStream out,List<String> commandList){
		//建立连接
		Connection conn = SSHUtils.getSSHConnection(hostname,username,password);
		Session ssh = null;
		try{
			ssh = conn.openSession();
			//模拟一个终端(将终端的宽度设置大一点,否则结果会出现 换行和\b)
			//ssh.requestDumbPTY();
			//ssh.requestPTY("vt100", 500, 100, 2000, 1000, null);
			/**
			 * 终端类型表示显示格式,可在/usr/share/terminfo下查找
			 * 终端宽度 可影响输入字符的显示, 过短会造成输入的命令换行显示
			 * dumb执行ls没有样式,但是当宽度过大时会效率下降
			 * vt100执行ls有样式,宽度过大时不会效率下降
			 * 暂时使用vt200
			 */
			ssh.requestPTY("vt200", 5000, 100, 10000, 200, null);
			ssh.startShell();
			//获得输入输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(ssh.getStdout()));
			StringBuilder builder = new StringBuilder();
			OutputStream sshOut = ssh.getStdin();
			//执行完成的正则
			String okRe = "(\\[.*\\][#$] )|(.*?'s password: )";
			
			//执行命令
			int num = 0;
			int i_char = -1;
			int lastLnIndex = 0;
			boolean isFirst = true;
			while( (i_char = reader.read())!=-1 ){
				char c = (char) i_char;
				builder.append(c);
				//打印输出结果
				if(out!=null && c=='\n'){
					out.write(builder.substring(lastLnIndex).getBytes());
					lastLnIndex = builder.length();
				}
				//如果遇到[root@elk ~]# ,则执行下一个命令
				String lastLine = builder.substring(builder.lastIndexOf("\n")+1);
				if(lastLine.matches(okRe)){
					//如果命令执行完,跳出循环
					if(num == commandList.size()){
						out.write((builder.substring(lastLnIndex)+"\r\n").getBytes());
						break;
					}
					
					//发送命令
					String command = commandList.get(num)+"\r";
					sshOut.write(command.getBytes());
					num++;
				}else if(isFirst && lastLine.contains("Are you sure you want to continue connecting (yes/no)? ")){
					isFirst = false; //这个只能有一次,否则总是读取自己输入的,死循环
					sshOut.write("yes\r".getBytes());
				}
			}
			return builder.toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(ssh!=null){
				ssh.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	
	/**
	 * 将\t填充为空格,批量从文件读取,向其它地方写入
	 * @author likaihao
	 * @date 2016年10月21日 上午10:03:53
	 * @param srcPath
	 * @param targetPath
	 */
	public void ltToSpaceBatch(String srcPath,String targetPath){
		//创建目标文件夹
		File targetDir = new File(targetPath);
		if(!targetDir.exists()){
			targetDir.mkdirs();	
		}
		
		File srcDir = new File(srcPath);
		for(File file : srcDir.listFiles()){
			if(file.isFile()){
				//读取文件
				String content = IOUtils.readFile(file.getAbsolutePath(), IOUtils.getFileEncode(file));
				//替换内容
				content = new StringHandlePubStore().ltToSpace(content, 4);
				//写入文件
				String targetFilePath = new File(targetDir,file.getName()).getAbsolutePath();
				IOUtils.writeFileReplace(targetFilePath, content, "utf-8");
			}
		}
	}
	
	/**
	 * 修改文件编码
	 * @author likaihao
	 * @date 2016年12月30日 上午9:13:34
	 * @param path
	 * @param fileNamePattern
	 * @param noFileNamePattern
	 * @param lastEncoding
	 * @param newEncoding
	 * @param isOnlyShowFileList 是否只是显示文件列表
	 */
	public void changeEncoding(String path,String fileNamePattern,String noFileNamePattern,String lastEncoding,String newEncoding,boolean isOnlyShowFileList){
		//建议编码
		try {
			"".getBytes(lastEncoding);
		} catch (Exception e) {
			System.out.println("指定编码未识别:"+lastEncoding);
			return;
		}
		try {
			"".getBytes(newEncoding);
		} catch (Exception e) {
			System.out.println("指定编码未识别:"+newEncoding);
			return;
		}
		
		try {
			String[] fileNamePatternArr = fileNamePattern.split(",");
			String[] noFileNamePatternArr = noFileNamePattern.split(",");
			
			//获取文件
			List<File> fileList = IOUtils.getFileListByPattern(path, null, null, fileNamePatternArr, noFileNamePatternArr);
			
			if(isOnlyShowFileList){
				//打印
				for(File file : fileList){
					System.out.println(file.getAbsolutePath());
				}
				return;
			}
			
			//修改编码
			for(File file : fileList){
				String content = IOUtils.readFile(file.getAbsolutePath(),lastEncoding);
				content = new String(content.getBytes(lastEncoding),newEncoding);
				IOUtils.writeFileReplace(path, content, newEncoding);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取文件列表
	 * @author likaihao
	 * @date 2016年12月30日 上午9:15:02
	 * @param path
	 * @param fileNamePattern
	 * @param noFileNamePattern
	 */
	public void getFileListByPattern(String path,String fileNamePattern,String noFileNamePattern){
		String[] fileNamePatternArr = fileNamePattern.split(",");
		String[] noFileNamePatternArr = noFileNamePattern.split(",");
		
		//获取文件
		List<File> fileList = IOUtils.getFileListByPattern(path, null, null, fileNamePatternArr, noFileNamePatternArr);
		
		//打印
		for(File file : fileList){
			System.out.println(file.getAbsolutePath());
		}
	}
	
	
	/**
	 * 文件重命名, 用于.开头的window不能重命名的文件
	 * @author likaihao
	 * @date 2017年2月9日 上午11:45:17
	 * @param path 路径
	 * @param oldName 原文件名称
	 * @param newName 新文件名称
	 */
	public void fileRename(String path, String oldName, String newName){
		boolean result = new File(path, oldName).renameTo(new File(path, newName));
		if(result){
			System.out.println("重命名成功");
		}else{
			System.out.println("重命名失败");
		}
	}
	
	
}