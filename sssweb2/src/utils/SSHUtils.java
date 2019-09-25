package utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import utils.model.concurrent.Job;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

public class SSHUtils {
	/**
	 * 文件上传
	 * @param hostname
	 * @param username
	 * @param password
	 * @param remoteFilePath
	 * @param localTargetDirectory
	 */
	public static void uploadLinuxFile(String hostname,String username,String password,String localFilePath,String remoteTargetDirectory){
		if(isFortressHostname(hostname)){
			Map<String,String> connInfo = getFortressConnInfo(hostname);
			SFTPUtils.uploadLinuxFile(connInfo.get("hostname"), new Integer(connInfo.get("port")), connInfo.get("username"), connInfo.get("password"), localFilePath, remoteTargetDirectory);
			return;
		}
		Connection conn = getSSHConnection(hostname,username,password);
		try{
			SCPClient scpClient = conn.createSCPClient();
			scpClient.put(localFilePath, remoteTargetDirectory);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	/**
	 * 文件下载
	 * @param hostname
	 * @param username
	 * @param password
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public static void downloadLinuxFile(String hostname,String username,String password,String remoteFilePath,String localFilePath){
		Connection conn = getSSHConnection(hostname,username,password);
		try{
			downloadLinuxFile(conn, remoteFilePath, localFilePath);
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	/**
	 * 文件下载
	 * @param hostname
	 * @param username
	 * @param password
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public static void downloadLinuxFile(Connection conn,String remoteFilePath,String localFilePath){
		if(isFortressHostname(conn.getHostname())){
			Map<String,String> connInfo = getFortressConnInfo(conn.getHostname());
			SFTPUtils.downloadLinuxFile(connInfo.get("hostname"), new Integer(connInfo.get("port")), connInfo.get("username"), connInfo.get("password"), remoteFilePath, localFilePath);
			return;
		}
		try{
			File file = new File(localFilePath);
			if(file.isFile() && file.exists()){
				throw new RuntimeException("文件已存在:"+file.getAbsolutePath());
			}
			LoggerUtils.info("下载文件开始:"+conn.getHostname()+","+remoteFilePath);
			SCPClient scpClient = conn.createSCPClient(); 
			OutputStream out = new FileOutputStream(file);
			scpClient.get(remoteFilePath, out);
			out.close();
			LoggerUtils.info("下载文件完成:"+conn.getHostname()+","+remoteFilePath);
		}catch(Exception e){
			throw new RuntimeException("下载文件失败",e);
		}
	}
	
	/**
	 * 文件下载(先压缩文件)
	 * @param hostname
	 * @param username
	 * @param password
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public static void downloadLinuxFileCompress(String hostname,String username,String password,String remoteFilePath,String localFilePath){
		Connection conn = getSSHConnection(hostname,username,password);
		try {
			downloadLinuxFileCompress(conn, remoteFilePath, localFilePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	/**
	 * 文件下载(先压缩文件)
	 * @param hostname
	 * @param username
	 * @param password
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public static void downloadLinuxFileCompress(Connection conn,String remoteFilePath,String localFilePath){
		try {
			String command = null;
			String result = null;
			String randomStr = StringUtils.getRandomStr();
			String zipRemoteFilePath = remoteFilePath+"."+randomStr+".tar.gz";
			String zipLocalFilePath = localFilePath+"."+randomStr+".tar.gz";
			
			//压缩服务器文件(分两条命令是ok的,1条会显示警告)
			File remoteFile = new File(remoteFilePath);
			String[] commandArr = new String[2];
			commandArr[0] = "cd "+remoteFile.getParent().replace("\\", "/");
			commandArr[1] = "tar -czvf "+zipRemoteFilePath+" "+remoteFile.getName();
			result = execLinuxCommandByClient(conn,null,Arrays.asList(commandArr),null);
			if(result.contains("tar:")){
				throw new RuntimeException("压缩服务器文件失败,返回结果:"+result);
			}
			
			//下载
			downloadLinuxFile(conn, zipRemoteFilePath, zipLocalFilePath);
			//LoggerUtils.info("下载文件完成");
			
			//删除服务器文件
			command = "rm -f "+zipRemoteFilePath;
			result = execLinuxCommandByClient(conn,null,Arrays.asList(command),null);
			//LoggerUtils.info("删除服务器文件,返回结果:"+result);
			
			//解压本地文件
			String tempPath = IOUtils.getHomeDirectoryPath()+"/"+IOUtils.getFileName(localFilePath)+"."+StringUtils.getRandomStr();
			new File(tempPath).mkdir();
			CompressUtils.deCompressGzip(zipLocalFilePath, tempPath);
			//LoggerUtils.info("解压本地文件完成");
			
			//删除本地压缩文件
			boolean success = new File(zipLocalFilePath).delete();
			if(!success){
				LoggerUtils.error("删除本地压缩文件失败");
			}
			
			//移动本地文件
			String tempFileName = tempPath +"/"+ IOUtils.getFileName(remoteFilePath);//如果下载的是文件,则是文件的名字.否则不存在
			if(new File(tempFileName).isFile()){
				//下载的是文件
				success = new File(tempFileName).renameTo(new File(localFilePath));
				if(!success){
					LoggerUtils.error("移动本地文件失败:"+tempFileName);
				}
				
				//删除解压创建的临时文件夹
				success = new File(tempPath).delete();
				if(!success){
					LoggerUtils.error("删除解压创建的临时文件夹失败");
				}
			}else{
				//下载的是文件夹
				success = new File(tempPath).renameTo(new File(localFilePath));
				if(!success){
					LoggerUtils.error("移动本地文件失败:"+tempPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过模拟一个终端执行命令列表
	 * @author likaihao
	 * @date 2016年4月27日 下午6:52:14
	 * @param hostname 地址
	 * @param username 用户名
	 * @param password 密码
	 * @param out 输出流,会打印执行的命令,为空表示不打印
	 * @param commands 命令...
	 * @return linux输出
	 */
	public static String execLinuxCommandByClient(String hostname,String username,String password,OutputStream out,String... commands){
		return execLinuxCommandByClient(hostname,username,password,out,Arrays.asList(commands));
	}
	
	
	/**
	 * 通过模拟一个终端执行命令列表
	 * @author likaihao
	 * @date 2016年4月27日 下午6:52:14
	 * @param hostname 地址
	 * @param username 用户名
	 * @param password 密码
	 * @param out 输出流,会打印执行的命令
	 * @param commandList 命令列表
	 * @return linux输出
	 */
	public static String execLinuxCommandByClient(String hostname,String username,String password, OutputStream out,List<String> commandList){
		return execLinuxCommandByClient(hostname, username, password, out, commandList, null);
	}
	
	/**
	 * 通过模拟一个终端执行命令列表
	 * @author likaihao
	 * @date 2016年4月27日 下午6:52:14
	 * @param hostname 地址
	 * @param username 用户名
	 * @param password 密码
	 * @param out 输出流,会打印执行的命令
	 * @param command 命令
	 * @param abortRe 中止正则
	 * @return linux输出
	 */
	public static String execLinuxCommandByClientCanAbort(String hostname,String username,String password, OutputStream out,String command, String abortRe){
		List<String> commandList = new ArrayList<String>();
		commandList.add(command);
		return execLinuxCommandByClient(hostname, username, password, out, commandList, abortRe);
	}
	
	/**
	 * 通过模拟一个终端执行命令列表
	 * @author likaihao
	 * @date 2016年4月27日 下午6:52:14
	 * @param hostname 地址
	 * @param username 用户名
	 * @param password 密码
	 * @param out 输出流,会打印执行的命令,为空表示不打印
	 * @param commandList 命令列表
	 * @param abortRe 退出字符串的正则(遇到匹配的字符串就退出)
	 * @return linux输出
	 */
	private static String execLinuxCommandByClient(String hostname,String username,String password, OutputStream out,List<String> commandList,String abortRe){
		Connection conn = getSSHConnection(hostname,username,password);
		try{
			return execLinuxCommandByClient(conn, out, commandList, abortRe);
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	/**
	 * 通过模拟一个终端执行命令列表
	 * @author likaihao
	 * @date 2016年4月27日 下午6:52:14
	 * @param hostname 地址
	 * @param username 用户名
	 * @param password 密码
	 * @param out 输出流,会打印执行的命令,为空表示不打印
	 * @param commandList 命令列表
	 * @param abortRe 退出字符串的正则(遇到匹配的字符串就退出)
	 * @return linux输出
	 */
	public static String execLinuxCommandByClient(Connection conn, OutputStream out,List<String> commandList,String abortRe){
		//建立连接
		boolean isNewConn = false;
		Session ssh = null;
		try{
			try {
				ssh = conn.openSession();
			} catch (Exception e) {
				boolean errorFlag = e.getMessage().contains("connection is being shutdown") || e.getMessage().contains("connection is closed");
				if(isFortressHostname(conn.getHostname()) && errorFlag){
					conn = getFortressConnection(conn.getHostname());
					isNewConn = true;
					ssh = conn.openSession();
				}else{
					throw new RuntimeException("openSession失败",e);
				}
			}
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
			String okRe = "[^\r\n]*\\[.*\\][#$] ";
			
			//执行命令
			int num = 0;
			int i_char = -1;
			while( (i_char = reader.read())!=-1 ){
				char c = (char) i_char;
				builder.append(c);
				//System.out.print(c);
				//如果遇到[root@elk ~]# ,则执行下一个命令
				String lastLine = builder.substring(builder.lastIndexOf("\n")+1);
				if(lastLine.matches(okRe)){
					//如果命令执行完,跳出循环
					if(num == commandList.size()){
						break;
					}
					
					//打印日志
					String log = conn.getHostname()+" 执行命令:"+commandList.get(num);
					LoggerUtils.info(log);
					if(out!=null){
						log += "\r\n";
						if(out instanceof ByteArrayOutputStream){
							log = DateUtils.dateToStringYMdHmsS(new Date()) + " ~ " + log;
						}
						out.write(log.getBytes("utf-8"));
					}
					
					//发送命令
					String command = commandList.get(num)+"\r";
					sshOut.write(command.getBytes());
					num++;
				}else if(abortRe!=null && abortRe.length()>0 && lastLine.matches(abortRe)){
					//如果遇到结束标志,不执行后续的命令,不接收后续的输出
					sshOut.write(3);
					sshOut.write("\r".getBytes());
					break;
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
			if(isNewConn){
				conn.close();
			}
		}
	}
	
	/**
	 * 获得连接
	 * @return
	 */
	public static Connection getSSHConnection(String hostname,String username,String password){
		return getSSHConnection(hostname, username, password, 22);
	}
	
	/**
	 * 获得连接
	 * @return
	 */
	public static Connection getSSHConnection(String hostname,String username,String password, int port){
		if(isFortressHostname(hostname)){
			return getFortressConnection(hostname);
		}
		
		try {
			//指明连接主机的IP地址
			Connection conn = new Connection(hostname, port);
			//连接到主机(超时时间10秒)
			conn.connect(null,10000,10000);
			//使用用户名和密码校验
			boolean isconn = conn.authenticateWithPassword(username, password);
			if(!isconn){
				throw new RuntimeException("连接远程服务器,登录失败");
			}
			return conn;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("连接远程服务器失败");
		}
	}
	
	/**
	 * 执行某个命令,并进行后续输入
	 * @author likaihao
	 * @date 2017年1月11日 下午5:36:32
	 * @param command 命令(或可执行文件路径)
	 * @param commandParam 命令参数
	 * @param exeSubCommandList 后续命令
	 * @param encoding 编码
	 * @return 输出
	 */
	public static String exec(String command, String commandParam, List<String> exeSubCommandList, final String encoding){
		Process p = null;
		try {
			if(System.getProperty("os.name").toLowerCase().startsWith("win")){
				//window
				if(commandParam!=null){
					p = Runtime.getRuntime().exec("cmd /c \"\""+command+"\" "+commandParam+"\"");
				}else{
					p = Runtime.getRuntime().exec(new String[]{"cmd","/c",command});
				}
			}else{
				//linux
				p = Runtime.getRuntime().exec(new String[]{"sh","-c",command});
			}
			
			//输入命令
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			if(exeSubCommandList!=null && exeSubCommandList.size()>0){
				for(String subCommand : exeSubCommandList){
					stdin.print(subCommand+"\r");
				}
			}
			stdin.close();
			
			//接收输出
			List<InputStream> taskParamList = new ArrayList<InputStream>();
			taskParamList.add(p.getErrorStream());
			taskParamList.add(p.getInputStream());
			
			final StringBuffer buffer = new StringBuffer();
			ThreadConcurrentUtils.common(taskParamList, new Job<InputStream,String>(){
				@Override
				public String doJob(InputStream in) {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
						String line = null;
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("]0;MINGW32")) {
								// 忽略标题
								buffer.append("\r\n");
							} else {
								//去除颜色标签
								line = line.replaceAll("\\[\\d+?m", "");
								buffer.append(line+"\r\n");
							}
						}
						return null;
					} catch (Exception e) {
						throw new RuntimeException("处理命令出现错误：" + e.getMessage(),e);
					}
				}
			}, 2, 0).getValueList();
			
			return buffer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally{
			if(p!=null){
				p.destroy();
			}
		}
	}
	
	/**
	 * 执行某个命令
	 * @author likaihao
	 * @date 2017年1月11日 下午5:36:32
	 * @param command 命令
	 * @return 输出
	 */
	public static String exec(String command){
		return exec(command, null, null, "utf-8");
	}
	
	/**
	 * 执行某个命令
	 * @author likaihao
	 * @date 2017年1月11日 下午5:36:32
	 * @param command 命令
	 * @param encoding 编码
	 * @return 输出
	 */
	public static String exec(String command, String encoding){
		return exec(command, null, null, encoding);
	}
	
	/**
	 * 描述:判断hostname是不是堡垒机的hostname 
	 * @author: 李凯昊
	 * @date:2017年8月11日 上午11:28:05
	 * @param hostname
	 * @return
	 */
	public static boolean isFortressHostname(String hostname){
		return hostname.startsWith("10.10.");
	}
	
	/**
	 * 描述: 获得堡垒机连接
	 * @author: 李凯昊
	 * @date:2017年8月11日 上午10:02:26
	 * @param hostname
	 * @return
	 */
	public static Connection getFortressConnection(String hostname){
		try {
			Method method = ReflectUtils.getUniqueMethod("work.protec.bus365.fortress.FortressService", "getSSHConnection");
			if(method!=null){
				Connection conn2 = (Connection) method.invoke(null, hostname);
				return conn2;
			}else{
				throw new RuntimeException("未找到指定方法FortressService.getSSHConnection");
			}
		} catch (Exception e) {
			throw new RuntimeException("连接远程服务器失败",e);
		}
	}
	
	/**
	 * 描述:获得堡垒机连接信息 
	 * @author: 李凯昊
	 * @date:2017年8月11日 上午9:59:28
	 * @param conn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getFortressConnInfo(String hostname){
		try {
			Method method = ReflectUtils.getUniqueMethod("work.protec.bus365.fortress.FortressService", "getNewConnectionInfo");
			if(method!=null){
				Map<String,String> connInfo = (Map<String,String>) method.invoke(null, hostname);
				return connInfo;
			}else{
				throw new RuntimeException("未找到指定方法FortressService.getNewConnectionInfo");
			}
		} catch (Exception e) {
			throw new RuntimeException("获取堡垒机连接信息失败",e);
		}
	}
	
}