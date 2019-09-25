package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNUtils {
	
	private static SVNRepository repository = null;
	private static SVNClientManager clientManager = null;
	
	/**
	 * 初始化操作
	 */
	static{
		//每隔一段时间重新连接一次(否则的话每隔一段时间第一次访问会报错)
		init();
		int interval = 1000 * 60 * 5;
		new Timer().schedule(new TimerTask(){
			public void run() {
				if(repository!=null){
					repository.closeSession();
				}
				init();
			}
		}, interval, interval);
	}
	
	//初始化操作
	public static void init(){
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
		
		String baseUrl = "svn://211.157.145.230/bj_1306_webtkt";
		//baseUrl = "svn://192.168.3.3/bj_1306_webtkt";
		String username = "likaihao";
		String password = "likh123";
		
		//连接并登陆
		try {
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.toCharArray());
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(baseUrl));
			repository.setAuthenticationManager(authManager);
			
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
			clientManager = SVNClientManager.newInstance(options,authManager);
		} catch (SVNException e) {
			e.printStackTrace();
		}
	}
	
	public static SVNRepository getSVNRepository(){
		return repository;
	}
	
	/**
	 * 获取svn日志
	 * @author likaihao
	 * @date 2015年9月16日 下午1:44:54
	 * @param subPath 路径
	 * @param startVersion 开始版本号
	 * @param endVersion 结束版本号
	 * @param author 作者
	 * @param topNum 前n条记录
	 * @return
	 */
	public static List<SVNLogEntry> getSVNLog(String subPath,long startVersion,long endVersion,final String author){
		try {
			List<SVNLogEntry> list = new ArrayList<SVNLogEntry>();
			
			//获得最新版本,log方法开始版本和结束版本不能同时传-1
			if(endVersion==-1){
				SVNNodeKind type = null;
				try {
					type = repository.checkPath(subPath, -1);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				if(type == SVNNodeKind.DIR){
					long lastRevision = repository.getDir(subPath, -1, true, null).getRevision();
					endVersion = lastRevision;
				}else if(type == SVNNodeKind.FILE){
					long lastRevision = repository.getDir(subPath.substring(0,subPath.lastIndexOf("/")), -1, true, null).getRevision();
					startVersion = lastRevision-2000;
				}else if(type == SVNNodeKind.NONE){
					System.err.println("文件不存在:"+subPath);
					return list;
				}
			}
			
			//获得指定范围的日志
			try {
				repository.log(new String[]{subPath}, list, startVersion, endVersion, true, false);
			} catch (SVNException e) {
				System.err.println("读取svn错误:"+e.getMessage());
				return list;
			}
			
			if(list==null || list.size()==0){
				return list;
			}
			if(author==null || author.length()==0){
				return list;
			}
			
			//筛选条数和作者
			List<SVNLogEntry> list2 = new ArrayList<SVNLogEntry>();
			for(int i=0;i<list.size();i++){
				SVNLogEntry entry = list.get(i);
				if(author.equals(entry.getAuthor())){
					list2.add(entry);
				}
			}
			return list2;
		} catch (SVNException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 返回指定版本号范围的svn提交记录
	 * @author likaihao 
	 * @date 2015年9月1日 上午9:58:00
	 * @param projectName 项目名称
	 * @param startVersionNo 开始版本号
	 * @param endVersionNo 结束版本号
	 * @param username 提交的用户
	 * @return map包含version,data,auth,pathMarkMap
	 */
	public static List<HashMap<String,Object>> getSVNVersionLogMap(String subPath,long startVersion,long endVersion,String author){
		
		List<SVNLogEntry> list = getSVNLog(subPath, startVersion, endVersion, author);
		
		List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String,Object>>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(SVNLogEntry logEntry : list){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("version", logEntry.getRevision());
			map.put("date", format.format(logEntry.getDate()));
			map.put("auth", logEntry.getAuthor());
			map.put("message", logEntry.getMessage());
			Map<String,String> pathMarkMap = new HashMap<String,String>();
			for(Object path : logEntry.getChangedPaths().keySet()){
				if(path.toString().startsWith("/"+subPath) && path.toString().length()>=subPath.length()+2){
					String p = path.toString().substring(subPath.lastIndexOf("/")+2);
					pathMarkMap.put(p, logEntry.getChangedPaths().get(path).toString().substring(0,1));
				}
			}
			map.put("pathMarkMap", pathMarkMap);
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 获得某个路径前n条svn提交记录
	 * @author likaihao 
	 * @date 2015年9月1日 上午9:58:00
	 * @param subPath 路径
	 * @return map包含version,data,auth
	 */
	public static List<Map<String,Object>> getSVNTopnVersionLogMap(String subPath,Integer topNum,String author){
		List<SVNLogEntry> list = getSVNLog(subPath, 0, -1, author);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i=list.size()-1;i>=list.size()-topNum&&i>=0;i--){
			SVNLogEntry logEntry = list.get(i);
			HashMap<String,Object> map = new LinkedHashMap<String,Object>();
			map.put("version", logEntry.getRevision()+"");
			map.put("date", format.format(logEntry.getDate()));
			map.put("auth", logEntry.getAuthor());
			map.put("message", logEntry.getMessage());
			Map<String,String> pathMarkMap = new HashMap<String,String>();
			for(Object path : logEntry.getChangedPaths().keySet()){
				String p = path.toString().substring(subPath.length()+2);
				pathMarkMap.put(p, logEntry.getChangedPaths().get(path).toString().substring(0,1));
			}
			map.put("pathMarkMap", pathMarkMap);
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 打印某个路径前n条svn提交记录
	 * @author likaihao
	 * @date 2015年9月16日 下午3:20:55
	 * @param subPath
	 * @param topNum
	 * @param author
	 */
	public static void printSVNTopnVersionLog(String subPath,Integer topNum,String author){
		List<Map<String,Object>> list = getSVNTopnVersionLogMap(subPath, topNum, author);
		System.out.println("版本号\t\t作者\t\t               时间\t\t\t\t说明");
		for(Map<String,Object> map : list){
			String version = (String) map.get("version");
			String date = (String) map.get("date");
			String auth = (String) map.get("auth");
			String message = (String) map.get("message");
			if(message==null || message.length()==0){
				message = "null";
			}
			System.out.println(version+"\t\t"+StringUtils.fillSpace(auth, 20, 10)+date+"\t\t"+message);
		}
	}
	
	/**
	 * 打印svn最后一次提交的文件路径
	 * @param subPath
	 */
	public static void printSVNLastVersionLog(String projectName){
		Map<String,Object> map = getSVNVersionLogMap(projectName,-1,-1,null).get(0);
		printSVNVersionLog(map);
	}
	
	/**
	 * 打印多次svn提交日志
	 * @author likaihao
	 * @date 2015年9月2日 下午2:33:22
	 * @param projectName
	 * @param startVersionNo
	 * @param endVersionNo
	 * @param username
	 */
	@SuppressWarnings("unchecked")
	public static void printSVNVersionLogList(String subPath,long startVersionNo,long endVersionNo,String username){
		List<HashMap<String,Object>> list = getSVNVersionLogMap(subPath,startVersionNo,endVersionNo,username);
		//分开打印
		for(Map<String,Object> map : list){
			printSVNVersionLog(map);
			System.out.println();
		}
		//合并打印
		System.out.println("========================合并打印============================================");
		// * 去重
		Map<String,String> map2 = new HashMap<String,String>();
		for(Map<String,Object> map : list){
			Map<String,String> pathMarkMap = (Map<String,String>) map.get("pathMarkMap");
			map2.putAll(pathMarkMap);
		}
		// * 处理
		List<String> errorList = new ArrayList<String>();
		List<String> filePathList = getFilePathList(map2,errorList);
		for(String filePath : filePathList){
			System.out.println(filePath);
		}
		for(String errorMsg : errorList){
			System.err.println(errorMsg);
		}
		System.out.println("总共有次"+list.size()+"提交记录,去重后有"+filePathList.size()+"个文件");
	}
	
	/**
	 * 打印一条svn日志
	 * @author likaihao
	 * @date 2015年9月2日 下午2:33:22
	 * @param projectName
	 * @param startVersionNo
	 * @param endVersionNo
	 * @param username
	 */
	@SuppressWarnings("unchecked")
	public static void printSVNVersionLog(Map<String,Object> map){
		Long version = (Long) map.get("version");
		String date = (String) map.get("date");
		String auth = (String) map.get("auth");
		Map<String,String> pathMarkMap = (Map<String,String>) map.get("pathMarkMap");
		System.out.println("版本:"+version+"   时间:"+date+"   作者:"+auth);
		System.out.println("--------------------------------------------------------------");
		
		// * 处理
		List<String> errorList = new ArrayList<String>();
		List<String> filePathList = getFilePathList(pathMarkMap,errorList);
		for(String filePath : filePathList){
			System.out.println(filePath);
		}
		for(String errorMsg : errorList){
			System.err.println(errorMsg);
		}
	}
	
	/**
	 * 获取处理过的文件路径
	 * @author likaihao
	 * @date 2015年9月2日 下午2:49:39
	 * @param map
	 * @return
	 */
	private static List<String> getFilePathList(Map<String,String> map,List<String> errorList){
		// * 去除文件夹和删除了的文件
		List<String> filePathList = new ArrayList<String>();
		for(String filePath : map.keySet()){
			//不要输出文件夹
			if(!IOUtils.getSuffix(filePath).equals("") || !filePath.startsWith("conf")){
				//去除删除了的文件
				if(!map.get(filePath).equals("D")){
					filePathList.add(filePath);
				}else{
					errorList.add("删除的文件:"+filePath);
				}
			}else{
				errorList.add("忽略的文件:"+filePath);
			}
		}
		// * 排序
		Collections.sort(filePathList);
		return filePathList;
	}
	
	/**
	 * 获取指定版本号的文件内容
	 * @author likaihao
	 * @date 2015年10月14日 上午10:55:01
	 * @param path
	 * @param revision
	 * @return
	 */
	public static String getFileContentByRevision(String path, Integer revision){
		try {
			//此变量用来存放要查看的文件的属性名/属性值列表。
			SVNProperties fileProperties = new SVNProperties();
			//此输出流用来存放要查看的文件的内容。
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//获取svn上的文件路径
			SVNInfo info = clientManager.getWCClient().doInfo(new File(path), SVNRevision.HEAD);
			String svnPath = info.getPath();
			//获取文件内容
			repository.getFile(svnPath, revision, fileProperties, out);
			return new String(out.toByteArray(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 对比同一文件的不同版本
	 * @author likaihao
	 * @date 2015年10月14日 上午9:48:10
	 * @param path1 文件路径 
	 * @param revision1 版本1
	 * @param revision2 版本2
	 * @return
	 */
	@SuppressWarnings("all")
	public static String diffOneFile(String path, SVNRevision revision1, SVNRevision revision2){
		try {
			/**
			 * SVNRevision.WORKING版本指工作副本中当前内容的版本
			 * SVNRevision.HEAD版本指的是版本库中最新的版本
			 * SVNRevision.PREVIOUS版本指的是版本库中的上一个版本
			 */
			SVNDiffClient diffClient = clientManager.getDiffClient();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			diffClient.doDiff(new File(path), SVNRevision.HEAD, revision1, revision2, SVNDepth.INFINITY, true, out, null);
			return new String(out.toByteArray(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "error:"+e.getMessage();
		}
	}
	
	/**
	 * 解析比较的结果
	 * @author likaihao
	 * @date 2015年10月14日 下午3:27:05
	 * @param diffStr 比较字符串
	 * @return 第一个map为旧版本,第二个版本为新版本. map中包含: path,revision,content. (添加用单独行++++表示,修改用@@@@放到原内容前显示)
	 */
	public static List<Map<String,String>> parseDiffStr(String diffStr){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> oldMap = new HashMap<String,String>();
		Map<String,String> newMap = new HashMap<String,String>();
		
		int lastEqualsLineNum = 1;
		String[] oldContentLineArr = null;//旧版本的内容
		List<String> oldContentList = new ArrayList<String>();//旧版本的添加占位符的内容
		List<String> newContentList = new ArrayList<String>();//新版本的添加占位符的内容
		List<String> oldDiffList = new ArrayList<String>();//旧版本特有的的内容
		List<String> newDiffList = new ArrayList<String>();//新版本特有的的内容
		
		String[] diffStrArr = diffStr.split("\r\n|\n");
		for(String str : diffStrArr){
			//获取路径和版本
			if(str.startsWith("--- ") || str.startsWith("+++ ")){
				String startStr = "";
				Map<String,String> sameMap = null;
				if(str.startsWith("--- ")){
					startStr = "--- ";
					sameMap = oldMap;
				}else{
					startStr = "\\+\\+\\+ ";
					sameMap = newMap;
				}
				List<String> reList = RegexUtils.getSubstrAllGroupByRegex(str, startStr+"(.+?)	\\((.+?)\\)");
				sameMap.put("path", reList.get(1));
				if(reList.get(2).startsWith("revision ")){
					sameMap.put("revision", reList.get(2).split(" ")[1]);
				}else{
					sameMap.put("revision", reList.get(2));
				}
				
				
				if(oldMap.containsKey("revision") && newMap.containsKey("revision")){
					if(oldMap.get("revision").equals("nonexistent")){
						//获取新版本的内容
						String newContent = getFileContentByRevision(newMap.get("path"), Integer.parseInt(newMap.get("revision")));
						String[] newContentLineArr = newContent.split("\r\n|\r|\n");
						oldContentLineArr = new String[newContentLineArr.length];
						for(int i=0;i<newContentLineArr.length;i++){
							oldContentList.add("++++");
							newContentList.add("++++"+newContentLineArr[i]);
						}
						lastEqualsLineNum = newContentLineArr.length+1;
						break;
					}else{
						//获取旧版本的内容
						String oldContent = getFileContentByRevision(oldMap.get("path"), Integer.parseInt(oldMap.get("revision")));
						oldContentLineArr = oldContent.split("\r\n|\r|\n");
					}
				}
			}
			//解析范围,添加相同的代码
			else if(str.startsWith("@@")){
				//添加一样的内容
				List<String> reList = RegexUtils.getSubstrAllGroupByRegex(str, "@@ -(.+?),(.+?)");
				int startDiffLineNum = new Integer(reList.get(1));//开始差异的行号
				for(int i=lastEqualsLineNum;i<startDiffLineNum;i++){
					oldContentList.add(oldContentLineArr[i-1]);
					newContentList.add(oldContentLineArr[i-1]);
				}
				lastEqualsLineNum = startDiffLineNum + new Integer(reList.get(2));//结束差异的行号
			}
			//解析差异文本
			else{
				if(lastEqualsLineNum!=1){ //存在不同的内容
					if(str.startsWith("-")){
						//旧版本的内容
						oldDiffList.add(str.substring(1));
					}else if(str.startsWith("+")){
						//新版本的内容
						newDiffList.add(str.substring(1));
					}else{
						//添加上次差异的内容
						if(oldDiffList.size()>0 || newDiffList.size()>0){
							//如果有存在一个为空,则为添加和删除,否则为修改 .添加用单独行++++表示,修改用@@@@放到原内容前显示
							//添加和删除
							if(oldDiffList.size()==0){
								for(int i=0;i<newDiffList.size();i++){
									oldDiffList.add("++++");
									newDiffList.set(i,"++++"+newDiffList.get(i));
								}
							}else if(newDiffList.size()==0){
								for(int i=0;i<oldDiffList.size();i++){
									newDiffList.add("++++");
									oldDiffList.set(i,"++++"+oldDiffList.get(i));
								}
							}
							//修改
							else{
								List<String> bigList = null;
								List<String> smallList = null;
								bigList = oldDiffList.size()>newDiffList.size()?oldDiffList:newDiffList;
								smallList = oldDiffList.size()>newDiffList.size()?newDiffList:oldDiffList;
								for(int i=0;i<bigList.size();i++){
									if(i<smallList.size()){
										smallList.set(i, "@@@@"+smallList.get(i));
									}else{
										smallList.add("@@@@");
									}
									bigList.set(i, "@@@@"+bigList.get(i));
								}
							}
							oldContentList.addAll(oldDiffList);
							newContentList.addAll(newDiffList);
							oldDiffList.clear();
							newDiffList.clear();
						}
						//共有的内容
						oldContentList.add(str);
						newContentList.add(str);
					}
				}
			}
		}
		
		if(oldContentLineArr!=null){
			for(int i=lastEqualsLineNum;i<=oldContentLineArr.length;i++){
				//共有的内容
				oldContentList.add(oldContentLineArr[i-1]);
				newContentList.add(oldContentLineArr[i-1]);
			}
		}

		oldMap.put("content",StringUtils.join(oldContentList, "\r\n"));
		newMap.put("content",StringUtils.join(newContentList, "\r\n"));
		
		//将最新版本放到最前
		if(oldMap.containsKey("revision") && newMap.containsKey("revision")){
			if(oldMap.get("revision").compareTo(newMap.get("revision"))>0){
				list.add(newMap);
				list.add(oldMap);
			}
		}
		if(list.size()==0){
			list.add(oldMap);
			list.add(newMap);
		}
		return list;
	}
	
	/**
	 * 将比较结果转换为html可以显示的字符串
	 * @author likaihao
	 * @date 2015年10月14日 下午4:10:22
	 * @param diffStr
	 * @return
	 */
	public static List<Map<String,String>> parseDiffStrToHtml(String diffStr){
		List<Map<String,String>> list = parseDiffStr(diffStr);
		for(Map<String,String> map : list){
			String content = map.get("content");
			content = content.replace("<", "&lt;").replace(">", "&gt;");//.replace("&", "&amp;") 要放也放到前面
			String[] lineArr = content.split("\r\n");
			for(int i=0;i<lineArr.length;i++){
				if(lineArr[i].startsWith("++++")){
					lineArr[i] = "<span class=\"code_add\">"+lineArr[i].substring(4)+"</span>";
				}else if(lineArr[i].startsWith("@@@@")){
					lineArr[i] = "<span class=\"code_modify\">"+lineArr[i].substring(4)+"</span>";
				}
			}
			map.put("content", StringUtils.join(lineArr, "\r\n"));
		}
		return list;
	}
	
	/**
	 * 获取本地文件在svn上的路径
	 * @param locaPath
	 * @return
	 */
	public static String getSVNPath(String locaPath){
		try {
			SVNInfo info = clientManager.getWCClient().doInfo(new File(locaPath), SVNRevision.HEAD);
			return info.getPath();
		} catch (SVNException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}