package utils;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.exceptions.UnexpectedException;
import play.libs.IO;
import play.mvc.Http;
import play.mvc.Scope.Params;
import play.vfs.VirtualFile;
import utils.CommonUtil.TableIndex;

/** 
 * 常用工具，提供常用的功能 
 * 
 */
public class CommonUtil {

	public enum TableIndex {
		menu,//菜单表
		businessconfig,
		vehicletype_brand,
		digitalDictionary,
		digitalDictionaryDetail,
		orderinfo,// 包车订单
		addservice,//附加服务
		content_management,//内容管理
		activecity,//开通城市
		dispatcher,//人员调度
		vehicle_type,//车型表
		vehicletypenum,//车型号
		contentmanagementnum,//内容号
		brand,//车辆品牌表
		orgrecord,//机构档案表
		sendorder,//派单表
		thirdorginfo,//三方机构表
		vehiclescheduling,//车辆运力信息表
		vehiclewarning,//车辆动态预警
		vehicle,//车辆表
		drivers,//司机表
		;
	}


	public enum RequestType {
		JSON, 
		HTML, 
		XML; 
	}

	/**
	 * 获得某表的ID
	 * 根据orgcode和tableIndex取得ID，独立事务，每调用一次自动加1
	 * @param orgcode 机构编号
	 * @param tableIndex 表的索引号
	 * @return 返回一个ID
	 * @throws Exception 
	 */
	public static Long GetPKIDAndCompleteZero(String orgcode, TableIndex tableIndex, String dbname,Integer completeLength) {

		if(CommonUtil.isEmptyString(orgcode)){
			orgcode = "100001";
		}
		if(completeLength ==null){
			completeLength = 0; 
		}
		Long PKID = 0L;
//		String lastDB = JPA.getCurrentConfigName();
//		JPA.setCurrentConfigName(dbname);
		EntityManager em =JPA.newEntityManager();
		try {
			if (!em.getTransaction().isActive()){
				em.getTransaction().begin();
			}
			Query query = em.createNativeQuery("update idbuild set currentid = currentid + 1 where tablename = :tablename and orgcode = :orgcode ");
			query.setParameter("tablename", tableIndex.name());
			query.setParameter("orgcode", orgcode);
			int result = query.executeUpdate(); // 影响的记录数
			if (result > 0) {
				Query querySelect = em.createNativeQuery("select currentid from idbuild where tablename = :tablename and orgcode = :orgcode ");
				querySelect.setParameter("tablename", tableIndex.name());
				querySelect.setParameter("orgcode", orgcode);
				String rs = querySelect.getSingleResult().toString();
				int len=completeLength-rs.length();

				StringBuilder zeros=new StringBuilder();
				if(len>0){
					for (int i = 0; i < len; i++) {
						zeros.append("0");
					}
				}
				em.getTransaction().commit();
				
				
				PKID = Long.parseLong(orgcode +zeros+ rs);
				return PKID;
				
			} else {
				Query querytable = em.createNativeQuery("select count(*) from information_schema.tables where table_schema = :dbname and table_name = :tablename ");
				querytable.setParameter("dbname", dbname);
				querytable.setParameter("tablename", tableIndex.name());
				String rs = querytable.getSingleResult().toString();
				if(rs.equals("0")){
					throw new UnexpectedException("数据库不存在改表，tablename="+tableIndex.name());
				}
				if (!em.getTransaction().isActive()){
					em.getTransaction().begin();
				}
				Query queryInsert = em.createNativeQuery("insert into idbuild ( tablename, orgcode, currentid ) values (:tablename, :orgcode, 1) ");
				queryInsert.setParameter("tablename", tableIndex.name());
				queryInsert.setParameter("orgcode", orgcode);
				queryInsert.executeUpdate();
				em.getTransaction().commit();
				int len=completeLength-1;
				StringBuilder zeros=new StringBuilder();
				if(len>0){
					for (int i = 0; i < len; i++) {
						zeros.append("0");
					}
				}
				
				PKID = Long.parseLong(orgcode +zeros+ "1");
				return PKID;
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()){
				em.getTransaction().rollback();
			}
			throw new UnexpectedException("获取ID失败,orgcode:"+orgcode+",dbname："+dbname,e);
		}finally{
//			JPA.setCurrentConfigName(lastDB);
		}
	}	
	/**
	 * 获得请求类型
	 * 根据Request对象获得请求类型
	 * @param Http.Request 请求对象
	 * @return 返回类型
	 */
	public static RequestType GetReqeustType(Http.Request r) {
		//		功能测试时，没有accept 所以此处用作测试时打开
		if(r.headers.get("accept") == null){
			return RequestType.JSON;
		}

		String accept= r.headers.get("accept").toString();
		String s[]=accept.split(",");
		if (s[0].equalsIgnoreCase("[text/html"))
			return RequestType.HTML;
		else if (s[0].equalsIgnoreCase("[application/json"))
			return RequestType.JSON;
		else if (s[0].equalsIgnoreCase("[text/xml"))
			return RequestType.XML;
		else
			return RequestType.HTML;
	}


	/**
	 * 判断是否为空字符
	 * @param val
	 * @return
	 */
	public static boolean isNotEmptyString(String val){
		if(val!=null&&!"".equals(val.trim())){  
			return true;
		}
		return false;
	}

	public static boolean isEmptyString(String val){
		if(val==null || "".equals(val.trim())){
			return true;
		}
		return false;
	}

	public static boolean isEmptyMap(Map map){
		if(map==null || map.isEmpty()){
			return true;
		}
		return false;
	}

	public static boolean isNotEmptyMap(Map map){
		if(map==null || map.isEmpty()){
			return false;
		}
		return true;
	}

	public static boolean isEmptyList(List list){
		if(list==null || list.isEmpty()){
			return true;
		}
		return false;
	}
	public static boolean isNotEmptyList(List list){
		if(list!=null && !list.isEmpty()){
			return true;
		}
		return false;
	}
	public static boolean isEmptySet(Set set){
		if(set==null || set.isEmpty()){
			return true;
		}
		return false;
	}
	public static String nullToString(String val){
		if(val==null || "".equals(val.trim())){
			return "";
		}
		return val;
	}
	/**
	 * 获得UUID
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	
	public static String[] getAllPropertiesOnObject(Class<?> c) throws Exception{
		try {
			if (c != null) {//if (object!=null )  ----begin
				// 获取实体类的所有属性，返回Field数组
				Field[] fields = c.getFields();
				String[] proarr = new String[fields.length];
				for (int i = 0; i < fields.length; i++) {
					proarr[i] = fields[i].getName().toString();
				}
				return proarr;
			}
			return null;
		} catch (Exception e) {
			Logger.error(e, null, "获取实体类的所有属性", false, "发生异常", null, null, c.getName());
			throw e;
		}
	}

	public static Date parseStringToDate(String strDate, String formateStr){
		SimpleDateFormat sdf=new SimpleDateFormat(formateStr);
		Date date=null;
		try {
			date=new Date(sdf.parse(strDate).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date parseStringToDate(String strDate){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		try {
			date=new Date(sdf.parse(strDate).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Timestamp parseStringToTimestamp(String strTimestamp){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Timestamp ts=null;
		try {
			ts=new Timestamp(sdf.parse(strTimestamp).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ts;
	}

	public static Time parseStringToTime(String strTime, String formateStr){
		SimpleDateFormat sdf=new SimpleDateFormat(formateStr);
		Time time=null;
		try {
			time=new Time(sdf.parse(strTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}


	public static Time parseStringToTime(String strTime){
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");
		Time time=null;
		try {
			time=new Time(sdf.parse(strTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String appendParam(String returns, String paramId, String paramValue) {
		if (!"".equals(returns)) {
			if (!"".equals(paramValue)) {

				returns += "&" + paramId + "=" + paramValue;
			}

		} else {

			if (!"".equals(paramValue)) {
				returns = paramId + "=" + paramValue;
			}
		}

		return returns;
	}
	/**
	 * 安装时间生成ID
	 * @return
	 */
	public static String createID() {
		Calendar calendar=Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		int date=calendar.get(Calendar.DATE);
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		int second=calendar.get(Calendar.SECOND);
		StringBuilder id=new StringBuilder();
		id.append((year+"").substring(2));
		if (month<10) {
			id.append("0"+month);
		}else {
			id.append(month);
		}
		if (date<10) {
			id.append("0"+date);
		}else {
			id.append(date);
		}
		if (hour<10) {
			id.append("0"+hour);
		}else {
			id.append(hour);
		}
		if (minute<10) {
			id.append("0"+minute);
		}else {
			id.append(minute);
		}
		if (second<10) {
			id.append("0"+second);
		}else {
			id.append(second);
		}
		return id.toString();
	}
	public static String getNetTicketno(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		return format.format(date);
	}

	/**
	 * Looks for extra db configs in config
	 * @return list of all extra db config names found
	 * @author ldl
	 */
	public static Set<String> detectedExtraDBConfigs() {
		Properties props = null;
		VirtualFile appRoot = VirtualFile.open(Play.applicationPath);
		VirtualFile conf = appRoot.child("conf/application.conf");
		try {
			props = IO.readUtf8Properties(conf.inputstream());
		} catch (RuntimeException e) {
			if (e.getCause() instanceof IOException) {
				Logger.fatal("Cannot read application.conf");
				System.exit(-1);
			}
		}

		Set<String> names = new LinkedHashSet<String>(0); //preserve order
		Pattern pattern = Pattern.compile("^db\\_([^\\.]+)(?:$|\\..*)");
		for( String propName : props.stringPropertyNames()) {
			Matcher m = pattern.matcher(propName);
			if (m.find()) {
				String configName = m.group(1);
				if (!names.contains(configName)) {
					names.add(configName);
					System.out.println("dbconfg:"+configName);
				}
			}
		}
		return names;
	}
	public static String stringFormat(String msg,String... args){
		for (int i = 0; i < args.length; i++) {
			msg = msg.replaceFirst("%\\d", args[i]);
		}
		return msg;
	}
	public static boolean isSelfDomain(String netname,String netaddress,String domain){
		if(netaddress==null || netname==null || "".equals(netaddress) || "".equals(netname) ||
				netaddress.equals(domain) || netname.equals(domain)){
			return true;
		}else {
			return false;
		}
		/*if(netaddress==null || netname==null || "".equals(netaddress) || "".equals(netname) || netname.equals(domain)){
    		return true;
    	}else {
			return false;
		}*/
	}

	/**
	 * @param source int数组字符串
	 * @return 转化完成的字符串
	 * @throws Exception 数组字符串结构错误
	 */
	public static String convertIntArrayStringForSql(String source) throws Exception{
		Pattern pt = Pattern.compile("^\\d+(_\\d+)*$");
		Matcher matcher = pt.matcher(source);
		if(!matcher.find()){
//			throw new BPException("validation.match");
		}
		return source.replace('_', ',');
	}

	public static String formatDate2String(java.util.Date source){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(source);
	}

	public static <T> List<T> json2Array(String jsonstr, Class<T[]> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		String pattern = Play.configuration.getProperty("date.format");
		DateFormat df = new SimpleDateFormat(pattern);
		mapper.setDateFormat(df);
		T[] resultArr = null;
		try {
			resultArr = (T[]) mapper.readValue(jsonstr, clazz);
			return Arrays.asList(resultArr);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	public static List<Map<String, Object>> list2Map(List list, String[] fields) {
		List<Map<String,Object>> outerlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		Object[] item = null;
		for(Object obj : list){
			map = new HashMap<String, Object>();
			item = (Object[]) obj;
			for(int i = 0; i < item.length; i++){
				if(item[i] instanceof Blob){
					byte[] it = blobToBytes((Blob)item[i]);
					map.put(fields[i], it);
				}else{
					map.put(fields[i], item[i]);
				}
			}
			outerlist.add(map);
		}
		return outerlist;
	}

	/**
	 * 将数值或字符串集合转换成字符串
	 * @return
	 */
	public static String list2Str(List idlist){
		StringBuilder sb = new StringBuilder();
		for (Object obj : idlist) {
			sb.append(obj).append(",");
		}
		if (!idlist.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	/**将字符串切割转化为 字符串集合
	 * @Date:2019年1月11日 下午1:04:25
	 * @author suqin  
	 * @param str
	 * @param regex
	 * @return
	 */
	public static List<String> str2list(String str, String regex ) {
		List<String> list = new ArrayList<String>();
		if(isNotEmptyString(str)){
			String[] split = str.split(regex);
			for (String string : split) {
				list.add(string);
			}
		}else{
			return list;
		}
		return list;
	}

	/**
	 * 判断表是否存在(oracle)
	 * @param sourcename
	 * @return
	 */
	public static boolean isTableExist(String sourcename) {
		String sql = "select count(1) from user_tables where table_name = upper('"+sourcename+"')";
		Query query = JPA.em().createNativeQuery(sql);
		int count = ((BigDecimal)query.getSingleResult()).intValue();
		if(count > 0){
			return true;
		}
		return false;
	}

	public static boolean isColumnExist(String tablename,String columnname){
		String sql = "select count(1) from user_tab_columns where table_name = upper('"+tablename+"') and column_name = upper('"+columnname+"')";
		Query query = JPA.em().createNativeQuery(sql);
		int count = ((BigDecimal)query.getSingleResult()).intValue();
		if(count > 0){
			return true;
		}
		return false;
	}

	/**
	 * 获取当前日期的字符串形式
	 * @return
	 */
	public static String getCurrentday(String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new java.util.Date());
	}

	/**
	 * 转换金额数值
	 * @param numberstr 金额数值  
	 * 单位为分，最左边为正负标志位。0：正数，1：负数；
	 * 剩下7位以分为单位，不足左补空格。
	 * 如8元：00000800；-8元：10000800
	 */
	public static BigDecimal formatNumber(String numberstr){
		BigDecimal resultval = new BigDecimal(new BigInteger(numberstr.substring(1))); 
		resultval = resultval.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN).negate();
		return resultval;
	}


	/**
	 * 把Blob类型转换为byte数组类型
	 * @param blob
	 * @return
	 */
	public static byte[] blobToBytes(Blob blob) {
		BufferedInputStream is = null;

		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;

			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}
	/***
	 * 从当前请求参数列表获取clienttype
	 * @author dongchao
	 * @return
	 */
	public static Integer getClientypeFromParams(){
		Integer clienttype = null;
		try{
			Params sh = Params.current();
			if(null != sh){
				String type = sh.allSimple().get("clienttype");
				if(isNotEmptyString(type)){
					clienttype = Integer.valueOf(type);
				}
			}
		}catch(Exception e){
			Logger.error(e, "从当前请求参数列表获取clienttype有误");
		}
		return clienttype;
	}

	/**
	 * 根据clienttype 判断是否是电脑端请求
	 * @author dongchao
	 * @date Aug 11, 2015 2:51:32 PM
	 * @return
	 */
	public static boolean requestFromComputer(){
		Integer type = CommonUtil.getClientypeFromParams();
		if(null == type){//没传的话,认为是电脑端请求
			return true;
		}
		switch(type){
		case 0://网站
			return true;
		default://其他android，ios等客户端类型
			return false;
		}
	}

	/**
	 * 返回字母区间中的所有字母
	 * @param beginAndEnd 字母区间 例如:A_F
	 * @return
	 */
	public static List<String> getLetters(String beginAndEnd){
		String[] letters = beginAndEnd.split("_");
		List<String> list = new ArrayList<String>();
		String begin = letters[0];
		String end = null;
		if(letters.length>1){
			end = letters[1];
		}else{
			end = letters[0];
		}
		int beginascii = begin.getBytes()[0];
		int endascii = end.getBytes()[0];
		for (int i = beginascii; i <= endascii; i++) {
			list.add(""+(char)i);
		}
		return list;
	}



	/**
	 * 从身份证获取出生日期
	 * @param cardNumber 已经校验合法的身份证号
	 * @return Strig YYYY-MM-DD 出生日期
	 */
	public static String getBirthDateFromCard(String cardNumber){
		String card = cardNumber.trim();
		String year;
		String month;
		String day;
		if (card.length()==18){ //处理18位身份证
			year=card.substring(6,10);
			month=card.substring(10,12);
			day=card.substring(12,14);
		}else{ //处理非18位身份证
			year=card.substring(6,8);
			month=card.substring(8,10);
			day=card.substring(10,12);
			year="19"+year;        
		}
		if (month.length()==1){
			month="0"+month; //补足两位
		}
		if (day.length()==1){
			day="0"+day; //补足两位
		}
		return year+"-"+month+"-"+day;
	}

	/** 
	 * @Title: formatHourString 
	 * @Description: (将Double小时数转换为字符串说明,如：1小时30分钟) 
	 * @author cuiyakun
	 * @date 2015年11月17日 下午3:45:07
	 * @param hour	小时数   如：1.50
	 * @return
	 * @throws 
	 */
	public static String formatHourString(Double hour){
		String hourString = "";
		if(null != hour){
			String str = hour.toString();
			String[] strArr = str.split("\\.");

			// 如果小时数为零，则不要小时了   2018-06-25 new add
			if(!"0".equals(strArr[0])){
				hourString += strArr[0] + "小时";		//小时部分
			}

			//分钟部分
			String minuteString = "0." + strArr[1];
			Double minuteDouble = Double.valueOf(minuteString) * 60;

			//如果分钟数是0的话，就不要分钟了
			if(minuteDouble.compareTo(new Double(0.0)) == 0){
				return hourString;
			}

			BigDecimal minuteBigDecimal = new BigDecimal(minuteDouble.toString()).setScale(0, BigDecimal.ROUND_UP);//向上取整
			hourString += minuteBigDecimal + "分钟";
		}
		return hourString;
	}

	/** 
	 * @Title: formatRangekmString 
	 * @Description: (将Double公里转换为字符串说明,如：3.9公里) 
	 * @author cuiyakun
	 * @date 2015年11月17日 下午3:57:28
	 * @param rangekm	公里数   如：3.89
	 * @return
	 * @throws 
	 */
	public static String formatRangekmString(Double rangekm){
		String rangekmString = "";
		if(null != rangekm){
			BigDecimal rangekmBigDecimal = new BigDecimal(rangekm.toString()).setScale(1, BigDecimal.ROUND_UP);//向上进位保留一位小数
			rangekmString += rangekmBigDecimal + "公里";
		}
		return rangekmString;
	}






	public static String getPrefix(String filename){
		return  filename.substring(filename.lastIndexOf(".")+1);
	}
	public static void main(String[] args) {
		System.out.println(getPrefix("1.png"));
	}

	/**
	 *  格式化小数为百分比，
	 * @param valu 要格式化的数
	 * @param num	精度（小数点几位）
	 * @return  
	 */
	public static String formatScale(Double value, Integer num) {
		//获取格式化对象
		NumberFormat nt = NumberFormat.getPercentInstance();

		//设置百分数精确度2即保留两位小数
		nt.setMinimumFractionDigits(num);

		//最后格式化并输出
		return nt.format(value);
	}


	public static String formatNumber(Double d) {
		if (d == null) {
			return "0";
		}
		String str = String.valueOf(d);
		String[] nums = str.split("\\.");
		if (nums.length == 2) {
			String i = nums[1];
			if (i.endsWith("0")) {
				int num = Integer.parseInt(i);
				if (num == 0) {
					return nums[0];
				} else {
					return nums[0] + "." + i;
				}
			} else {
				return nums[0] + "." + i;
			}
		}else {
			return str;
		}
	}

	/**  
	 * 描述: 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
	 * @author: cuiyakun
	 * @date:2018年9月3日 上午11:23:46
	 * @param version1
	 * @param version2
	 * @return
	 * @throws Exception 
	 */
	public static int compareVersion(String version1, String version2) {
		String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
		int diff = 0;
		while (idx < minLength
				&& (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
			++idx;
		}
		//如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}


	/**
	 * 获得某表的ID
	 * 根据orgcode和tableIndex取得ID，独立事务，每调用一次自动加1
	 * @param orgcode 机构编号
	 * @param tableIndex 表的索引号
	 * @return 返回一个ID
	 * @throws Exception 
	 */
	public static long GetPKID(String orgcode, TableIndex tableIndex, String dbname) {
		if(orgcode==null || orgcode.length()==0){
			orgcode = "100001";
		}
		long PKID = 0;
//		String lastDB = JPA.getCurrentConfigName();
//		JPA.setCurrentConfigName(dbname);
		EntityManager em =JPA.newEntityManager();
		try {
			if (!em.getTransaction().isActive()){
				em.getTransaction().begin();
			}
			Query query = em.createNativeQuery("update idbuild set currentid = currentid + 1 where tablename = :tablename and orgcode = :orgcode ");
			query.setParameter("tablename", tableIndex.name());
			query.setParameter("orgcode", orgcode);
			int result = query.executeUpdate(); // 影响的记录数
			if (result > 0) {
				Query querySelect = em.createNativeQuery("select currentid from idbuild where tablename = :tablename and orgcode = :orgcode ");
				querySelect.setParameter("tablename", tableIndex.name());
				querySelect.setParameter("orgcode", orgcode);
				String rs = querySelect.getSingleResult().toString();
				em.getTransaction().commit();
				PKID = Long.parseLong(orgcode + rs);
				return PKID;
			} else {

				if (!em.getTransaction().isActive()){
					em.getTransaction().begin();
				}
				Query queryInsert = em.createNativeQuery("insert into idbuild ( tablename, orgcode, currentid ) values (:tablename, :orgcode, 1) ");
				queryInsert.setParameter("tablename", tableIndex.name());
				queryInsert.setParameter("orgcode", orgcode);
				queryInsert.executeUpdate();
				em.getTransaction().commit();
				PKID = Long.parseLong(orgcode + "1");
				return PKID;
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()){
				em.getTransaction().rollback();
			}
			throw new UnexpectedException("获取ID失败,orgcode:"+orgcode+",dbname："+dbname,e);
		}finally{
//			JPA.setCurrentConfigName(lastDB);
		}
		
	}   
	
	/**
	   * 验证手机格式
	   * @param phone
	   * @return
	   */
	  public static boolean isPhone(String phone){
	    if(null == phone){
	      return false;
	    }
	    Pattern p = Pattern.compile(Constants.regexmap.get("phone_regex"));
	    Matcher m = p.matcher(phone);
	    if(m.matches()){
	      return true;
	    }
	    return false;
	  }

	/**
	 * 获得请求类型
	 * 根据Request对象获得请求类型
	 * @param Http.Request 请求对象
	 * @return 返回类型
	 */
	public static RequestType GetRequestType(Http.Request r) {
//		功能测试时，没有accept 所以此处用作测试时打开
		if(r.headers.get("accept") == null){
			return RequestType.JSON;
		}
		
		String accept= r.headers.get("accept").toString();
		String s[]=accept.split(",");
		if (s[0].equalsIgnoreCase("[text/html"))
			return RequestType.HTML;
		else if (s[0].equalsIgnoreCase("[application/json"))
			return RequestType.JSON;
		else if (s[0].equalsIgnoreCase("[text/xml"))
			return RequestType.XML;
		else
			return RequestType.HTML;
	}
}
