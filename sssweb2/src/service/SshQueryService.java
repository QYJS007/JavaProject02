package service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.generate.RemoteProject;
import model.vo.ParseResult;
import model.vo.SearchResult;
import utils.IOUtils;
import utils.LoggerUtils;
import utils.RegexUtils;
import utils.SSHUtils;
import utils.StringUtils;
import utils.ThreadConcurrentUtils;
import utils.model.concurrent.Job;
import ch.ethz.ssh2.Connection;
import dao.generate.RemoteProjectGenDao;

public class SshQueryService {
	/**
	 * 页面使用的方法
	 * @author likaihao
	 * @date 2017年1月30日 下午5:37:52
	 * @param filePath
	 * @param queryStr
	 * @param matchCase
	 * @param showLineNum
	 * @return
	 */
	public Map<String,String> pageSshQuery(String filePath,String queryStr,boolean matchCase,Integer showLineNum){
		RemoteProjectGenDao remoteProjectGenDao = new RemoteProjectGenDao();
		
		//解析页面参数
		List<ClientSearchParam> clientSearchParamList = new ArrayList<ClientSearchParam>();
		String[] lineArr = filePath.trim().split("[\r\n]+");
		for(String line : lineArr){
			String[] arr = line.split(" > ");
			String name = arr[0].trim();
			String searchPath = arr[1].trim();
			//根据名称查询远程项目信息
			RemoteProject project = remoteProjectGenDao.findCollectionByConditionNoPage(" and name=?", new Object[]{name}, null).get(0);
			//组装clientSearchParamList
			clientSearchParamList.add(new ClientSearchParam(project.getIp(), project.getUsername(), project.getPassword(), searchPath));
		}
		
		return sshQuery(clientSearchParamList, queryStr, matchCase, showLineNum);
	}
	
	/**
	 * 便捷方法,从一个服务器上搜索文件
	 * @author likaihao
	 * @date 2017年1月30日 下午2:25:21
	 * @param ip
	 * @param username
	 * @param password
	 * @param basePath
	 * @param queryStr
	 * @param matchCase
	 * @param showLineNum
	 * @return
	 */
	public Map<String,String> sshQuery(String ip, String username, String password, String basePath, String queryStr, boolean matchCase,final Integer showLineNum){
		ClientSearchParam clientSearchParam = new ClientSearchParam(ip, username, password, basePath);
		List<ClientSearchParam> clientSearchParamList = new ArrayList<ClientSearchParam>();
		clientSearchParamList.add(clientSearchParam);
		return sshQuery(clientSearchParamList, queryStr, matchCase, showLineNum);
	}
	
	/**
	 * 从多个服务器上搜索文件
	 * @author likaihao
	 * @date 2017年1月30日 下午2:21:06
	 * @param clientSearchParamList 服务器信息
	 * @param queryStr 搜索字符串
	 * @param matchCase 是否区分大小写
	 * @param showLineNum 显示相关的行数
	 * @return
	 */
	public Map<String,String> sshQuery(List<ClientSearchParam> clientSearchParamList, final String queryStr, final boolean matchCase, final Integer showLineNum){
		Long lastTime = System.currentTimeMillis();
		
		//搜索文件
		final List<ParseResult> parseResultList = new ArrayList<ParseResult>();
		final List<String> errorList = new ArrayList<String>();
		ThreadConcurrentUtils.common(clientSearchParamList, new Job<ClientSearchParam, ParseResult>(){
			public ParseResult doJob(ClientSearchParam param) {
				try {
					List<ParseResult> list = sshQuery_searchAndParse(param.getIp(), param.getUsername(), param.getPassword(), param.getSearchPath(), queryStr, matchCase, showLineNum);
					parseResultList.addAll(list);
				} catch (Exception e) {
					errorList.add("查询出现错误\nip:"+param.getIp()+"\n路径:"+param.getSearchPath()+"\n错误内容:"+e.getMessage());
				}
				return null;
			}
		}, clientSearchParamList.size(), 0).getValueList();
		
		if(errorList.size()>0){
			throw new RuntimeException(errorList.get(0));
		}
		
		//汇总结果
		Map<String,String> searchResultMap = null;
		if(parseResultList.size()==0){
			searchResultMap = new HashMap<String,String>();
			searchResultMap.put("title", "");
			searchResultMap.put("content", "");
			searchResultMap.put("lineCount", "0");
		}else{
			searchResultMap = new QueryService().searchAndParse_collect(parseResultList);
		}
		
		//记录时间
		Long lastTime2 = System.currentTimeMillis();
		searchResultMap.put("time", (lastTime2 -lastTime)+"");
		LoggerUtils.info("搜索完成,花费"+(lastTime2 -lastTime)+"毫秒");
		return searchResultMap;
	}
	
	
	/**
	 * ssh文件查询
	 * @author likaihao
	 * @date 2016年6月29日 下午12:26:21
	 * @param projectId 远程项目id
	 * @param basePath 搜索路径
	 * @param queryStr 搜索字符串
	 * @param matchCase 是否区分大小写
	 * @param showLineNum 显示相关的行数
	 * @return
	 */
	private List<ParseResult> sshQuery_searchAndParse(final String ip, String username, String password, final String basePath,final String queryStr,final boolean matchCase,final Integer showLineNum){
		//拼凑grep命令并执行
		String command = null;
		if(queryStr.contains("@@")){
			//使用正则模式进行查询
			//将查询字符串 进行正则转义,并将@@替换为.*
			String newQueryStr = StringUtils.getRegexEscapeString(queryStr).replace("@@", ".*");
			if(matchCase){
				command = "grep -EnrC"+showLineNum+" '"+newQueryStr+"' "+basePath;
			}else{
				//不区分大小写会非常慢
				command = "grep -EnriC"+showLineNum+" '"+newQueryStr+"' "+basePath;
			}
		}else{
			if(matchCase){
				command = "grep -nrC"+showLineNum+" '"+queryStr+"' "+basePath;
			}else{
				//不区分大小写会非常慢
				command = "grep -nriC"+showLineNum+" '"+queryStr+"' "+basePath;
			}
		}
		
		//执行命令,压缩下载(压缩下载能大大提高效率 1~100秒 能控制到 3~10秒)
		// * 执行命令(将结果保存到linux某个文本文件中)
		String resultFileName = "sshSearch.log." + StringUtils.getRandomStr();
		String remoteFilePath = "/tmp/" + resultFileName;//创建在tmp中不会有权限问题,其它地方可能没有写权限
		String localFilePath = IOUtils.getHomeDirectoryPath() + "/" + resultFileName;
		command += " > " + remoteFilePath;
		Connection conn = SSHUtils.getSSHConnection(ip, username, password);
		SSHUtils.execLinuxCommandByClient(conn, null, Arrays.asList(command), null);
		// * 下载文件
		SSHUtils.downloadLinuxFileCompress(conn, remoteFilePath, localFilePath);
		// * 删除服务器上的结果文件
		command = "rm -f " + remoteFilePath;
		SSHUtils.execLinuxCommandByClient(conn, null, Arrays.asList(command), null);
		
		//处理搜索结果
		// * 如果文件大于20M,则不再解析(一个字母1字节,一个汉字3字节,字母占大多数)
		File localFile = new File(localFilePath);
		if(localFile.length()>20 * 1024 * 1024){
			throw new RuntimeException("文件过大,已将结果保存到桌面:"+resultFileName);
		}
		// * 读取文件
		String result = IOUtils.readFile(localFilePath);
		StringBuilder resultBuilder = new StringBuilder(result);
		// * 去除前后空白字符
		result = resultBuilder.toString().trim();
		// * 去除二进制搜索内容(以"Binary file "开头的行)
		result = result.replaceAll("(^|\r\n)Binary file .*?(?=(\r\n|$))", "");
		
		List<ParseResult> parseResultList = new ArrayList<ParseResult>();
		if(result.length()>0){
			//判断是搜索文件还是目录
			Boolean isSearchFile = result.substring(0,1).matches("\\d");//开头为数字
			
			//分隔每个匹配内容(一个文件中有多个匹配项也是用--分隔的)
			String[] fileContentArr = result.split("\r\n--\r\n"); //如果文件内容中含有这个也没办法
			
			//记录文件的搜索信息
			Map<String,SearchResult> fileSearchResultMap = new HashMap<String,SearchResult>();
			for(int i=0;i<fileContentArr.length;i++){
				if(fileContentArr[i].length()==0){
					continue;
				}
				//获取文件信息
				String filePath = null;
				List<String> contentList = null;
				List<Integer> lineNumList = null;
				if(showLineNum>0){
					SearchResult searchResult = getSearchResult(fileContentArr[i],isSearchFile,basePath,fileSearchResultMap);
					filePath = searchResult.getFilePath();
					contentList = searchResult.getContentList();
					lineNumList = searchResult.getLineNumList();
				}
				
				//获取行号,内容
				String[] lineArr = fileContentArr[i].split("\r\n");
				Pattern re = Pattern.compile("^(\\d{1,8})([-:])");
				
				Boolean isMatchLine = false;//是否是匹配行
				String content = null;//行的内容
				Integer lineNum = null;//行号
				for(String allLine : lineArr){
					//如果相关行数是0,则不会存在--分隔符,需要重新获取文件信息
					if(showLineNum==0){
						SearchResult searchResult = getSearchResult(allLine,isSearchFile,basePath,fileSearchResultMap);
						filePath = searchResult.getFilePath();
						contentList = searchResult.getContentList();
						lineNumList = searchResult.getLineNumList();
					}
					
					try {
						String line = allLine;
						if(!isSearchFile){
							line = line.substring(filePath.length()+1);
						}
						
						//获取指定的行号,内容
						Matcher matcher = re.matcher(line);
						if(!matcher.find()){
							throw new RuntimeException("存在没有匹配的行:"+line+"@@@filePath:"+filePath);
						}
						isMatchLine = matcher.group(2).equals(":");
						lineNum = new Integer(matcher.group(1));
						content = line.substring(lineNum.toString().length() + 1);
						
						//如果是匹配行,记录行号
						if(isMatchLine){
							lineNumList.add(lineNum);
						}
						
						//记录相关行文件内容,不相关的记为null占位
						int diff = lineNum-contentList.size()-1;
						for(int j=0;j<diff;j++){
							contentList.add(null);
						}
						contentList.add(content);
					} catch (Exception e) {
						//调试用,分析错误
						System.out.println("错误行内容:"+allLine+"@@@filePath:"+filePath);
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}
			
			List<SearchResult> paramList = new ArrayList<SearchResult>(fileSearchResultMap.values());
			parseResultList = ThreadConcurrentUtils.common(paramList, new Job<SearchResult,ParseResult>(){
				public ParseResult doJob(SearchResult searchResult) {
					searchResult.setFilePath(searchResult.getFilePath()+"@"+ip);
					ParseResult m = new QueryService().searchAndParse_parse(searchResult, "", queryStr, matchCase, StringUtils.getTempRandomInteger(), showLineNum);
					return m;
				}
			}, 3, 0).getValueList();
		}
		
		//删除桌面的搜索结果文件
		new File(localFilePath).delete();
		
		return parseResultList;
	}
	
	/**
	 * 从文本中获取相关的文件搜索信息
	 * @author likaihao
	 * @date 2016年7月22日 上午9:40:37
	 * @param result 文本
	 * @param isSearchFile 是否搜索的是文件
	 * @param basePath 搜索路径
	 * @param fileSearchResultMap 文件搜索信息的map
	 * @return
	 */
	private SearchResult getSearchResult(String result,boolean isSearchFile,String basePath,Map<String,SearchResult> fileSearchResultMap){
		//获取文件路径
		String filePath = null;
		if(isSearchFile){
			filePath = basePath;
		}else{
			filePath = RegexUtils.getSubstrByRegex(result, "(^|\r\n)(/[^\r\n :]+?):\\d{1,8}:");
			if(filePath==null || filePath.length()==0){
				throw new RuntimeException("获取文件路径失败,请查看桌面的日志");
			}
		}
		
		//判断map中是否存在文件
		SearchResult searchResult = null;
		if(fileSearchResultMap.containsKey(filePath)){
			searchResult = fileSearchResultMap.get(filePath);
		}else{
			searchResult = new SearchResult();
			searchResult.setFilePath(filePath);
			searchResult.setContentList(new ArrayList<String>());
			searchResult.setLineNumList(new ArrayList<Integer>());
			fileSearchResultMap.put(filePath, searchResult);
		}
		return searchResult;
	}
	
}

class ClientSearchParam{
	private String ip;
	private String username;
	private String password;
	private String searchPath;
	
	public ClientSearchParam(String ip, String username, String password,
			String searchPath) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.searchPath = searchPath;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSearchPath() {
		return searchPath;
	}
	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}
}