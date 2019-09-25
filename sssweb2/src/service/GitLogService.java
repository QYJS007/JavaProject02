package service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import model.generate.Params;
import utils.DateUtils;
import utils.JsonUtils;
import utils.LoggerUtils;
import utils.RegexUtils;
import utils.SSHUtils;
import utils.StringUtils;


public class GitLogService {
	
	ParamsService paramsService = new ParamsService();
	
	//查询所有的git本地仓库
	public List<String> getAllGitLocalPath(){
		Params params = paramsService.findParamsByName("gitLocalPath");
		String wordPathJson = params.getValue();
		List<String> wordPathList = null;
		try {
			wordPathList = JsonUtils.parseJson2List(wordPathJson.replace("\\", "\\\\"), String.class, null);
		} catch (Exception e) {
			wordPathList = JsonUtils.parseJson2List(wordPathJson, String.class, null);
		}
		return wordPathList;
	}
	
	/**
	 * 获得所有的分支
	 * @author likaihao
	 * @date 2017年2月9日 上午9:39:00
	 * @param path
	 * @return
	 */
	public List<String> getAllBranch(String path){
		//执行命令
		String result = execGit(path, "git branch");
		//处理结果
		String branchStr = RegexUtils.getSubstrByRegex(result, "git branch\r\n([\\s\\S]+?)\r\n\r\n");
		branchStr = branchStr.replace("*", "");
		
		List<String> branchList = new ArrayList<String>();
		String[] lineArr = branchStr.split("\r\n");
		for(String line : lineArr){
			branchList.add(line.trim());
		}
		return branchList;
	}
	
	/**
	 * 获取页面默认值
	 * @author likaihao
	 * @date 2017年2月10日 上午10:33:22
	 * @param path
	 * @return
	 */
	public Map<String,String> getPageDefaultValue(String path){
		Map<String,String> map = new HashMap<String,String>();
		
		//获取当前用户名称
		String result = execGit(path, "git config --get user.name");
		String committer = RegexUtils.getSubstrByRegex(result, "user.name\r\n(.+?)\r\n\r\n");
		
		//获取开始时间和结束时间(当前一月内)
		Calendar c = Calendar.getInstance();
		Date beforeDate = c.getTime();
		c.add(Calendar.MONTH, -1);
		Date afterDate = c.getTime();

		map.put("committer", committer);
		map.put("afterDate", DateUtils.dateToStringYMd(afterDate));
		map.put("beforeDate", DateUtils.dateToStringYMd(beforeDate));
		return map;
	}
	
	/**
	 * 查询日志
	 * @author likaihao
	 * @date 2017年2月9日 上午9:39:07
	 * @param path 本地仓库路径
	 * @param branch 分支
	 * @param committer 提交人
	 * @param afterDate 日期范围
	 * @param beforeDate 日期范围
	 * @param comment 注释,可填写正则
	 * @return
	 */
	public String searchLog(String path, String branch, String committer, String afterDate, String beforeDate, String comment){
		/*
			按分支查询日志		git log xxx
			按提交人查询日志		git log --committer=likaihao
			按输入日期查询日志	git log --after={2017-01-23} --before={2017-01-24} //注意:after不包含当天
			按提交说明过滤日志	git log --grep=支付
			日志显示格式		git log --pretty=format:"@@%h@@%cd@@%an@@%s@@" --date=iso
			合并: git log xxx --pretty=format:"@@%h@@%cd@@%an@@%s@@" --date=iso --all-match --committer=likaihao --after=2017-01-10 --before=2017-01-30 --grep=支付
		 */
		//拼凑命令
		String command = "git log "+branch+" --pretty=format:\"@@%h@@%cd@@%an@@%s@@\" --date=iso --all-match";
		if(committer!=null && committer.length()>0){
			command += " --committer="+committer;
		}
		if(afterDate!=null && afterDate.length()>0){
			command += " --after=\""+afterDate+" 00:00:00\"";
		}
		if(beforeDate!=null && beforeDate.length()>0){
			command += " --before=\""+beforeDate+" 23:59:59\"";
		}
		if(comment!=null && comment.length()>0){
			comment = comment.replaceAll("@@", ".*");
			command += " --grep="+comment;
		}
		
		//执行命令
		String result = execGit(path, command);
		
		//解析结果
		StringBuilder builder = new StringBuilder();
		List<List<String>> list = RegexUtils.getSubstrAllGroupByRegexReturnList(result, "@@(.*?)@@(.*?) \\+0800@@(.*?)@@(.*?)@@");
		for(List<String> subList : list){
			//日期, 提交人, 注释
			builder.append("<option value=\""+subList.get(1)+"\">");
			builder.append(subList.get(2) + "&ensp;&ensp;&ensp;&ensp;&ensp;");
			builder.append(StringUtils.fillSpace(subList.get(3), 18, 2).replace(" ", " "));//浏览器识别的空格:&ensp;
			builder.append(subList.get(4));
			builder.append("</option>\r\n");
		}
		return builder.toString();
	}
	
	/**
	 * 获得指定hashid次提交修改的文件列表
	 * @author likaihao
	 * @date 2017年2月9日 上午10:27:18
	 * @param path
	 * @param branch
	 * @param hashIds
	 * @return
	 */
	public String getChangeFilePath(String path, String branch, String hashIds){
		//git show xxx --pretty=format:"@@%h@@" --date=raw --name-only b96d47 6df1a0
		//拼凑命令
		String command = "git show "+branch+" --pretty=format:\"@@%h@@\" --date=raw --name-only ";
		command += hashIds.replace(",", " ");
		
		//执行命令
		String result = execGit(path, command);
		
		//解析结果
		Set<String> changeFilePathSet = new TreeSet<String>();
		Set<String> noPatternFilePathSet = new TreeSet<String>();//记录不符合条件的文件路径
		// * 提取所有路径
		String[] hashIdArr = hashIds.split(",");
		for(String hashId : hashIdArr){
			if(!result.contains("@@"+hashId+"@@")){
				throw new RuntimeException("没有找到指定提交:"+hashId);
			}
			String filePathStr = RegexUtils.getSubstrByRegex(result, "@@"+hashId+"@@([\\s\\S]*?)\r\n\r\n");
			if(filePathStr.length()>0){
				String[] filePathArr = filePathStr.trim().split("\r\n");
				for(String filePath : filePathArr){
					changeFilePathSet.add(filePath);
				}
			}
		}
		// * 验证路径(文件存在且不是文件夹,且不是指定的文件)
		Set<String> changeFilePathSet2 = new TreeSet<String>();
		List<String> igorNameList = Arrays.asList(new ParamsService().findParamsByName("gitLogIgorName").getValue().split(","));
		for(String subPath : changeFilePathSet){
			File file = new File(path,subPath);
			if(file.exists() && !file.isDirectory() && !igorNameList.contains(file.getName())){
				changeFilePathSet2.add(subPath);
			}else{
				noPatternFilePathSet.add(subPath);
			}
		}
		changeFilePathSet = changeFilePathSet2;
		
		//拼凑页面需要的格式
		// * 按项目分组
		Map<String,StringBuilder> map = new HashMap<String,StringBuilder>();
		for(String filePath : changeFilePathSet){
			if(filePath.startsWith("SRC/")){
				filePath = filePath.substring(4);
			}
			int index = filePath.indexOf("/");
			String projectName = filePath.substring(0,index);
			String subFilePath = filePath.substring(index);
			if(!map.containsKey(projectName)){
				map.put(projectName, new StringBuilder());
			}
			map.get(projectName).append(subFilePath).append("\r\n");
		}
		// * 合并结果
		StringBuilder builder = new StringBuilder();
		for(String projectName : map.keySet()){
			builder.append(projectName).append("\r\n");
			builder.append(map.get(projectName)).append("\r\n");
		}
		if(noPatternFilePathSet.size()>0){
			builder.append("不提交的文件:\r\n");
			for(String filePath : noPatternFilePathSet){
				File file = new File(path,filePath);
				if(!file.exists()){
					builder.append("(文件不存在)"+filePath+"\r\n");
				}else if(file.isDirectory()){
					builder.append("(文件夹)"+filePath+"\r\n");
				}else if(igorNameList.contains(file.getName())){
					builder.append("(忽略的文件)"+filePath+"\r\n");
				}
			}
			
		}
		if(builder.length()==0){
			builder.append("没有发现修改文件,可能是分支合并提交");
		}
		return builder.toString();
	}
	
	//执行git命令,返回结果
	private static String execGit(String path, String command){
		//拼凑命令
		//处理path F:\git\bj_1602_wbtckt --> /F/git/bj_1602_wbtckt
		path = "/" + path.replace(":", "").replace("\\", "/");
		List<String> subCommandList = new ArrayList<String>();
		subCommandList.add("cd "+path);
		subCommandList.add(command);
		LoggerUtils.info("执行git命令:"+command+",本地仓库:"+path);
		
		String gitCommand = new ParamsService().findParamsByName("gitExePath").getValue();
		String result = SSHUtils.exec(gitCommand, "--login -i", subCommandList, "utf-8");
		return result;
	}
	
}