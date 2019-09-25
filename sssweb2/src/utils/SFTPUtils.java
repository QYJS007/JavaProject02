package utils;

import java.io.File;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SFTPUtils {

	
	/**
	 * 描述: 从linux下载文件
	 * @author: 李凯昊
	 * @date:2017年8月10日 下午4:35:17
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public static void downloadLinuxFile(String hostname, int port, String username, String password, String remoteFilePath, String localFilePath) {
		Session session = null;
		Channel channel = null;
		try {
			File file = new File(localFilePath);
			if(file.isFile() && file.exists()){
				throw new RuntimeException("文件已存在:"+file.getAbsolutePath());
			}
			LoggerUtils.info("下载文件开始:"+hostname+","+remoteFilePath);
			JSch jsch = new JSch();
			session = jsch.getSession(username, hostname, port);
			session.setPassword(password);
			session.setTimeout(100000);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
	
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp chSftp = (ChannelSftp) channel;
			chSftp.get(remoteFilePath, localFilePath);
			chSftp.quit();
			LoggerUtils.info("下载文件完成:"+hostname+","+remoteFilePath);
		} catch (Exception e) {
			throw new RuntimeException("下载文件失败",e);
		} finally {
			if(channel!=null){
				channel.disconnect();
			}
			if(session!=null){
				session.disconnect();
			}
		}
	}
	
	/**
	 * 描述:将文件上传到linux 
	 * @author: 李凯昊
	 * @date:2017年8月10日 下午4:47:00
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 * @param localFilePath
	 * @param remoteFilePath
	 */
	public static void uploadLinuxFile(String hostname, int port, String username, String password, String localFilePath, String remoteFilePath) {
		Session session = null;
		Channel channel = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, hostname, port);
			session.setPassword(password);
			session.setTimeout(100000);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
	
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp chSftp = (ChannelSftp) channel;
			chSftp.put(localFilePath, remoteFilePath);
			chSftp.quit();
		} catch (Exception e) {
			throw new RuntimeException("下载文件失败",e);
		} finally {
			if(channel!=null){
				channel.disconnect();
			}
			if(session!=null){
				session.disconnect();
			}
		}
	}

}