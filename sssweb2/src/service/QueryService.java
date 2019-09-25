package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import model.generate.QueryTemplet;
import model.vo.ParseResult;
import model.vo.SearchResult;
import service.generate.QueryTempletGenService;
import sys.SystemConf;
import utils.IOUtils;
import utils.LoggerUtils;
import utils.OfficeUtils;
import utils.StringUtils;
import utils.ThreadConcurrentUtils;
import utils.model.concurrent.Job;

public class QueryService {
	
	QueryTempletGenService queryTempletGenService = new QueryTempletGenService();
	
	/**
	 * 获得所有模板
	 * @author likaihao
	 * @date 2016年4月25日 下午4:02:25
	 * @return
	 */
	public List<QueryTemplet> getQueryList(){
		List<QueryTemplet> list = queryTempletGenService.findAll();
		//如果以workspace开头,认为是工作空间,遍历下面的文件夹
		List<QueryTemplet> list2 = new ArrayList<QueryTemplet>();
		for(QueryTemplet query : list){
			if(query.getName().startsWith("workspace")){
				File workDir = new File(query.getPath());
				File[] projectArr = workDir.listFiles();
				if(projectArr!=null){
					for(File project : projectArr){
						if(project.isDirectory() && !project.getName().startsWith(".")){
							QueryTemplet q = new QueryTemplet();
							q.setName(query.getName()+"> "+project.getName());
							q.setPath(project.getAbsolutePath());
							q.setPathPattern(query.getPathPattern());
							q.setFileNamePattern(query.getFileNamePattern());
							q.setNoFileNamePattern(query.getNoFileNamePattern());
							q.setNoPathPattern(query.getNoPathPattern());
							q.setEncoding(query.getEncoding());
							list2.add(q);
						}
					}
				}
			}else{
				list2.add(query);
			}
		}
		return list2;
	}
	
	/**
	 * 文件查询
	 * @author likaihao
	 * @date 2015年12月17日 下午2:40:57
	 * @param path 文件路径
	 * @param pathPattern 要搜索的路径模式
	 * @param noPathPattern 不搜索的路径模式
	 * @param fileNamePattern 允许的文件名称模式
	 * @param noFileNamePattern 不允许的文件名称模式
	 * @param matchCase 是否区分大小写
	 * @param queryStr 查询的字符串,如果有多个用@@分隔,表示同一行要同时包含多个搜索字符串
	 * @param encoding 文件编码
	 * @param showLineNum 显示相关的行数
	 * @return 
	 */
	public Map<String,String> query(final String path,String pathPattern,String noPathPattern,String fileNamePattern,String noFileNamePattern,final boolean matchCase,final String queryStr,final String encoding,final int showLineNum){
		Long lastTime = System.currentTimeMillis();
		
		//获得文件
		final List<File> fileList = getFileList(path, pathPattern, noPathPattern, fileNamePattern, noFileNamePattern);
		LoggerUtils.info("文件搜索_获得文件:"+fileList.size()+"个");
		
		//搜索,使用多线程大幅加快效率(开3个线程)
		List<ParseResult> list = ThreadConcurrentUtils.common(fileList, new Job<File,ParseResult>(){
			//共享的字段
			int fileNum = 0; //搜索的文件数-1
			public synchronized int getFileNum(){
				if(fileNum>0 && fileNum%100==0){
					LoggerUtils.info("文件搜索_搜索进度:"+fileNum+"/"+fileList.size());
				}
				return fileNum++;
			}
			
			public ParseResult doJob(File file) {
				int fileNum = getFileNum();
				//查询
				SearchResult result = searchAndParse_search(file, queryStr, matchCase, encoding, showLineNum);
				if(result==null){
					return null;
				}
				//解析结果
				return searchAndParse_parse(result, path, queryStr, matchCase, fileNum, showLineNum);
			}
		}, 3, 0).getValueList();
		
		LoggerUtils.info("文件搜索_搜索进度:"+fileList.size()+"/"+fileList.size());
		
		//将结果汇总
		Map<String,String> map = searchAndParse_collect(list);
		
		Long diff = (System.currentTimeMillis() - lastTime);
		map.put("fileCount",fileList.size()+"");
		map.put("time", diff+"");
		return map;
	}
	
	/**
	 * 获得符合条件的文件列表
	 * @author likaihao
	 * @date 2015年12月18日 上午11:50:11
	 * @param path 文件路径
	 * @param pathPattern 符合的路径模式
	 * @param noPathPattern 不符合的路径模式
	 * @param fileNamePattern 符合的文件名称模式
	 * @param noFileNamePattern 不符合的文件名称模式
	 * @return 符合条件的文件列表
	 */
	public List<File> getFileList(String path,String pathPattern,String noPathPattern,String fileNamePattern,String noFileNamePattern){
		//去除配置的文件夹
		String _noPathPattern = SystemConf.getResourceValue("query_noPathPattern");
		if(_noPathPattern!=null){
			if(noPathPattern==null || noPathPattern.length()==0){
				noPathPattern = _noPathPattern;
			}else{
				noPathPattern+=","+_noPathPattern;
			}
		}
		
		//去除配置的文件
		String _noFileNamePattern = SystemConf.getResourceValue("query_noFileNamePattern");
		if(_noFileNamePattern!=null){
			if(noFileNamePattern==null || noFileNamePattern.length()==0){
				noFileNamePattern = _noFileNamePattern;
			}else{
				noFileNamePattern += ","+_noFileNamePattern;
			}
		}
		
		String[] pathPatternArr = null;
		if(pathPattern!=null && pathPattern.length()>0){
			pathPatternArr = pathPattern.split(",");
		}
		String[] noPathPatternArr = null;
		if(noPathPattern!=null && noPathPattern.length()>0){
			noPathPatternArr = noPathPattern.split(",");
		}
		String[] fileNamePatternArr = null;
		if(fileNamePattern!=null && fileNamePattern.length()>0){
			fileNamePatternArr = fileNamePattern.split(",");
		}
		String[] noFileNamePatternArr = null;
		if(noFileNamePattern!=null && noFileNamePattern.length()>0){
			noFileNamePatternArr = noFileNamePattern.split(",");
		}
		
		//获得文件
		List<File> fileList = IOUtils.getFileListByPattern(path, pathPatternArr, noPathPatternArr, fileNamePatternArr, noFileNamePatternArr);
		return fileList;
	}
	
	/**
	 * 替换查找到的字符串
	 * @author likaihao
	 * @date 2015年12月18日 上午11:52:18
	 * @param path 文件路径
	 * @param pathPattern 要搜索的路径模式
	 * @param noPathPattern 不搜索的路径模式
	 * @param fileNamePattern 允许的文件名称模式
	 * @param noFileNamePattern 不允许的文件名称模式
	 * @param matchCase 是否区分大小写
	 * @param queryStr 查询的字符串
	 * @param replaceStr 替换的字符串
	 * @param encoding 文件编码
	 * @return 成功或失败
	 */
	public String replace(String path,String pathPattern,String noPathPattern,String fileNamePattern,String noFileNamePattern,final boolean matchCase,final String queryStr, String replaceStr,final String encoding){
		//获得文件
		List<File> fileList = getFileList(path, pathPattern, noPathPattern, fileNamePattern, noFileNamePattern);
		
		//获得所有查询到内容的文件路径
		//开3个线程
		List<SearchResult> list = ThreadConcurrentUtils.common(fileList, new Job<File,SearchResult>(){
			public SearchResult doJob(File file) {
				//查询
				SearchResult result = searchAndParse_search(file, queryStr, matchCase, encoding, 0);
				return result;
			}
		}, 3, 0).getValueList();
		List<String> filePathList = new ArrayList<String>();
		for(SearchResult result : list){
			filePathList.add(result.getFilePath());
		}
		
		//替换
		for(String filePath : filePathList){
			String encoding2 = encoding;
			if(encoding.equals("auto")){
				encoding2 = IOUtils.getFileEncode(new File(filePath));
			}
			String content = IOUtils.readFile(filePath,encoding2);
			content = content.replace(queryStr, replaceStr);
			IOUtils.writeFileReplace(filePath, content, encoding2);
		}
		return "完成";
	}
	
	/**
	 * 搜索文件
	 * @author likaihao
	 * @date 2015年10月19日 下午2:30:55
	 * @param file 文件
	 * @param searchStr 要搜索的字符串,如果有多个用@@分隔,表示同一行要同时包含多个搜索字符串
	 * @param matchCase 是否区分大小写
	 * @param encoding 文件编码
	 * @param showLineNum 显示相关的行号
	 * @return 
	 */
	public SearchResult searchAndParse_search(File file,String searchStr,boolean matchCase,String encoding,Integer showLineNum){
		if(!file.exists()){
			throw new RuntimeException("文件不存在!"+file.getAbsolutePath());
		}
		
		String houZhui = file.getName();
		int index = houZhui.lastIndexOf(".");
		if(index!=-1){
			houZhui = houZhui.substring(index+1);
		}else{
			houZhui = "";
		}
		
		List<String> noTextContentList = null;
		if(houZhui.equals("doc") || houZhui.equals("docx")){
			//word
			noTextContentList = OfficeUtils.readWordReturnList(file);
		}else if(houZhui.equals("xls") || houZhui.equals("xlsx")){
			//excel
			noTextContentList = OfficeUtils.readExcelReturnList(file,"yyyy-MM-dd","◆");
		}
		
		List<String> contentList = new ArrayList<String>();
		List<Integer> lineNumList = new ArrayList<Integer>();
		String[] searchStrArr = null;
		if(!matchCase){
			searchStrArr = searchStr.toUpperCase().split("@@");
		}else{
			searchStrArr = searchStr.split("@@");
		}
		if(encoding.equals("auto")){
			encoding = IOUtils.getFileEncode(file);
		}
		int lineNum = 1;
		if(noTextContentList!=null){
			//word或excel
			for(String line : noTextContentList){
				searchAndParse_search_line(line,matchCase,searchStrArr,lineNumList,contentList,lineNum);
				lineNum++;
			}
		}else{
			//按文本搜索
			BufferedReader in = null;
			try {
				boolean isProperties = file.getName().endsWith(".properties");
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file),encoding));
				String line = null;
				int clearNum = 100000;//多少行清理一次contentList
				while((line = in.readLine())!=null){
					if(isProperties){
						//如果是properties文件要先解码中文
						line = StringUtils.unicodeToString(line);
					}
					searchAndParse_search_line(line,matchCase,searchStrArr,lineNumList,contentList,lineNum);
					lineNum++;
					
					if(lineNum%clearNum==0){
						//需要清理contentList,防止内存溢出
						//获取要显示的行号
						Set<Integer> showLineNumSet = getShowLineNum(lineNumList,showLineNum,contentList.size());
						for(int i=lineNum-clearNum;i<contentList.size()-showLineNum;i++){//后面也需要留出showLineNum个不能清理
							if(!showLineNumSet.contains(new Integer(i+1))){
								contentList.set(i, null);
							}
						}
						LoggerUtils.info("文件搜索_搜索进度_行数,"+file.getName()+",行数:"+lineNum);
					}
				}
			} catch(Exception e){
				LoggerUtils.error("查询文件出错:"+file.getAbsolutePath(), e);
				throw new RuntimeException(e);
			} finally{
				if(in!=null){
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}
		}
		//如果查找到了,存储文件路径,行号和内容
		if(lineNumList.size()>0){
			SearchResult result = new SearchResult();
			result.setFilePath(file.getAbsolutePath());
			result.setContentList(contentList);
			result.setLineNumList(lineNumList);
			return result;
		}
		return null;
	}
	
	//配合searchAndParse_search,搜索一行
	private void searchAndParse_search_line(String line,boolean matchCase,String[] searchStrArr,List<Integer> lineNumList, List<String> contentList,Integer lineNum){
		//判断是否区分大小写
		String lastLine = line;
		if(!matchCase){
			line = line.toUpperCase();
		}
		//搜索字符串
		if(searchStrArr.length==1){ //搜索字符串只有一个
			if(line.contains(searchStrArr[0])){
				lineNumList.add(lineNum);
			}
		}else{ //搜索字符串有多个,遍历判断
			int containsCount = 0;
			for(String s : searchStrArr){
				if(line.contains(s)){
					containsCount++;
				}else{
					break;
				}
			}
			if(containsCount==searchStrArr.length){
				lineNumList.add(lineNum);
			}
		}
		contentList.add(lastLine);
	}
	
	/**
	 * 解析查询结果
	 * @author likaihao
	 * @date 2015年10月19日 下午3:24:13
	 * @param searchResult 查询结果
	 * @param basePath 查询路径(用来获得相对路径)
	 * @param queryStr 查询字符串
	 * @param matchCase 是否区分大小写
	 * @param fileNum 当前文件的编号,用来获取超链接的唯一id
	 * @param showLineNum 显示相关的行数
	 * @return 
	 */
	public ParseResult searchAndParse_parse(SearchResult searchResult, String basePath, String queryStr, boolean matchCase, Integer fileNum, int showLineNum){
		String filePath = searchResult.getFilePath();
		String fileName = IOUtils.getFileName(filePath);
		List<String> contentList = searchResult.getContentList();
		List<Integer> lineNumList = searchResult.getLineNumList();
		try {
			//获取要显示的行号
			Set<Integer> showLineNumSet = getShowLineNum(lineNumList,showLineNum,contentList.size());
			
			//将第一个变红字体加超链接的正则
			String str1 = "(<span class=\"span_red.*?</span>)";
			Pattern firstRedStrAddLinkPattern = null;
			if(matchCase){
				firstRedStrAddLinkPattern = Pattern.compile(str1);
			}else{
				firstRedStrAddLinkPattern = Pattern.compile(str1,Pattern.CASE_INSENSITIVE);
			}
			
			//准备查询字符串的正则
			queryStr = getNewEscapeStr(queryStr);//将&变为|&|
			Pattern queryStrPattern = null;
			String queryPatternStr = "";
			String[] arr = queryStr.split("@@");
			for(int i=0;i<arr.length;i++){
				queryPatternStr += "("+Pattern.quote(arr[i])+")";
				if(i!=arr.length-1){
					queryPatternStr += "|";
				}
			}
			if(matchCase){
				 queryStrPattern = Pattern.compile(queryPatternStr);
			}else{
				 queryStrPattern = Pattern.compile(queryPatternStr, Pattern.CASE_INSENSITIVE);
			}
			
			//获取文件相对路径
			try {
				filePath = filePath.substring(basePath.length()).replace("\\", "/");
			} catch (Exception e) {
				//如果获取失败,则忽略
			}
			
			//处理存在指定字符串的行,准备title,添加超链接
			StringBuilder titleBuilder = new StringBuilder();
			for(Integer nowLineNum : lineNumList){
				String lineContent = contentList.get(nowLineNum-1);
				
				//将&变为|&|
				lineContent = getNewEscapeStr(lineContent);
				
				//将匹配项变红
				lineContent = queryStrPattern.matcher(lineContent).replaceAll("<span class=\"span_red\">$0</span>");
				
				//将|&|变为html转义字符
				lineContent = getOldEscapeStr(lineContent);
				
				//为超链接获取一个唯一的id
				String id = "a"+fileNum+"_"+nowLineNum;
				//标题
				titleBuilder.append("<li><a href=\"#"+id+"\">");
				titleBuilder.append("<span class=\"fileName\">"+fileName+"</span>");
				titleBuilder.append("<span class=\"lineNum\">第"+nowLineNum+"行</span>");
				titleBuilder.append("<span class=\"lineContent\">"+lineContent+"</span>");
				titleBuilder.append("<span class=\"filePath\">"+filePath+"</span>");
				titleBuilder.append("</a></li>");
				
				//添加超链接
				String str2 = "<a href=\"javascript:;\" id=\""+id+"\">$1</a>";
				lineContent = firstRedStrAddLinkPattern.matcher(lineContent).replaceFirst(str2);
				
				contentList.set(nowLineNum-1, lineContent);
			}
			
			//将其它显示的行的<转义
			List<Integer> showLineNumList = new ArrayList<Integer>(showLineNumSet);
			showLineNumList.removeAll(lineNumList);
			for(Integer lineNum : showLineNumList){
				contentList.set(lineNum-1, StringUtils.getHtmlEscapeString(contentList.get(lineNum-1),false));
			}

			//拼装要返回的内容
			StringBuilder builder = new StringBuilder();
			builder.append("\r\n##################################"+fileName+"######################################\r\n");
			int lastLineNum = 0;
			for(Integer lineNum : showLineNumSet){
				//如果行号不连续,要添加省略号
				if(lineNum - lastLineNum > 1){
					if(lastLineNum!=0){
						builder.append("................end "+lastLineNum+"\r\n\r\n");
					}
					builder.append("\r\n................start "+lineNum+"\r\n");
				}
				builder.append(contentList.get(lineNum-1)).append("\r\n");
				lastLineNum = lineNum;
			}
			if(lastLineNum!=contentList.size()){
				builder.append("................end "+lastLineNum+"\r\n\r\n");
			}
			builder.append("\r\n##################################"+fileName+"######################################\r\n");
			
			ParseResult parseResult = new ParseResult();
			parseResult.setFileName(fileName);
			parseResult.setTitle(titleBuilder.toString());
			parseResult.setContent(builder.toString());
			parseResult.setLineCount(lineNumList.size());
			return parseResult;
		} catch (Exception e) {
			LoggerUtils.error("解析出错:"+fileName,e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将结果进行汇总
	 * @param list
	 * @return
	 */
	public Map<String,String> searchAndParse_collect(List<ParseResult> list){
		//排序
		String[] orderbyArr = SystemConf.getResourceValue("query_suffixSort").split(",");
		final List<String> orderbyList = Arrays.asList(orderbyArr);
		Collections.sort(list, new Comparator<ParseResult>(){
			public int compare(ParseResult o1, ParseResult o2) {
				String fileName1 = o1.getFileName();
				String fileName2 = o2.getFileName();
				//先按后缀
				String suffix1 = IOUtils.getSuffix(fileName1);
				String suffix2 = IOUtils.getSuffix(fileName2);
				
				if(suffix1.equals(suffix2)){
					//后缀相同的按名称排序
					return fileName1.compareTo(fileName2);
				}else{
					//按后缀名称排序
					int index1 = orderbyList.indexOf(suffix1);
					int index2 = orderbyList.indexOf(suffix2);
					//没有指定的后缀向后排
					if(index1==-1){
						index1=100;
					}
					if(index2==-1){
						index2=100;
					}
					return index1 - index2;
				}
			}
		});
		StringBuilder titleBuilder = new StringBuilder();
		StringBuilder contentBuilder = new StringBuilder();
		Integer lineCount = 0;
		for(ParseResult result : list){
			titleBuilder.append(result.getTitle());
			contentBuilder.append(result.getContent());
			lineCount += result.getLineCount();
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("title", titleBuilder.toString());
		map.put("content", contentBuilder.toString());
		map.put("lineCount", lineCount.toString());
		return map;
	}
	
	/**
	 * 获取需要显示的行号的set
	 * @author likaihao
	 * @date 2016年6月17日 上午8:57:37
	 * @param lineNumList
	 * @param showLineNum
	 * @param contentListSize
	 * @return
	 */
	private Set<Integer> getShowLineNum(List<Integer> lineNumList,Integer showLineNum,Integer contentListSize){
		//获取要显示的行号
		if(showLineNum<0){
			showLineNum = 0;
		}
		if(showLineNum>contentListSize){
			showLineNum = contentListSize;
		}
		Set<Integer> showLineNumSet = new TreeSet<Integer>();
		for(Integer lineNum : lineNumList){
			for(int i=0;i<=showLineNum;i++){
				if(lineNum-i>0){
					showLineNumSet.add(lineNum-i);
				}
				if(lineNum+i<=contentListSize){
					showLineNumSet.add(lineNum+i);
				}
			}
		}
		return showLineNumSet;
	}
	
	//获得自定义的转义过的字符串(为了将 原字符串的<和新添加的<区别开)
	private String getNewEscapeStr(String str){
		return str.replace("&", "¦&¦").replace(">", "¦>¦").replace("<", "¦<¦").replace(" ", "¦ ¦");
	}
	
	//将自定义的转义字符串 变为html的转义字符
	private String getOldEscapeStr(String str){
		return str.replace("¦&¦", "&amp;").replace("¦>¦", "&gt;").replace("¦<¦", "&lt;").replace("¦ ¦", " ");
	}
	
}