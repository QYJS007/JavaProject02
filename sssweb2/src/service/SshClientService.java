package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import model.generate.RemoteProject;
import utils.IOUtils;
import utils.LoggerUtils;
import utils.SSHUtils;
import utils.StringUtils;
import utils.TimerManager;
import utils.model.concurrent.Job;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import dao.generate.RemoteProjectGenDao;

public class SshClientService {
	
	public static TimerManager<Object,Object> noActiveTimerManager = null;//无动作定时器的引用
	public static Map<String,SshClientService> sshBPMap = new HashMap<String,SshClientService>();//存储所有sshbp的实例
	
	private SshClientService(){//私有构造方法
		
	}
	
	/**
	 * 获得实例,id为null表示新建,不为空表示获取
	 * @author likaihao
	 * @date 2016年4月12日 下午4:01:20
	 * @param id 
	 * @param out 目标输出流
	 * @return
	 */
	public static SshClientService getInstance(String id){
		SshClientService sshService = null;
		//id为null表示新建,不为空表示获取
		if(id==null || id.length()==0){
			sshService = new SshClientService();
			sshService.id = "ssh_"+StringUtils.getTempRandomStr();
			sshBPMap.put(sshService.id, sshService);
		}else{
			sshService = sshBPMap.get(id);
		}
		if(sshService==null){
			throw new RuntimeException("没有发现此连接,id:"+id);
		}
		return sshService;
	}
	
	public String id = null;
	public RemoteProject remoteproject = null;//远程项目信息
	public Connection conn = null;//连接
	public Session ssh = null;//会话
	public OutputStream out = null;//页面输出流
	public TimerManager<Object,Object> pageCloseTimerManager = null;//页面关闭定时器的引用
	
	/**
	 * 建立连接并监听返回
	 * @param remoteProjectId 远程项目id
	 * @param out 输出流
	 */
	public void openConn(String remoteProjectId, OutputStream out){
		try {
			this.out = out;
			
			//查询远程项目信息
			this.remoteproject = new RemoteProjectGenDao().findById(new Integer(remoteProjectId));
			
			//建立新连接
			LoggerUtils.info("ssh-建立连接("+remoteproject.getIp()+",id:"+id+")");
			writeStrToPage("正在建立连接:"+remoteproject.getIp()+"\r\nid:"+id);
			conn = SSHUtils.getSSHConnection(remoteproject.getIp(), remoteproject.getUsername(), remoteproject.getPassword());
			ssh = conn.openSession();
			
			//开启定时器
			// * 页面关闭后,自动关闭连接 (采用心跳机制,页面8分钟内没有发送请求重置定时器,则关闭连接)
			if(out instanceof ServletOutputStream ) {
				String pageCloseTimerName = "pageCloseTimer_"+id.split("_")[1];
				pageCloseTimerManager = TimerManager.setTimeout(pageCloseTimerName, new Job<Object,Object>(){
					public Object doJob(Object param) {
						closeConn(true);
						return null;
					}
				}, 1000 * 60 * 8, pageCloseTimerName);
			}
			
			// * 15分钟项目无请求后自动关闭所有连接
			if(noActiveTimerManager==null){
				String noActiveTimerName = "noActiveTimer";
				noActiveTimerManager = TimerManager.setTimeout(noActiveTimerName, new Job<Object,Object>(){
					public Object doJob(Object param) {
						//关闭全部的连接 (需要新建立一个map迭代,因为在closeConn中对sshBPMap进行了删除元素操作)
						Map<String,SshClientService> map = new HashMap<String,SshClientService>(sshBPMap);
						for(SshClientService bp : map.values()){
							bp.closeConn(true);
						}
						noActiveTimerManager = null;
						LoggerUtils.info("无动作定时器执行完成,任务列表:"+TimerManager.getStatus());
						return null;
					}
				},1000 * 60 * 15, noActiveTimerName);
			}
			
			//模拟一个终端
			ssh.requestPTY("dump", 500, 100, 2000, 1000, null);
			ssh.startShell();
			
			//获取输入流,接收输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(ssh.getStdout()));
			String str = null;
			while( (str = reader.readLine())!=null ){
				//如果无法写入到页面,请检查fiddler是否正在运行
				writeStrToPage(str);
			}
		} catch (Exception e) {
			closeConn(false);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 执行命令
	 * @param command 命令
	 */
	public void execCommand(String command){
		LoggerUtils.info("ssh-执行命令("+conn.getHostname()+",id:"+id+"): "+command);
		
		//如果是正式环境,则忽略play stop命令, 除非命令开头有个@
		if(remoteproject.getIstest()==0){
			if(command.startsWith("play stop")){
				LoggerUtils.info("ssh-忽略命令("+conn.getHostname()+",id:"+id+"): "+command);
				return;
			}else if(command.startsWith("@play stop")){
				command = command.substring(1);
			}
		}
		
		try {
			//获得输出流, 发送命令
			OutputStream out = ssh.getStdin();
			if(command.equals("^C")){ //ctrl+c 中断符
				out.write(3);
				out.write("\r".getBytes());
			}else{
				command += "\r\r"; //两个\r跟显示有关,相当于又执行了一个空命令
				out.write(command.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 向页面发送消息
	 * @param str
	 */
	public void writeStrToPage(String str){
		try {
			str += "\r\n";
			out.write(str.getBytes("utf-8"));
			out.flush();
		} catch (Exception e) {
			if(e.getClass().getSimpleName().equals("ClientAbortException")){
				//页面关闭,不进行操作
				LoggerUtils.info("页面关闭,发送消息失败,关闭连接,id:"+id);
				closeConn(false);
				throw new RuntimeException("页面关闭,发送消息失败,关闭连接,id:"+id);
			}else{
				e.printStackTrace();
				throw new RuntimeException("发送消息失败:"+str);
			}
			
		}
	}
	
	/**
	 * 断开连接
	 * @author likaihao
	 * @date 2016年7月23日 上午10:51:48
	 * @param isSendMessage 是否向页面发送断开连接的消息
	 */
	public void closeConn(boolean isSendMessage){
		try {
			LoggerUtils.info("ssh-断开连接:"+id);
			//向页面发送消息
			if(isSendMessage){
				try {
					writeStrToPage("连接已断开");
				} catch (Exception e) {
					LoggerUtils.error("发送消息失败");
				}
			}
			
			//取消定时器
			if(pageCloseTimerManager!=null){
				pageCloseTimerManager.cancel();
			}
			
			//关闭连接
			if(ssh!=null){
				ssh.getStdout().close();
				ssh.close();
				ssh = null;
			}
			if(conn!=null){
				conn.close();
				conn = null;
			}
			
			//删除引用
			sshBPMap.remove(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 下载文件
	 * @author likaihao
	 * @date 2016年7月23日 上午10:52:18
	 * @param path 下载文件的路径
	 */
	public void downFile(String path){
		try {
			//下载文件
			String localFilePath = IOUtils.getHomeDirectoryPath()+"/"+IOUtils.getFileName(path)+"."+StringUtils.getRandomStr();
			SSHUtils.downloadLinuxFileCompress(remoteproject.getIp(), remoteproject.getUsername(), remoteproject.getPassword(), path, localFilePath);
			
			//如果是日志,尝试用notepad++打开
			String notepadPath = new ParamsService().findParamsByName("notepadPath").getValue();
			if(localFilePath.contains(".log") && notepadPath!=null && notepadPath.length()>0){
				//如果出错了,提示下载完成
				try {
					Runtime.getRuntime().exec(notepadPath + " " + localFilePath);
				} catch (Exception e) {
					out.write("下载完成".getBytes());
				}
			}else{
				//如果不是日志,提示下载完成
				out.write("下载完成".getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 打开SecureCRT
	 * @author likaihao
	 * @date 2016年7月23日 上午10:52:18
	 * @param path 下载文件的路径
	 */
	public void openSecureCRT(){
		//拼接命令
		String path = new ParamsService().findParamsByName("SecureCRTExePath").getValue();
		String command = "\""+path+"\" /T /SSH2 /L "+remoteproject.getUsername()+" /P 22 /PASSWORD \""+remoteproject.getPassword()+"\" "+remoteproject.getIp();
		
		//执行命令
		LoggerUtils.info("执行命令:"+command);
		SSHUtils.exec(command);
	}
	
}