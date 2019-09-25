package service;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.generate.Params;
import model.generate.RemoteProject;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;

import service.generate.RemoteProjectGenService;
import utils.CompressUtils;
import utils.DateUtils;
import utils.IOUtils;
import utils.LoggerUtils;
import utils.SSHUtils;
import utils.SVNUtils;
import utils.StringUtils;
import utils.ThreadConcurrentUtils;
import utils.model.concurrent.Job;

public class SshPackService {
	
	RemoteProjectGenService remoteProjectGenService = new RemoteProjectGenService();
	RemoteProjectService remoteProjectBp = new RemoteProjectService();
	
	/**
	 * 查询符合条件的文件列表
	 * @param localPath 本地项目路径
	 * @param pathPattern 打包的路径模式
	 * @param type 打包类型
	 */
	public String query(String localPath,String pathPattern,String type){
		pathPattern = pathPattern.replace("\n", ",");
		
		String returnStr = null;
		//全部打包 和 根据指定路径打包
		if(type.equals("all") || type.equals("path")){
			//根据指定的模式 获取文件
			List<File> list = getFileByPattern(localPath, pathPattern);
			
			//拼凑文件路径,返回页面
			StringBuilder builder = new StringBuilder();
			for(File f : list){
				builder.append(f.getAbsolutePath().substring(localPath.length()).replace("\\", "/")+"\r\n");
			}
			returnStr = builder.toString();
		}else if(type.startsWith("lastModifyMinute")){
			//根据最后修改时间打包
			List<String> filePathList = getLastSomeDateModifyFilePath(localPath,pathPattern,type.split("-")[1]);
			if(filePathList.size()>0){
				returnStr = StringUtils.join(filePathList, "\r\n")+"\r\n";
			}
		}else{
			throw new RuntimeException("未处理的type:"+type);
		}
		
		if(returnStr==null || returnStr.length()==0){
			returnStr = "没有修改的文件";
		}
		return returnStr;
	}
	
	/**
	 * 导出文件
	 * @param localPath 本地路径
	 * @param text 导出文件文本
	 * @return
	 */
	public Map<String,String> export(String localPath,String text){
		//解析页面传来的路径, 不是/开头的忽略
		List<String> pathList = new ArrayList<String>();
		String[] lineArr = text.split("[\r\n]+");
		for(String line : lineArr){
			if(line.startsWith("/")){
				pathList.add(line);
			}
		}
		
		//导出
		Map<String,String> map = new HashMap<String,String>();
		map.put("exportCount", pathList.size()+"");
		if(pathList.size()>0){
			//导出到桌面的打包目录下
			String dirName = StringUtils.getRandomStr();
			String homeDir =IOUtils.getHomeDirectoryPath().replace("\\", "/")+"/打包/"+dirName.substring(0,8)+"/"+dirName.substring(8);
			for(String filePath : pathList){
				IOUtils.copyFile(localPath+filePath, homeDir+filePath);
			}
			map.put("exportPath", homeDir);
		}
		return map;
	}
	
	/**
	 * 打包
	 * @author likaihao
	 * @date 2016年7月23日 下午6:29:15
	 * @param remoteProjectIds 远程项目信息,多个用逗号分隔
	 * @param localFilePath 本地文件路径
	 * @param type 打包类型
	 * @param pathPattern 允许的路径模式
	 * @param out 输出流,会打印执行的命令
	 */
	public void pack(String remoteProjectIds,String localFilePath,String type,String pathPattern,OutputStream out){
		//获取远程项目信息,忽略正式环境
		Params params = new ParamsService().findParamsByName("onLinePackage");
		List<RemoteProject> remoteProjectList = new ArrayList<RemoteProject>();
		String[] idArr = remoteProjectIds.split(",");
		for(String id : idArr){
			RemoteProject r = remoteProjectGenService.findById(new Integer(id));
			if(r.getIstest()==1 || (params!=null && params.getValue().equals("1"))){//判断是否忽略正式环境
				remoteProjectList.add(r);
			}else{
				LoggerUtils.info("正式环境,已忽略:"+r.getName());
			}
		}
		if(remoteProjectList.size()==0){
			throw new RuntimeException("没有需要打包的项目");
		}
		
		//获得打包时压缩文件的名称
		String gzFileName = new File(localFilePath).getName() + ".tar.gz";
		
		//查询打包使用的命令
		String commandName = null;
		if(type.equals("all")){
			commandName = "打包-全部打包";
		}else{
			commandName = "打包-局部打包";
		}
		for(RemoteProject r : remoteProjectList){
			String command = remoteProjectBp.getRemoteCommand(r.getId(), "ssh-pack", commandName, gzFileName);
			if(command==null){
				throw new RuntimeException("没有找到打包命令,项目id:"+r.getId()+",项目名称:"+r.getName()+",命令名称:"+commandName);
			}
			r.setCommand(command);
		}
		
		//打包
		packDir(localFilePath, remoteProjectList, gzFileName, out);
		
	}
	
	/**
	 * 打包文件夹
	 * @param localDirPath 本地文件夹路径
	 * @param RemoteProjectInfoInfoList 服务器地址
	 * @param out 输出流,会打印执行的命令
	 */
	public void packDir(String localDirPath,List<RemoteProject> remoteProjectList,final String gzFileName, final OutputStream out){
		//判断本地文件是否存在
		final File localDir = new File(localDirPath);
		if(!localDir.exists() || !localDir.isDirectory()){
			throw new RuntimeException("文件夹不存在:"+localDirPath);
		}
		
		//本地压缩文件
		writeToOut(out, "本地压缩文件...");
		final String gzFilePath = localDir.getParent()+"/"+gzFileName;//要打包文件夹的上一层目录
		if(!localDirPath.endsWith("/")){
			localDirPath+="/";
		}
		CompressUtils.compressGzip(localDirPath, gzFilePath);
		
		//执行远程命令, 每个远程项目开一个线程
		List<Boolean> returnList = ThreadConcurrentUtils.common(remoteProjectList, new Job<RemoteProject, Boolean>(){
			public Boolean doJob(RemoteProject remoteProjectInfo) {
				try {
					//本地上传文件
					writeToOut(out, remoteProjectInfo.getIp()+" 上传文件("+remoteProjectInfo.getPath()+"/"+gzFileName+")...");
					SSHUtils.uploadLinuxFile(remoteProjectInfo.getIp(), remoteProjectInfo.getUsername(), remoteProjectInfo.getPassword(), gzFilePath, remoteProjectInfo.getPath());
					
					//获取命令
					String[] commandArr = remoteProjectInfo.getCommand().split("\r\n");
					
					//执行命令
					String startTime = DateUtils.dateToStringYMdHms(new Date());
					String str = SSHUtils.execLinuxCommandByClient(remoteProjectInfo.getIp(), remoteProjectInfo.getUsername(), remoteProjectInfo.getPassword(), out, Arrays.asList(commandArr));
					String endTime = DateUtils.dateToStringYMdHms(new Date());
					
					//保存执行日志
					String logPath = localDir.getAbsolutePath()+"\\"+remoteProjectInfo.getName()+DateUtils.dateToString(new Date(), "_MMdd_HHmmss")+".txt";
					str = "开始时间:"+startTime + ",结束时间:"+endTime+"\r\n" + str;
					IOUtils.writeFileReplace(logPath, str);
					LoggerUtils.info(remoteProjectInfo.getIp() + " 打包完成,日志保存在:"+logPath);
					writeToOut(out, remoteProjectInfo.getIp() + " 打包完成,日志保存在:"+logPath);
					return true;
				} catch (Exception e) {
					LoggerUtils.error("打包异常:"+remoteProjectInfo.getIp()+","+remoteProjectInfo.getPath(), e);
					return false;
				}
			}
		}, remoteProjectList.size(), 0).getValueList();
		
		int failNum = 0;
		for(Boolean b : returnList){
			if(!b){
				failNum ++;
			}
		}
		
		//本地删除压缩文件
		writeToOut(out, "本地删除压缩文件...");
		if(!new File(gzFilePath).delete()){
			writeToOut(out, "删除本地压缩文件失败:"+gzFilePath);
		}
		writeToOut(out, "打包完成,打包了"+(returnList.size()-failNum)+"个项目");
		
		//访问项目,每3秒访问一次,最多重试20次
		ThreadConcurrentUtils.common(remoteProjectList, new Job<RemoteProject, Boolean>(){
			public Boolean doJob(RemoteProject remoteProjectInfo) {
				try {
					int count = 20;//重试次数
					int successCount = 0;//用来记录成功次数
					for(int i=0;i<count;i++){
						//访问项目
						String command = "curl -m 1  http://"+remoteProjectInfo.getIp()+":"+remoteProjectInfo.getPort();
						String returnStr = SSHUtils.execLinuxCommandByClient(remoteProjectInfo.getIp(), remoteProjectInfo.getUsername(), remoteProjectInfo.getPassword(), null, command);
						//LoggerUtils.info("访问"+remoteProjectInfo.getName()+",返回:"+returnStr);
						if(!returnStr.startsWith("curl: ")){
							//有时访问一次play不会重新编译,所以现在访问3次成功后才停止
							successCount++;
							if(successCount>=3){
								LoggerUtils.info("访问成功,"+remoteProjectInfo.getName()+",http://"+remoteProjectInfo.getIp()+":"+remoteProjectInfo.getPort());
								return true;
							}
						}
						//每3秒重试一次
						Thread.sleep(3000);
					}
					LoggerUtils.error("访问"+count+"次失败,"+remoteProjectInfo.getName()+",http://"+remoteProjectInfo.getIp()+":"+remoteProjectInfo.getPort());
					return false;
				} catch (Exception e) {
					LoggerUtils.error("访问异常:"+remoteProjectInfo.getIp()+","+remoteProjectInfo.getPath(), e);
					return false;
				}
			}
		}, remoteProjectList.size(), 0);
	}
	
	/**
	 * 获得修改过的文件路径(将文件系统和svn最新版本的文件比较大小)
	 * @param projectName
	 * @param firstDirNameArr
	 * @param lgnoreFileNameArr
	 * @param localBasePath
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<String> getSVNModifyFilePathList(String localPath,String[] firstDirNameArr){
		//将项目名称对应到svn子路径
		String subPath = SVNUtils.getSVNPath(localPath);
		
		//获得本地所有文件
		List<File> fileList = IOUtils.getFileList(localPath, true, firstDirNameArr, null, null);
		
		//获得svn所有文件
		Map<String,Long> svnFileMap2 = getSVNFileMap(subPath, firstDirNameArr);
		Map<String,Long> svnFileMap = new HashMap<String,Long>();
		for(String path: svnFileMap2.keySet()){
			String key = path.substring(subPath.length());//key去除subPath
			if(key.indexOf("/",1)!=-1){//去除根目录的文件
				svnFileMap.put(key, svnFileMap2.get(path));
			}
		}
		
		//获得本地文件的大小
		Map<String,Long> localFileMap = new HashMap<String,Long>();
		for(File file : fileList){
			String s = file.getAbsolutePath().substring(localPath.length()).replace("\\", "/");
			if(!s.contains(".svn")){//去除svn文件
				localFileMap.put(s, file.length());
			}
		}
		
		//比较,找出新增和修改的文件
		List<String> diffList = new ArrayList<String>();
		for(String filePath : localFileMap.keySet()){
			if(!svnFileMap.containsKey(filePath)){
				diffList.add(filePath);
			}else{
				if(!localFileMap.get(filePath).equals(svnFileMap.get(filePath))){
					diffList.add(filePath);
				}
			}
		}
		//排序
		Collections.sort(diffList);
		return diffList;
	}
	
	/**
	 * 获得svn最新版本的文件列表
	 * @param subPath
	 * @param firstDirNameArr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Long> getSVNFileMap(String subPath,String[] firstDirNameArr){
		SVNRepository repository = SVNUtils.getSVNRepository();
		Map<String,Long> svnFileMap = new HashMap<String,Long>();
		
		List<String> firstDirNameList = Arrays.asList(firstDirNameArr);
		try {
			Collection<SVNDirEntry> entries;
			try {
				entries = repository.getDir(subPath, -1, null, (Collection<Object>)null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			Iterator<SVNDirEntry> iterator = entries.iterator();
			while (iterator.hasNext()) {
				SVNDirEntry entry = iterator.next();
				String name = entry.getName();
				if (entry.getKind() == SVNNodeKind.DIR) {
					if(firstDirNameList.contains(name)){
						getSVNFileMap2(subPath+"/"+name, svnFileMap);
					}
				}else{
					if(!name.startsWith(".")){
						svnFileMap.put(subPath + "/" +name, entry.getSize());
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return svnFileMap;
	}
	
	/**
	 * 获得svn最新版本的文件列表,用于递归的私有方法
	 * @param subPath
	 * @param firstDirNameArr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Long> getSVNFileMap2(String subPath, Map<String,Long> svnFileMap){
		SVNRepository repository = SVNUtils.getSVNRepository();
		try {
			Collection<SVNDirEntry> entries = repository.getDir(subPath, -1, null, (Collection<Object>)null);
			Iterator<SVNDirEntry> iterator = entries.iterator();
			while (iterator.hasNext()) {
				SVNDirEntry entry = iterator.next();
				String name = entry.getName();
				if (entry.getKind() == SVNNodeKind.DIR) {
					getSVNFileMap2(subPath+"/"+name, svnFileMap);
				}else{
					svnFileMap.put(subPath + "/" +name, entry.getSize());
				}
			}
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return svnFileMap;
	}
	
	/**
	 * 获得某个路线下 最近某段时间修改的文件路径
	 * @author likaihao
	 * @date 2016年2月24日 上午11:03:48
	 * @param path 根路径
	 * @param pathPattern 路径规则
	 * @param lastMinute 最后修改分钟范围
	 * @return
	 */
	public List<String> getLastSomeDateModifyFilePath(String path,String pathPattern,String lastMinute){
		//获取符合路径规则的文件
		List<File> fileList = getFileByPattern(path,pathPattern);
		
		//计算符合条件的最后修改时间
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, new Integer("-"+lastMinute));
		long okLastModifyTime = c.getTimeInMillis();
		
		//筛选文件
		List<String> pathList = new ArrayList<String>();
		for(File file : fileList){
			//如果是文件夹,就判断下面的所有文件
			if(file.isDirectory()){
				List<File> l = IOUtils.getAllFileList(file.getAbsolutePath());
				for(File f : l){
					if(f.isFile() && f.lastModified()>okLastModifyTime){
						pathList.add(f.getAbsolutePath().substring(path.length()).replace("\\", "/"));
					}
				}
			}else if(file.isFile() && file.lastModified()>okLastModifyTime){
				pathList.add(file.getAbsolutePath().substring(path.length()).replace("\\", "/"));
			}
		}
		return pathList;
	}
	
	/**
	 * 获取指定模式的文件
	 * @author likaihao
	 * @date 2016年4月26日 上午11:39:13
	 * @param basePath 根路径
	 * @param pathPattern 符合的路径规则,多个用换行分开,文件夹以@开头
	 * @return
	 */
	public List<File> getFileByPattern(String basePath,String pathPattern){
		//获取项目路径下的所有文件(包括文件夹)
		List<File> fileList = IOUtils.getAllFileListContainsDir(basePath);
		
		//文件夹 和 文件的匹配规则
		String[] pathArr = pathPattern.split(",");
		Pattern dirPattern = null;
		Pattern filePattern = null;
		StringBuilder builder1 = new StringBuilder();
		StringBuilder builder2 = new StringBuilder();
		for(String pattern : pathArr){
			if(pattern.startsWith("@")){
				builder1.append("("+pattern.substring(1)+")|");
			}else{
				builder2.append("("+pattern+")|");
			}
		}
		if(builder1.length()>0){
			builder1.deleteCharAt(builder1.length()-1);
			dirPattern = Pattern.compile(builder1.toString(),Pattern.CASE_INSENSITIVE);
		}
		if(builder2.length()>0){
			builder2.deleteCharAt(builder2.length()-1);
			filePattern = Pattern.compile(builder2.toString(),Pattern.CASE_INSENSITIVE);
		}
		
		//筛选符合条件的文件
		List<File> fileList2 = new ArrayList<File>();
		for(File f : fileList){
			String p = f.getAbsolutePath().substring(basePath.length()).replace("\\", "/");
			if(f.isDirectory() && dirPattern!=null && dirPattern.matcher(p).matches()){
				fileList2.add(f);
			}else if(f.isFile() && filePattern!=null && filePattern.matcher(p).matches()){
				fileList2.add(f);
			}
		}
		return fileList2;
	}
	
	//向指定流写入字符串
	public void writeToOut(OutputStream out,String str){
		try {
			if(out!=null){
				str+="\r\n";
				out.write(str.getBytes("UTF-8"));
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}