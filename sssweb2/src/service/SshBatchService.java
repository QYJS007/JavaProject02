package service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.generate.RemoteProject;
import utils.DateUtils;
import utils.RegexUtils;
import utils.SSHUtils;
import utils.StringUtils;
import utils.ThreadConcurrentUtils;
import utils.model.concurrent.Job;
import utils.model.concurrent.JobResult;
import dao.generate.RemoteProjectGenDao;

public class SshBatchService {
	/**
	 * 获得远程项目和本地项目时间的差距(毫秒值:服务器-本地)
	 * @author likaihao
	 * @date 2017年2月16日 下午5:52:23
	 * @param hostname
	 * @param username
	 * @param password
	 * @return 平均差值,平均延迟,最终结果(差值-延迟)
	 */
	public static long[] getServerLocalTimeDiff(String hostname, String username, String password){
		//执行命令
		String command = "date '+%Y-%m-%d %H:%M:%S.%N'";
		int num = 10;//执行次数,取平均
		List<String> commandList = new ArrayList<String>();
		for(int i=0;i<num;i++){
			commandList.add(command);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String result = SSHUtils.execLinuxCommandByClient(hostname, username, password, out, commandList);
		String outStr = new String(out.toByteArray());
		//解析结果
		String dateRegex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}";
		List<String> resultDateList = RegexUtils.getSubstrByRegexReturnList(result, dateRegex);
		List<String> sendDateList = RegexUtils.getSubstrByRegexReturnList(outStr, dateRegex);
		//差值 = 平均差值-平均延迟
		//平均差值 = sum(每个resultTime-每个sendTime)/个数
		//平均延迟 = 最后一个(每个resultTime - 上一个resultTime)/个数
		int averageDiff = 0;
		int averageAfter = 0;
		for(int i=0;i<resultDateList.size();i++){
			long resultDate = DateUtils.stringToDateYMdHmsS(resultDateList.get(i)).getTime();
			long sendDate = DateUtils.stringToDateYMdHmsS(sendDateList.get(i)).getTime();
			
			averageDiff += (resultDate - sendDate);
			if(i!=0){
				long lastResultDate = DateUtils.stringToDateYMdHmsS(resultDateList.get(i-1)).getTime();
				averageAfter += (resultDate - lastResultDate);
			}
		}
		averageDiff = averageDiff/num;
		averageAfter = averageAfter/(num-1);
		
		return new long[]{averageDiff, averageAfter, averageDiff - averageAfter};
	}
	
	public static void main(String[] args) {
		//查询数据库
		List<RemoteProject> projectList = new RemoteProjectGenDao().findCollectionByConditionNoPage(" and ip like '10.%'", null, null);
		//按ip分组,每个ip保留一个项目即可
		Map<String,RemoteProject> serverMap = new HashMap<String,RemoteProject>();
		for(RemoteProject project : projectList){
			if(!serverMap.containsKey(project.getIp())){
				serverMap.put(project.getIp(), project);
			}
			//if(serverMap.size()>5)break;
		}
		List<RemoteProject> serverList = new ArrayList<RemoteProject>(serverMap.values());
		//检测时间
		List<JobResult<RemoteProject,long[]>> list = ThreadConcurrentUtils.common(serverList, new Job<RemoteProject,long[]>(){
			public long[] doJob(RemoteProject server) {
				return getServerLocalTimeDiff(server.getIp(),server.getUsername(),server.getPassword());
			}
		} , 1, 0).getParamValueList();
		
		//排序,按间隔大小
		Collections.sort(list, new Comparator<JobResult<RemoteProject,long[]>>(){
			public int compare(JobResult<RemoteProject, long[]> o1,
					JobResult<RemoteProject, long[]> o2) {
				return new Long(o1.getValue()[2] - o2.getValue()[2]).intValue();
			}
		});
		
		System.out.println("以下时间单位均为毫秒");
		System.out.println("|ip|               |与我的时间间隔|    |与我的通讯时间|    |综合|          |服务器上某个项目名称|");
		for(JobResult<RemoteProject,long[]> result: list){
			String str = StringUtils.fillSpace(result.getParam().getIp(), 20, 0) + "" + StringUtils.fillSpace(result.getValue()[0]+"", 15, 0) + "" + StringUtils.fillSpace(result.getValue()[1]+"", 15, 0) + StringUtils.fillSpace(result.getValue()[2]+"", 15, 0) + ""+result.getParam().getName();
			System.out.println(str);
		}
	}
}