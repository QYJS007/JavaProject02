package work.protec.bus365.store.bus365;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utils.AlgorithmUtils;
import utils.DateUtils;
import utils.HttpUtils;
import utils.JsonUtils;
import utils.RegexUtils;
import utils.ThreadConcurrentUtils;
import utils.model.concurrent.Job;
import work.protec.bus365.store.bus365.other.RSAUtil;

public class Bus365BP {
	
	//创建clienttoken
	public static String createClientToken(String username,String password,String userid){
		String str = username+"#"+password+"#"+System.currentTimeMillis()+"#"+userid;
		String key = "112358";
		byte[] bytes = AlgorithmUtils.encryptAES(str.getBytes(), key.getBytes());
		return AlgorithmUtils.byteToHexStr(bytes);
	}
	
	//解析clienttoken
	public static String parseClientToken(String clientToken){
		String key = "112358";
		byte[] bytes = AlgorithmUtils.decryptAES(AlgorithmUtils.hexStrToByte(clientToken), key.getBytes());
		return new String(bytes);
	}
	
	//创建订单
	public static void createOrder(final String subsite,String s_count,String s_isRobot,String s_isPay,String s_afterDate,String s_isInsurance,String s_isShuangRen,final String userId,final String scheduleId){
		final Integer count = new Integer(s_count);
		final Boolean isRobot = new Boolean(s_isRobot);
		final Boolean isPay = new Boolean(s_isPay);
		final Integer afterDate = new Integer(s_afterDate);
		final Boolean isInsurance = new Boolean(s_isInsurance);
		final Boolean isShuangRen = new Boolean(s_isShuangRen);
		
		if(count==1){
			Bus365Utils.createOrder(subsite,false,isRobot,isPay,afterDate,isInsurance,isShuangRen,userId,scheduleId);
		}else{
			//开3个线程 买count张票,线程启动间隔500毫秒
			List<String> taskParamList = new ArrayList<String>();
			for(int i=0;i<count;i++){
				taskParamList.add(i+"");
			}
			
			List<String> ordernoList = ThreadConcurrentUtils.common(taskParamList, new Job<String,String>(){
				public String doJob(String param) {
					String orderno = Bus365Utils.createOrder(subsite,false,isRobot,isPay,afterDate,isInsurance,isShuangRen,userId,scheduleId).get("orderno");
					return orderno;
				}
			}, 3, 500).getValueList();
			
			for(String orderno : ordernoList){
				System.out.println("下订单成功:"+orderno);
			}
		}
	}
	
	/**
	 * 获取DML语句的表名
	 * @param sql
	 * @return
	 */
	private static String getDMLSqlTableName(String sql){
		sql = sql.toLowerCase();
		String tableName = RegexUtils.getSubstrByRegexReturnList(sql, "(insert +into|delete +from|delete|update|create table if not exists)[ `]+(.+?)\\b").get(0);
		return tableName;
	}
	
	/**
	 * 获取DML语句的主键名称和值
	 * @param sql
	 * @return
	 */
	private static String[] getDMLPrimaryKey(String sql,String wordPath){
		sql = sql.toLowerCase().trim();
		String tableName = getDMLSqlTableName(sql);
		Map<String,Object> wordMap = WordCodeUtils.getWordMap(wordPath,tableName,false);
		String primaryKey = wordMap.get("primaryKey").toString();
		String[] arr = new String[2];
		arr[0] = primaryKey;
		
		if(sql.startsWith("update") || sql.startsWith("delete")){
			List<String> list = RegexUtils.getSubstrByRegexReturnList(sql, primaryKey+"`? *= *(.+?)(?:[ ,;()]|$)");//后面紧跟 空格,符号或行结尾
			if(list.size()>0){
				arr[1] = list.get(list.size()-1);
			}
		}else if(sql.startsWith("insert")){
			arr[1] = getInsertSqlColumnMap(sql,wordPath).get(primaryKey);
		}
		return arr;
	}
	
	/**
	 * 获取insert语句列与值的map
	 * @param sql
	 * @return
	 */
	private static Map<String,String> getInsertSqlColumnMap(String sql,String wordPath){
		sql = sql.toLowerCase();
		String columnStr = sql.substring(0,sql.indexOf("values"));
		String valueStr = sql.substring(sql.indexOf("values"));
		
		//获取列名称
		List<String> columnList = null;
		if(columnStr.contains("(")){
			//指定列的插入语句
			int start1 = columnStr.indexOf("(")+1;
			int end1 = columnStr.lastIndexOf(")");
			columnStr = columnStr.substring(start1,end1);
			columnList = Arrays.asList(columnStr.split(","));
		}else{
			//默认插入全部列的插入语句
			String tableName = getDMLSqlTableName(sql);
			Map<String,Object> wordMap = WordCodeUtils.getWordMap(wordPath,tableName,false);
			@SuppressWarnings("unchecked")
			Map<String,Map<String,String>> columnMapMap = (Map<String,Map<String,String>>)wordMap.get("columnMapMap");
			columnList = new ArrayList<String>(columnMapMap.keySet());
		}
		//获取值
		int start2 = valueStr.indexOf("(")+1;
		int end2 = valueStr.lastIndexOf(")");
		valueStr = valueStr.substring(start2,end2);
		List<String> valueList = Arrays.asList(valueStr.split(","));
		if(columnList.size()!=valueList.size()){
			throw new RuntimeException("列的个数和值的个数不相等!"+columnList.size()+":"+valueList.size());
		}
		//拼装map
		Map<String,String> map = new LinkedHashMap<String,String>();
		for(int i=0;i<columnList.size();i++){
			String columnName = columnList.get(i);
			if(columnName.startsWith("`")){
				columnName = columnName.substring(1,columnName.length()-1);
			}
			map.put(columnName, valueList.get(i));
		}
		return map;
	}
	
	/**
	 * 描述:获得班次id列表
	 * @author: 李凯昊
	 * @date:2017年7月20日 上午10:03:04
	 * @param domain 域名
	 * @param start 开始时间, 距今天几天
	 * @param end 结束时间, 距今天几天
	 * @return
	 */
	public static List<String> getScheduleIdList(String domain,int start,int end){
		List<String> list = new ArrayList<String>();
		for(int i=start;i<=end;i++){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			Date date = c.getTime();
			String dateStr = DateUtils.dateToStringYMd(date);
			
			String url = null;
			if(domain.contains("nmd") || domain.contains("wwwd")){
				url = "http://nmt.bus365.cn/schedulesearch0?departid=841120000000017&departname=%E6%9E%A3%E9%98%B3%E7%AB%99_362&destination=%E5%94%90%E6%B2%B3&departdate="+dateStr+"&netname=nmt.bus365.cn&netaddress=192.168.3.7&departtype=2&destinationid=&destinationtype=2&showType=2";
			}else if(domain.contains("nmt") || domain.contains("wwwt")){
				url = "http://nmt.bus365.cn/schedulesearch0?departid=841120000000017&departname=%E6%9E%A3%E9%98%B3%E7%AB%99_362&destination=%E5%94%90%E6%B2%B3&departdate="+dateStr+"&netname=nmt.bus365.cn&netaddress=192.168.3.7&departtype=2&destinationid=&destinationtype=2&showType=2";
			}else if(domain.contains("cra") || domain.contains("mraw")){
				url = "http://cra.bus365.cn/schedulesearch0?departid=801120000001084&departname=%E6%9E%A3%E9%98%B3%E7%AB%99_362&destination=%E5%94%90%E6%B2%B3&departdate="+dateStr+"&netname=cra.bus365.cn&netaddress=192.168.3.7&departtype=2&destinationid=&destinationtype=2&showType=2";
			}else{
				throw new RuntimeException("没有指定domain对应的获取班次id的url");
			}
			
			String result = HttpUtils.sendHttpGet(url);
			List<String> subList = RegexUtils.getSubstrByRegexReturnList(result, "onclick=\"booking\\('.*?','(.*?)'.*?\\)\"");
			list.addAll(subList);
		}
		return list;
	}
	
	/**
	 * 描述:获得班次id列表
	 * @author: 李凯昊
	 * @date:2017年7月20日 上午10:03:04
	 * @param domain 域名
	 * @param start 开始时间, 距今天几天
	 * @param end 结束时间, 距今天几天
	 * @return
	 */
	public static void printScheduleIdList(String domain,int start,int end){
		List<String> list = getScheduleIdList(domain, start, end);
		for(String id : list){
			System.out.println(id);
		}
	}
	
	
	/**
	 * 添加作者和判断注解
	 * @param str
	 * @return
	 */
	public static String addAuthorAndAnn(String str,String wordPath){
		String nowDayString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		StringBuilder builder = new StringBuilder();
		builder.append("##---Create By likaihao  "+nowDayString+"  begin--------------------------------------------\r\n");
		
		String[] arr = str.split("[\r\n]+");
		for(String sql : arr){
			String tableName = getDMLSqlTableName(sql);//表名
			String[] primaryKeyArr = getDMLPrimaryKey(sql,wordPath);//主键名称和值
			String sql2 = sql.toLowerCase().trim();
			
			if(sql2.startsWith("insert")){
				builder.append("@repeat{select count(*) from `"+tableName+"` where "+primaryKeyArr[0]+" = "+primaryKeyArr[1]+"}\r\n");
			}else if(sql2.startsWith("update") || sql2.startsWith("delete")){
				builder.append("@repeat{select 1-count(*) from `"+tableName+"` where "+primaryKeyArr[0]+" = "+primaryKeyArr[1]+"}\r\n");
			}
			//结尾添加分号
			if(!sql.trim().endsWith(";")){
				sql = sql.trim()+";";
			}
			builder.append(sql+"\r\n");
		}
		builder.append("##---Create By likaihao  "+nowDayString+"  end----------------------------------------------");
		return builder.toString();
	}
	
	/**
	 * 登录后台管理系统
	 * @author likaihao
	 * @date 2016年6月28日 下午2:58:36
	 * @param subSite
	 * @param operatorCode
	 * @param password
	 * @return
	 */
	private static boolean mngr_login(String subSite,String operatorCode,String password,HttpUtils httpUtils){
		try {
			//登录
			String url = "http://192.168.3.39:6081/";
			String result = HttpUtils.sendHttpGet(url);
			String module = RegexUtils.getSubstrByRegex(result, "id=\"module\" value=\"(.*?)\"");
			String empoent = RegexUtils.getSubstrByRegex(result, "id=\"empoent\" value=\"(.*?)\"");
			
			url = subSite + "/OperatorLoginAction/operatorLogin";
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("operatorcode", operatorCode);
			//将password加密
			password = new StringBuilder(password).reverse().toString();
			password = AlgorithmUtils.byteToHexStr(RSAUtil.encrypt(RSAUtil.getKeyPair().getPublic(), password.getBytes()));
			paramMap.put("password", password);
			paramMap.put("module", module);
			paramMap.put("empoent", empoent);
			String returnStr = httpUtils.sendPost(url, paramMap, null);
			
			String currUser = RegexUtils.getSubstrByRegex(returnStr, "当前登录用户:(.*?)<");
			if(currUser!=null){
				System.out.println("登录成功,"+subSite+",当前登录用户:"+currUser);
				return true;
			}
			String errorMsg = RegexUtils.getSubstrByRegex(returnStr, "alert\\('(.*?)'\\);");
			if(errorMsg!=null){
				System.out.println("登录失败,"+subSite+","+errorMsg);
			}else{
				System.out.println("重新登录中,"+subSite);
				mngr_login(subSite,operatorCode,password,httpUtils);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		throw new RuntimeException();
	}
	
	/**
	 * 解析json返回值是否为成功
	 * @param returnStr
	 * @return
	 */
	public static Boolean parseReturnJsonStrSuccess(String returnStr){
		boolean b = false;
		if(returnStr.contains("\"variables\"")){
			//存在错误
			System.out.println(returnStr);
		}else if(returnStr.startsWith("{")){
			//json
			Map<String,Object> returnMap = JsonUtils.parseJson2Map(returnStr, Object.class);
			if(returnMap.containsKey("success")){
				//success-message
				System.out.println(returnMap.get("message"));
				b = returnMap.get("success").toString().equals("true");
			}else{
				System.out.println(returnStr);
			}
		}else{
			//html
			System.out.println(returnStr);
		}
		
		if(!b){
			throw new RuntimeException();
		}
		return b;
	}
	
	//更新支付方式
	public static void updateGetwayIsshow(String subSite,HttpUtils httpUtils){
		//for(int i=152;i<=155;i++){
			String url = subSite + "/getwaysettlement/isShow?ismock=false";
			Map<String,String> paramMap = new HashMap<String,String>();
			//paramMap.put("id",i+"");
			paramMap.put("id","143");
			paramMap.put("isshow","1");//0表示开启
			String returnStr = httpUtils.sendPost(url, paramMap, null);
			parseReturnJsonStrSuccess(returnStr);
		//}
	}
	
	
	/**
	 * 修改参数值
	 * @author likaihao
	 * @date 2016年6月28日 下午3:50:54
	 * @param subSite
	 */
	public static void updateParameter(String subSite,HttpUtils httpUtils){
		String url = subSite + "/PlatParameterConfig/avtionInfos";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("fun","4");
		paramMap.put("platinfo.id","048");
		paramMap.put("platinfo.code","048");
		paramMap.put("platinfo.isactive","1");
		paramMap.put("platinfo.paramvalue","{\"gatewayid\":\"64,65,66,76\",\"color\":\"0xfe1d2a\",\"desc\":\"随机立减\"}");
		paramMap.put("platinfo.remark","活动的支付方式信息");
		
		String result = HttpUtils.parseHttpByteToString(httpUtils.sendReturnByteArr("get", url, paramMap, null, true), "utf-8");
		System.out.println(result);
	}
	
	public static void main(String[] args) throws Exception {
		Map<String,String> map = new LinkedHashMap<String,String>();
		
//		map.put("http://localhost:9031", "bus365|bus365_0502");
//		map.put("http://192.168.3.39:6081", "bus365|bus365_0502");
//		map.put("http://192.168.3.62:6081", "bus365|bus365_0502");
//		map.put("http://192.168.3.7:8436", "bus365|bus365_0502");

		map.put("http://sxzmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://jlmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://nmmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://gsmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://ahmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://jxmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://hljmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://tjmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://wwwmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://hbzmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://nxmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://qhmngr.bus365.com", "likaihao|swsd_2016");
		map.put("http://bjmngr.bus365.com", "likaihao|swsd_2016");
		
		
		for(String subSite : map.keySet()){
			HttpUtils httpUtils = new HttpUtils(true);
			String[] valueArr = map.get(subSite).split("\\|");
			mngr_login(subSite,valueArr[0],valueArr[1],httpUtils);
			
			//updateParameter(subSite,httpUtils);
			updateGetwayIsshow(subSite,httpUtils);
		}
		
	}
	
	
	
}