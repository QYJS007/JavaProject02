package work.protec.bus365.fortress;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.generate.Params;
import service.ParamsService;
import utils.HttpUtils;
import utils.LoggerUtils;
import utils.RegexUtils;
import utils.SSHUtils;
import ch.ethz.ssh2.Connection;

public class FortressService {
	
	//登录标识
	private static String sid = null;
	
	//保存堡垒机ip和authid的对应关系
	private static Map<String,String> authIdMap = new HashMap<String,String>();
	
	/**
	 * 描述:获得headerMap 
	 * @author: 李凯昊
	 * @date:2017年8月9日 下午5:12:47
	 * @return
	 */
	private static Map<String,String> getHeaderMap(){
		if(sid==null){
			Params params = new ParamsService().findParamsByName("fortress_sid");
			if(params==null){
				throw new RuntimeException("请在参数管理中设置fortress_sid");
			}
			sid = params.getValue();
		}
		Map<String,String> headerMap = new HashMap<String,String>();
		headerMap.put("Cookie", "_sid="+sid+";");
		return headerMap;
	}
	
	/**
	 * 描述:获得服务器的authid
	 * @author: 李凯昊
	 * @date:2017年8月9日 下午4:10:47
	 * @param sid
	 * @param ip
	 * @return
	 */
	private static String getAuthId(String ip){
		if(!authIdMap.containsKey(ip)){
			String url = "http://fortress.bus365.com:7190/host/list";
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("args", "{\"filter\":{\"host_group\":0,\"host_sys_type\":0,\"search\":\"\"},\"order\":null,\"limit\":{\"page_index\":0,\"per_page\":100}}");
			String result = HttpUtils.sendHttpPost(url, paramMap, getHeaderMap());
			if(result.contains("-99")){
				throw new RuntimeException("sid无效,请重新登录fortress.bus365.com");
			}
			
			List<List<String>> list = RegexUtils.getSubstrAllGroupByRegexReturnList(result, "host_ip\": \"(.*?)\".*?\"auth_id\": (.*?),");
			for(List<String> subList : list){
				authIdMap.put(subList.get(1), subList.get(2));
			}
		}
		
		return authIdMap.get(ip);
	}
	
	/**
	 * 描述:获取服务器的sessionid 
	 * @author: 李凯昊
	 * @date:2017年8月9日 下午4:10:58
	 * @param sid
	 * @return
	 */
	private static String getSessionId(String ip){
		String authid = getAuthId(ip);
		String url = "http://fortress.bus365.com:7190/host/get-session-id";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("args", "{\"auth_id\":"+authid+"}");
		
		String result = HttpUtils.sendHttpPost(url, paramMap, getHeaderMap());
		if(result.contains("-99")){
			throw new RuntimeException("sid无效,请重新登录fortress.bus365.com");
		}
		String sessionId = RegexUtils.getSubstrByRegex(result, "session_id\": \"(.*?)\"");
		LoggerUtils.info("获取到堡垒机用户,ip:"+ip+",username:"+sessionId);
		return sessionId;
	}
	
	/**
	 * 描述:获得堡垒机连接 
	 * @author: 李凯昊
	 * @date:2017年8月10日 上午11:17:57
	 * @param hostname
	 * @return
	 */
	public static Connection getSSHConnection(String hostname){
		Map<String,String> connInfo = getNewConnectionInfo(hostname);
		Connection conn = SSHUtils.getSSHConnection(connInfo.get("hostname"), connInfo.get("username"), connInfo.get("password"), new Integer(connInfo.get("port")));
		
		//将hostname填回到conn中
		try {
			Field hostnameField = conn.getClass().getDeclaredField("hostname");
			hostnameField.setAccessible(true);
			hostnameField.set(conn, hostname);
		} catch (Exception e) {
			throw new RuntimeException("创建堡垒机连接填充hostname失败",e);
		}
		return conn;
	}
	
	/**
	 * 描述: 通过连接获取一个新的连接信息
	 * @author: 李凯昊
	 * @date:2017年8月10日 下午8:26:01
	 * @param conn
	 * @return
	 */
	public static Map<String,String> getNewConnectionInfo(String hostname){
		String username = getSessionId(hostname);
		
		Map<String,String> connInfoMap = new HashMap<String,String>();
		connInfoMap.put("hostname", "fortress.bus365.com");
		connInfoMap.put("username", username);
		connInfoMap.put("port", "52189");
		connInfoMap.put("password", "****");
		return connInfoMap;
	}
	
}
