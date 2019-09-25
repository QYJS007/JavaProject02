package work.protec.bus365.store;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.generate.RemoteProject;
import service.ParamsService;
import utils.DateUtils;
import utils.HttpUtils;
import utils.IOUtils;
import utils.ListUtils;
import utils.OfficeUtils;
import utils.RegexUtils;
import utils.TempletUtils;
import utils.ThreadConcurrentUtils;
import utils.model.concurrent.Job;
import work.codegenerate.base.DatabaseInfo;
import work.codegenerate.base.DatabaseUtils;
import work.protec.bus365.store.model.ZenDaoTask;

public class CodeCallBus365Store {
	//禅道登录(将sid存入cookie)
	private void zentao_login(HttpUtils httpUtils){
		ParamsService ps = new ParamsService();
		String url = "http://192.168.3.71/zentao/user-login.html";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("account", ps.findParamsByName("zentao_username").getValue());
		paramMap.put("password", ps.findParamsByName("zentao_password").getValue());
		String returnStr = httpUtils.sendPost(url, paramMap, null);
		if(returnStr.length()==0 || returnStr.contains("parent.location=")){
			System.out.println("登录成功:"+paramMap.get("account"));
		}else{
			throw new RuntimeException("登录失败,返回值:"+returnStr);
		}
	}
	
	/**
	 * 从禅道任务列表获取任务信息
	 * @author likaihao
	 * @date 2016年12月30日 下午8:03:50
	 * @param url
	 * @return
	 */
	private Map<Integer,ZenDaoTask> getZenDaoTaskInfoByListPage(String url){
		HttpUtils httpUtils = new HttpUtils(true);
		//登录
		new CodeCallBus365Store().zentao_login(httpUtils);
		
		//请求任务列表
		String myTaskStr = httpUtils.sendGet(url);
				
		Map<Integer,ZenDaoTask> taskMap = new LinkedHashMap<Integer,ZenDaoTask>();
		List<List<String>> taskList = RegexUtils.getSubstrAllGroupByRegexReturnList(myTaskStr, "<a href='/zentao/project-browse-\\d+.html' >(.*?)</a>[\\s\\S]*?<a href='/zentao/task-view-(.*?).html' >(.*?)</a>\\s*</td>\\s*<td>(.*?)</td>\\s*<td>(.*?)</td>\\s*<td>(.*?)</td>\\s*<td.*?>(.*?)</td>\\s*<td.*?>(.*?)</td>");
		for(List<String> task : taskList){
			
			ZenDaoTask zenDaoTask = new ZenDaoTask();
			zenDaoTask.projectName = task.get(1);
			zenDaoTask.taskNo = new Integer(task.get(2));
			zenDaoTask.taskName = task.get(3);
			zenDaoTask.taskEstimate = new Integer(task.get(4));
			zenDaoTask.taskDeadline = DateUtils.stringToDateYMd(task.get(7));
			zenDaoTask.taskState = task.get(8);
			
			taskMap.put(zenDaoTask.taskNo, zenDaoTask);
		}
		
		return taskMap;
	}
	
	/**
	 * 从禅道任务列表获取bug信息
	 * @author likaihao
	 * @date 2016年12月30日 下午8:03:50
	 * @param url
	 * @param isQueryDetail 是否查询详情(所属项目,解决时间)
	 * @return
	 */
	private Map<Integer,ZenDaoTask> getZenDaoBugInfoByListPage(String url,boolean isQueryDetail){
		final HttpUtils httpUtils = new HttpUtils(true);
		//登录
		new CodeCallBus365Store().zentao_login(httpUtils);
		
		//请求已完成bug列表
		Map<Integer,ZenDaoTask> taskMap = new LinkedHashMap<Integer,ZenDaoTask>();
		String myTaskStr = httpUtils.sendGet(url);
		
		List<List<String>> taskContentList = RegexUtils.getSubstrAllGroupByRegexReturnList(myTaskStr, "<a href='/zentao/bug-view-\\d+.html' target='_blank' >(\\d+)</a>[\\s\\S]*?<a href='/zentao/bug-view-\\1.html' >(.*?)</a>");
		for(List<String> task : taskContentList){
			ZenDaoTask zenDaoTask = new ZenDaoTask();
			zenDaoTask.taskNo = new Integer(task.get(1));
			zenDaoTask.taskName = task.get(2);
			zenDaoTask.taskState = "已完成";
			taskMap.put(zenDaoTask.taskNo, zenDaoTask);
		}
		
		if(isQueryDetail){
			//获取解决时间和所属项目
			List<Object[]> dateList = ThreadConcurrentUtils.common(new ArrayList<Integer>(taskMap.keySet()), new Job<Integer,Object[]>(){
				public Object[] doJob(Integer taskParam) {
					String detailUrl = "http://192.168.3.71/zentao/bug-view-"+taskParam+".html";
					String detailContent = httpUtils.sendGet(detailUrl);
					String dateStr = RegexUtils.getSubstrByRegex(detailContent, "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}), 由 <strong>\\S+</strong> 解决");
					Date date = DateUtils.stringToDateYMdHms(dateStr);
					String projectName = RegexUtils.getSubstrByRegex(detailContent, "<td class='rowhead w-p20'>所属项目</td>\\s+<td><a href='/zentao/project-browse-\\d+.html' >(.*?)</a>");
					return new Object[]{taskParam,date,projectName};
				}
			}, taskMap.size(), 20).getValueList();
			for(Object[] dateArr : dateList){
				taskMap.get(dateArr[0]).taskDeadline = (Date)dateArr[1];
				taskMap.get(dateArr[0]).projectName = (String)dateArr[2];
			}
		}
		
		return taskMap;
	}
	
	/**
	 * 获取最近的星期一
	 * @author likaihao
	 * @date 2017年1月7日 下午12:04:34
	 * @return
	 */
	private Date getLastMonday(){
		//筛选最近一个星期一之后的
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 2); //将日期设置到星期一
		//如果比今天大,或今天-星期一小于2天,则推迟到上一个星期天
		if(c.getTime().after(new Date()) || new Date().getTime() - c.getTimeInMillis() < 2 * 24 * 60 * 60 * 1000){
			c.add(Calendar.DATE, -7);
		}
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date monday = c.getTime();
		return monday;
	}
	
	/**
	 * 筛选一个星期内的任务,并排序
	 * @author likaihao
	 * @date 2017年1月7日 上午11:16:43
	 * @param taskList
	 */
	private void filterWeekTask(List<ZenDaoTask> taskList){
		Date monday = getLastMonday();
		
		for(int i=taskList.size()-1;i>=0;i--){
			ZenDaoTask zenDaoTask = taskList.get(i);
			if(zenDaoTask.taskDeadline.getTime() < monday.getTime()){
				taskList.remove(i);
			}
		}
		
		//排序
		ListUtils.sort(taskList, "taskDeadline", true);
	}
	
	//打印升级补丁中 任务信息 numStr:4460,4461,4462,4472,4473,4474
	public void zentao_printTaskInfo(String numStr){
		HttpUtils httpUtils = new HttpUtils(true);
		//登录
		zentao_login(httpUtils);
		//获取我的历史任务编号
		String url = "http://192.168.3.71/zentao/my-task-finishedBy.html";
		String returnStr = httpUtils.sendGet(url);
		List<String> allTaskNumList = Arrays.asList(numStr.split("\\D"));
		
		//判断 要升级的任务编号 是否是我的
		System.out.println("任务号\t完成人\t任务说明");
		
		//打印要提交的任务的描述
		for(String taskNum : allTaskNumList){
			url = "http://192.168.3.71/zentao/task-view-"+taskNum+".html";
			returnStr = httpUtils.sendGet(url);
			if(returnStr.contains("您无权访问该项目")){
				System.out.println(taskNum+"\t"+"没有权限");
			}else{
				String desc = RegexUtils.getSubstrByRegex(returnStr, "<div id='main'[\\s\\S]*?>.+? #.+? (.+?)</div>");
				String auth = RegexUtils.getSubstrByRegex(returnStr, "<li value='2'>[\\s\\S]*?<strong>(.*?)</strong> 完成");
				System.out.println(taskNum+"\t"+auth+"\t"+desc);
			}
		}
	}
	
	/**
	 * 禅道-添加任务
	 * @author likaihao
	 * @date 2016年12月17日 下午4:53:23
	 * @param url 添加任务的地址
	 * @param name 任务名称
	 * @param desc 任务描述
	 * @param estimate 最初预计小时
	 * @param estStarted 预计开始日期
	 * @param deadline 截止日期
	 */
	public String zentao_addTask(String url, String name, String desc, String estimate, String estStarted, String deadline){
		HttpUtils httpUtils = new HttpUtils(true);
		//登录
		zentao_login(httpUtils);
		
		//填充请求体
		String templetStr = new ParamsService().findParamsByName("zentaoAddTaskTemplet").getValue();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("assignedTo", new ParamsService().findParamsByName("zentao_username").getValue()); //指派给
		paramMap.put("name", name); //任务名称
		paramMap.put("desc", desc); //任务描述
		paramMap.put("pri", "3"); 	//优先级
		paramMap.put("estimate", estimate); 	//最初预计小时
		paramMap.put("estStarted", estStarted); //预计开始日期
		paramMap.put("deadline", deadline); 	//截止日期
		String bodyStr = TempletUtils.templetFillMultiLine(templetStr, paramMap);
		
		//发送请求添加任务
		Map<String,String> httpParamMap = new HashMap<String,String>();
		httpParamMap.put("", bodyStr);
		Map<String,String> headerMap = new HashMap<String,String>();
		headerMap.put("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryWTfPj14iPlGgHUbZ");
		String returnStr = httpUtils.sendPost(url, httpParamMap, headerMap);
		return returnStr;
	}
	
	/**
	 * 禅道-完成今天之前的任务
	 * @author likaihao
	 * @date 2016年12月30日 上午11:11:51
	 * @param assignedTo 指派人
	 * @param finishedDate 完成日期
	 * @return
	 */
	public void zentao_finishBeforeTask(String assignedTo,String finishedDate){
		HttpUtils httpUtils = new HttpUtils(true);
		//登录
		zentao_login(httpUtils);
		
		//任务列表
		String url = "http://192.168.3.71/zentao/my-task.html";
		//获取任务信息
		Map<Integer,ZenDaoTask> noFinishTaskMap = getZenDaoTaskInfoByListPage(url);
		
		//完成任务
		for(Integer taskno : noFinishTaskMap.keySet()){
			ZenDaoTask zenDaoTask = noFinishTaskMap.get(taskno);
			//未开始且是昨天的
			if(zenDaoTask.taskState.equals("未开始")){
				String nowDateStr = DateUtils.dateToStringYMd(new Date()); 
				if(zenDaoTask.taskDeadline.before(DateUtils.stringToDateYMd(nowDateStr))){
					url = "http://192.168.3.71/zentao/task-finish-"+taskno+".html?onlybody=yes";
					
					Map<String,String> httpParamMap = new HashMap<String,String>();
					httpParamMap.put("consumed", noFinishTaskMap.get(taskno).taskEstimate+"");
					httpParamMap.put("assignedTo", assignedTo);
					httpParamMap.put("finishedDate", finishedDate);
					httpParamMap.put("comment", "");
					
					httpUtils.sendPost(url, httpParamMap, null);
					System.out.println(noFinishTaskMap.get(taskno));
				}
			}
			
		}
	}
	
	/**
	 * 禅道-查询一周内已完成的任务和bug(周报)
	 * @author likaihao
	 * @date 2016年12月30日 下午8:20:36
	 */
	public void zentao_queryFinishTaskAndBug(){
		//已完成任务
		String url = "http://192.168.3.71/zentao/my-task-finishedBy.html";
		Map<Integer,ZenDaoTask> taskMap = getZenDaoTaskInfoByListPage(url);
		List<ZenDaoTask> taskList = new ArrayList<ZenDaoTask>(taskMap.values());
		//筛选并排序
		filterWeekTask(taskList);
		
		
		//已解决bug
		String bugUrl = "http://192.168.3.71/zentao/my-bug-resolvedBy.html";
		Map<Integer,ZenDaoTask> bugMap = getZenDaoBugInfoByListPage(bugUrl, true);
		List<ZenDaoTask> bugList = new ArrayList<ZenDaoTask>(bugMap.values());
		//筛选并排序
		filterWeekTask(bugList);
		
		
		//打印任务
		System.out.println("\r\n上周完成任务:");
		String name = new ParamsService().findParamsByName("zentao_chineseName").getValue();
		for(ZenDaoTask task : taskList){
			String dateStr = DateUtils.dateToStringYMd(task.taskDeadline);
			System.out.println(task.projectName+"\ttask_"+task.taskNo+"_"+task.taskName + "\t" + dateStr + "\t" + dateStr + "\t"+name+"\t"+name+"\t已完成");
		}
		for(ZenDaoTask task : bugList){
			String dateStr = DateUtils.dateToStringYMd(task.taskDeadline);
			System.out.println(task.projectName+"\tbug_"+task.taskNo+"_"+task.taskName + "\t" + dateStr + "\t" + dateStr + "\t"+name+"\t"+name+"\t已完成");
		}
		System.out.println();
		
		//打印日期
		Date monday = getLastMonday();
		Calendar c = Calendar.getInstance();
		c.setTime(monday);
		c.add(Calendar.DATE, 4);
		Date friday = c.getTime();
		
		c.add(Calendar.DATE, 3);
		Date nextMonday = c.getTime();
		c.add(Calendar.DATE, 4);
		Date nextFriday = c.getTime();
		System.out.println("上周日期(不含周六):"+DateUtils.dateToString(monday,"yyyy.MM.dd")+"-"+DateUtils.dateToString(friday,"yyyy.MM.dd"));
		System.out.println("下周日期(不含周六):"+DateUtils.dateToString(nextMonday,"yyyy.MM.dd")+"-"+DateUtils.dateToString(nextFriday,"yyyy.MM.dd"));
	}
	
	/**
	 * 从补丁管理系统数据库获得远程项目信息
	 * @author likaihao
	 * @date 2017年1月12日 上午11:12:29
	 * @param okProjectPortMap
	 * @param startId
	 */
	public void getProjectInfoByDB(Map<String,Integer> okProjectPortMap, int startId){
		//从数据库获得数据
		DatabaseInfo dbInfo = new DatabaseInfo("mysql","192.168.3.60","3312","patchm","root","bus365_0502");
		List<RemoteProject> projectList = new ArrayList<RemoteProject>();
		for(String okProjectName : okProjectPortMap.keySet()){
			String sql = "SELECT s.name,s.ip,s.contexttype,s.username,s.password,p.appid,p.path FROM remoteproject p,serverclient s where p.serverid=s.id and appid='"+okProjectName+"'";
			List<Map<String, Object>> dbList = new DatabaseUtils(dbInfo).queryReturnMap(sql);
			
			for(Map<String,Object> projectMap : dbList){
				try {
					RemoteProject project = new RemoteProject();
					//设置ip
					project.setIp(projectMap.get("ip").toString());
					//设置用户名,密码
					project.setUsername(projectMap.get("username").toString());
					project.setPassword(projectMap.get("password").toString());
					//设置路径
					project.setPath(projectMap.get("path").toString());
					//设置端口
					project.setPort(okProjectPortMap.get(okProjectName));
					//获取项目名称
					String projectName = projectMap.get("appid").toString();
					//设置本地项目
					project.setLocalProject(projectName);
					if(project.getPath().contains("openapi")){
						projectName = "openapi";
					}
					
					//设置名称
					// * 获取环境类型
					String contentType = projectMap.get("contexttype").toString();
					String contentType1 = null;
					String contentType2 = "";
					String province = "";
					if(contentType.contains("online")){
						contentType1 = "正式环境";
						if(projectName.equalsIgnoreCase("bus365")){
							if(contentType.contains("master")){
								contentType2 = "_master";
							}else{
								contentType2 = "_standby";
							}
						}
						
						// * 获取省市(汉字)
						if(projectName.equalsIgnoreCase("bus365") || projectName.equalsIgnoreCase("bus365mngr") || projectName.equalsIgnoreCase("bus365task") || projectName.equalsIgnoreCase("openapi") || projectName.equalsIgnoreCase("PushServices")){
							province = "_"+RegexUtils.getSubstrByRegex(projectMap.get("name").toString(), "[\u4e00-\u9fa5]+");
						}
					}else if(contentType.contains("simulate")){
						contentType1 = "模拟环境";
					}else if(contentType.contains("test")){
						contentType1 = "线上模拟环境";
					}else if(contentType.contains("dev")){
						contentType1 = "开发环境";
					}else{
						contentType1 = "!!!!!!!!!!!!!!!!!!!!!!!未知的contentType:"+contentType;
					}
					// * 获取ip最后一位
					String[] ipArr = project.getIp().split("\\.");
					// * 如果是openAPI,则端口为9032
					if(projectName.equalsIgnoreCase("openAPI")){
						project.setPort(9032);
					}
					project.setName(contentType1+province+"_"+projectName+contentType2+"_"+ipArr[ipArr.length-1]);
					
					projectList.add(project);
				} catch (Exception e) {
					System.out.println(projectMap);
					e.printStackTrace();
					return;
				}
			}
		}
		
		//按名称排序
		ListUtils.sort(projectList, "name", true);
		
		//拼装sql
		String templet = "INSERT INTO `t_remote_project` (`id`, `type`, `name`, `ip`, `username`, `password`, `path`, `port`, `commandGroupId`, `command`, `localProject`, `istest`) VALUES ('${startId+i}', '网站-正式环境', '${p.name}', '${p.ip}', '${p.username}', '${p.password}', '${p.path}', '${p.port}', '1', NULL, '${p.localProject}', '0');";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		for(int i=0;i<projectList.size();i++){
			paramMap.clear();
			paramMap.put("startId", startId);
			paramMap.put("i", i);
			paramMap.put("p", projectList.get(i));
			String result = TempletUtils.templetFillMultiLine(templet, paramMap).toString();
			System.out.println(result);
		}
	}
	
	//暂时没用到,被从数据库获取项目信息替代
	public void getPorjectInfoByWord(){
		//抓取的端口和项目名称的对应关系
		Map<String,String> portNameMap = new HashMap<String,String>();
		portNameMap.put("9031", "bus365");
		
		//读取文件内容
		File file = new File(IOUtils.getHomeDirectoryPath()+"/网站生产环境文档.xls");
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Map<String, ArrayList<ArrayList<String>>> contentMap = OfficeUtils.readExcel(file, dateFormat);
		
		//遍历工作表
		Map<String,List<RemoteProject>> projectMap = new LinkedHashMap<String,List<RemoteProject>>();
		for(String sheetName : contentMap.keySet()){
			ArrayList<ArrayList<String>> rowList = contentMap.get(sheetName);
			//遍历行
			List<RemoteProject> projectList = new ArrayList<RemoteProject>();
			for(ArrayList<String> columnList : rowList){
				//判断是否是master或standby开头,或是指定项目名
				if(columnList.size()>4 && portNameMap.keySet().contains(columnList.get(4).trim())){
					try {
						RemoteProject project = new RemoteProject();
						//设置ip
						project.setIp(columnList.get(1).trim());
						//设置用户名,密码
						String[] arr = columnList.get(2).trim().split("/");
						if(arr.length==2){
							project.setUsername(arr[0]);
							project.setPassword(arr[1]);
						}else{
							project.setUsername("app");
							project.setPassword(arr[0]);
						}
						String projectName = portNameMap.get(columnList.get(4).trim());
						//设置路径
						project.setPath(columnList.get(3).trim()+"/"+projectName);
						//设置端口
						project.setPort(new Integer(columnList.get(4).trim()));
						//设置名称
						String contentType = columnList.get(0).trim();
						String[] ipArr = project.getIp().split("\\.");
						project.setName("正式环境_"+sheetName+"_"+projectName+"_"+contentType+"_"+ipArr[ipArr.length-1]);
						System.out.println(project.getName());
						projectList.add(project);
					} catch (Exception e) {
						System.out.println(columnList);
						e.printStackTrace();
						return;
					}
				}
			}
			projectMap.put(sheetName, projectList);
		}
		
		for(String sheetName : projectMap.keySet()){
			List<RemoteProject> projectList= projectMap.get(sheetName);
			System.out.println(sheetName + "\t-->\t"+projectList.size());
		}
	}
	
}
