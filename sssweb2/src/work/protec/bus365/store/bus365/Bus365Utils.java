package work.protec.bus365.store.bus365;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.AlgorithmUtils;
import utils.HttpUtils;
import utils.JsonUtils;
import utils.RegexUtils;
import utils.StringUtils;
import utils.other.IDCardGeneratorUtils;
import work.protec.bus365.store.bus365.other.Constants;

public class Bus365Utils {
	
	private static Map<String,Map<String,Integer>> scheduleIdMap = new HashMap<String,Map<String,Integer>>();
	private static Map<String,String[]> tokenArrMap = new HashMap<String,String[]>();
	
	public static String returnType = null;//客户端类型
	/**
	 * 获取一个可售班次id
	 * @param subSite
	 * @param afterDate 
	 * @return
	 */
	@SuppressWarnings("all")
	private static String getScheduleId(String subSite,int afterDate){
		synchronized (subSite) {
			int start = -1;//开始的热门班次
			//排除的班次id
			String[] noArr = {""};
			List<String> noList = Arrays.asList(noArr);
			
			for(int i=start;;i++){
				Map<String,Integer> scheduleIdMap2 = scheduleIdMap.get(subSite+"_"+afterDate);
				if(scheduleIdMap2==null){
					System.out.println("正在获取可售班次id...");
					Calendar afterCalendar = Calendar.getInstance();
					afterCalendar.add(Calendar.DATE, afterDate);
					Date date = afterCalendar.getTime();
					String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
					afterCalendar.add(Calendar.HOUR_OF_DAY, 2);
					Date afterTwoHourDate = afterCalendar.getTime();
					
					//获取热门线路信息
					String departcity = null;
					String departcityid = null;
					String reachstation = null;
					if(i==-1 && (subSite.contains("nmd") || subSite.contains("nmt") || subSite.contains("cra") || subSite.contains("wechat.bus365.cn") )){
						departcity = "枣阳市";
						departcityid = "420683000000";
						reachstation = "唐河";
					}else{
						if(i==-1){
							i=0;
						}
						String returnStr = HttpUtils.sendHttpGet(subSite);
						String uri = null;
						try {
							if(subSite.length()>22 && !subSite.contains("master") && !subSite.contains("standby")){
								uri = RegexUtils.getSubstrByRegexReturnList(returnStr, "<a title=\".*?\" href=\"(.*?)\">").get(i);
							}else{
								uri = RegexUtils.getSubstrByRegexReturnList(returnStr, "class=\"station\" href=\"(.*?)\">").get(i);
							}
						} catch (Exception e1) {
						}
						if(uri==null){
							System.out.println("获取热门线路信息失败,返回值"+returnStr);
							throw new RuntimeException("获取热门线路信息失败");
						}
						
						String url = subSite + uri;
						returnStr = HttpUtils.sendHttpGet(url);
						departcity = RegexUtils.getSubstrByRegex(returnStr, "id=\"depart_city\" value=\"(.*?)\"");
						departcityid = RegexUtils.getSubstrByRegex(returnStr, "name=\"departcityid_\" value=\"(.*?)\"");
						reachstation = RegexUtils.getSubstrByRegex(returnStr, "id=\"reach_station\" value=\"(.*?)\"");
						if(departcity==null || departcityid==null){
							System.out.println("返回值"+returnStr);
							throw new RuntimeException("获取热门线路信息失败");
						}
					}
					System.out.println("获取热门线路:"+departcity+"到"+reachstation);
					
					
					//获取所有班次
					String urlPath = subSite + "/schedulesearch0";
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("callback", "jsonpCallback");
					paramMap.put("departid", departcityid);
					paramMap.put("departname", departcity);
					paramMap.put("destination", reachstation);
					paramMap.put("departdate", dateStr);
					paramMap.put("departtype", "1");
					paramMap.put("destinationtype", "2");
					paramMap.put("showType", "2");
					
					String jsonStr = HttpUtils.sendHttpGet(urlPath, paramMap, null);
					
					Map<String,Integer> map = new HashMap<String,Integer>();
					Map<String,Object> jsonMap = JsonUtils.parseJson2Map(jsonStr, Object.class, null);
					List jsonList = (List) jsonMap.get("schedules");
					for(Object o : jsonList){
						Map<String,Object> m = (Map<String, Object>) o;
						//如果过了开车前两小时,则不能购票
						if(afterDate==0){
							try {
								String datetimeStr = dateStr+" "+m.get("departtime").toString();
								Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetimeStr);
								boolean b = afterTwoHourDate.after(d);
								if(b){
									continue;
								}
							} catch (ParseException e) {
							}
						}
						//班次为可售状态
						if(m.get("iscansell").toString().equals("1") || m.get("iscansell").toString().equals("3")){
							//票价大于10
							if(m.get("fullprice").toString().length()>3){
								if(!noList.contains(m.get("id").toString())){
									map.put(m.get("id").toString(), (Integer)m.get("residualnumber"));
								}
							}
						}
					}
					scheduleIdMap.put(subSite+"_"+afterDate, map);
				}
				
				scheduleIdMap2 = scheduleIdMap.get(subSite+"_"+afterDate);
				for(String key : scheduleIdMap2.keySet()){
					Integer value = scheduleIdMap2.get(key);
					if(value>10){
						System.out.println("班次:"+key+"还有"+value+"张票");
						scheduleIdMap2.put(key, value-1);
						return key;
					}
				}
				//到这还没有班次,则更换路线
				scheduleIdMap.remove(subSite+"_"+afterDate);
			}
		}
	}
	
	
	/**
	 * 获取authenticityToken
	 * @param subSite 域名
	 * @return arr[0]为cookie,arr[1]为authenticityToken
	 */
	private static String[] getAuthenticityToken(String subSite){
		String[] arr = null;
		synchronized (subSite) {
			String[] tokenArr = tokenArrMap.get(subSite);
			if(tokenArr!=null){
				return tokenArr;
			}
			System.out.println("正在获取authenticityToken...");
			String urlPath = subSite+"/getAuthenticityToken/";
			String str = HttpUtils.sendHttpGet(urlPath);
			arr =  str.split(",");
			try {
				arr[1] = URLDecoder.decode(arr[1].trim(), "UTF-8");
				byte[] decryptFrom = AlgorithmUtils.hexStrToByte(arr[1]);
				//byte[] decryptFrom = AlgorithmUtils.parseHexStr2Byte(arr[1]);
				arr[1] = new String(AlgorithmUtils.decryptAES(decryptFrom,"112358".getBytes("utf-8")),"utf-8");
				System.out.println("获取authenticityToken成功");
			} catch (Exception e) {
				e.printStackTrace();
			} 
			tokenArrMap.put(subSite, arr);
		}
		return arr;
	}
	
	
	/**
	 * 获得orderToken
	 * @param orderno
	 * @param orderid
	 * @return
	 * @throws Exception
	 */
	public static String getOrderToken(String orderno,Long orderid){
		try {
			String str = String.valueOf(orderid)+"#"+orderno+"#"+new Timestamp(Calendar.getInstance().getTimeInMillis());
			String desOrder = AlgorithmUtils.encodeBase64(AlgorithmUtils.encryptDES(str.getBytes("utf-8"), "bus84365".getBytes("utf-8")));
			return desOrder;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 检验orderToken
	 * @param ordertoken
	 * @param orderno
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	private static boolean checkOrderToken(String ordertoken,String orderno,String type){
		try {
			byte[] bytes = AlgorithmUtils.decryptDES(AlgorithmUtils.decodeBase64(ordertoken), "bus84365".getBytes("utf-8"));
			String[] order = new String(bytes,"utf-8").split("#");
			String orderid = order[0];
			String realorderno = order[1];
			if(type.equals("orderid")){
				if(orderid.equals(orderno)){
					return true;
				}
			}else{
				if(realorderno.equals(orderno)){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 调用生成验证码的接口
	 * @param subSite
	 */
	@SuppressWarnings("all")
	public static void createCheckImg(String subSite){
		String createImgUrl = subSite + "/imagevalidate/createValidateImage";
		sendGet(createImgUrl,null,null);
	}
	
	/**
	 * 发送请求
	 * @param method 请求方式
	 * @param url 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	private static byte[] send(String method,String url,Map<String,String> paramMap,Map<String,String> headerMap, boolean isUrlEncoding){
		int index = url.indexOf("/",10);
		String subSite = url;
		if(index!=-1){
			subSite = subSite.substring(0,index);
		}
		
		if(paramMap==null){
			paramMap = new HashMap<String,String>();
		}
		if(headerMap==null){
			headerMap = new HashMap<String,String>();
		}
		
		//如果是网站的,先获取authenticityToken
		if((subSite.contains("bus365.com") || subSite.contains("bus365.cn") || subSite.contains("localhost:9032")) && !subSite.contains("mngr") && !subSite.contains("pays")){
			String[] arr = Bus365Utils.getAuthenticityToken(subSite);
			if(method.equals("POST")){//
				paramMap.put("authenticityToken",arr[1]);
			}
			//get也需要cookie,比如生成验证码时
			headerMap.put("Cookie",arr[0]);
		}
		
		if(returnType!=null){
			if("json".equals(returnType)){
				headerMap.put("accept", "application/json,");
			}else if("xml".equals(returnType)){
				headerMap.put("accept", "text/xml,");
			}else if("html".equals(returnType)){
				headerMap.put("accept", "text/html,");
			}
		}
		return new HttpUtils().sendReturnByteArr(method, url, paramMap, headerMap, isUrlEncoding);
	}
	
	/**
	 * 发送get请求
	 * @param urlPath 请求路径
	 * @return
	 */
	public static String sendGet(String url){
		return HttpUtils.parseHttpByteToString(send("GET",url,null,null,true),"utf-8");
	}
	
	/**
	 * 发送get请求
	 * @param urlPath 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public static String sendGet(String url, Map<String,String> paramMap,Map<String,String> headerMap){
		return HttpUtils.parseHttpByteToString(send("GET",url,paramMap,headerMap,true),"utf-8");
	}
	
	/**
	 * 发送get请求
	 * @param url
	 * @param isUrlEncoding
	 * @return
	 */
	public static String sendGetNoUrlEncoding(String url, Map<String,String> paramMap,Map<String,String> headerMap){
		return HttpUtils.parseHttpByteToString(send("GET",url,paramMap,headerMap,false),"utf-8");
	}
	
	/**
	 * 发送post请求获取字符串
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public static String sendPost(String url,Map<String,String> paramMap,Map<String,String> headerMap){
		return sendPost(url,paramMap,headerMap,false);
	}
	
	/**
	 * 发送post请求获取字符串
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public static String sendPost(String url,Map<String,String> paramMap,Map<String,String> headerMap,boolean isUrlEncoding){
		return HttpUtils.parseHttpByteToString(send("POST",url,paramMap,headerMap,isUrlEncoding),"utf-8");
	}
	
	//发送post请求获取字节数组
	public static byte[] sendPostReturnByteArray(String url,Map<String,String> paramMap,Map<String,String> headerMap){
		return send("POST",url,paramMap,headerMap,false);
	}
	
	/**
	 * 带有key值得md5摘要函数
	 * @param aValue  要获取摘要的字符串
	 * @param aKey  获取摘要用的key值
	 * @return
	 */
	private static String md5WithKey(String aValue, String aKey) {
		if(null == aKey || "".equals(aKey)){
			return AlgorithmUtils.getMD5Value(aValue);
		}
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes("UTF-8");
			value = aValue.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}
		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return AlgorithmUtils.byteToHexStr(dg);
		//return AlgorithmUtils.parseBytesHexStr(dg);
	}
	
	/**
	 * 客户端非对称加密
	 * @param paramsMap 加密数据
	 * @param isUrlEncoding 是否进行url编码
	 * @return
	 */
	@SuppressWarnings("all")
	public static Map<String,String> encryptData(Map<String,Object> paramsMap,boolean isUrlEncoding){
		return encryptData(paramsMap,Constants.coupon_privateKey,Constants.coupon_md5key,isUrlEncoding);
	}
	
	/**
	 * 客户端非对称加密
	 * @param paramsMap 加密数据
	 * @param isUrlEncoding 是否进行url编码
	 * @return
	 */
	public static Map<String,String> encryptData(Map<String,Object> paramsMap,String privateKey,String md5Key,boolean isUrlEncoding){
		try {
			String params = JsonUtils.parseObject(paramsMap, null);
//			Map<String,String> map = SecretClientUtil.encryptData(Constants.coupon_privateKey, Constants.coupon_md5key, params);
			
			Map<String,String> map = new HashMap<String,String>();
			byte[] bytes = params.getBytes("utf-8");
			bytes = AlgorithmUtils.encryptByPrivateKey(bytes, privateKey);
			String data = AlgorithmUtils.encodeBase64(bytes);
			map.put("data", data);
			String md5 = md5WithKey(params,md5Key);
			map.put("md5", md5);
			
			if(isUrlEncoding){
				for(String key : map.keySet()){
					map.put(key, URLEncoder.encode(map.get(key),"utf-8"));
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 解密或直接返回数据
	 * @author likaihao
	 * @date 2016年4月21日 下午3:47:52
	 * @param jsonStr
	 * @return
	 */
	public static String decryptDataOrReturnData(String jsonStr){
		if(jsonStr.contains("data") && jsonStr.contains("md5")){
			return decryptData(jsonStr,Constants.coupon_privateKey,Constants.coupon_md5key).toString();
		}else{
			return jsonStr;
		}
	}
	
	/**
	 * 客户端非对称解密
	 * @param jsonStr
	 * @return
	 */
	public static Map<String,Object> decryptData(String jsonStr){
		return decryptData(jsonStr,Constants.coupon_privateKey,Constants.coupon_md5key);
	}
	
	/**
	 * 客户端非对称解密
	 * @param jsonStr
	 * @return
	 */
	public static Map<String,Object> decryptData(String jsonStr,String privateKey,String md5Key){
		Map<String,String> jsonMap = JsonUtils.parseJson2Map(jsonStr, String.class, null);
		try {
			byte[] bytes = AlgorithmUtils.decodeBase64(jsonMap.get("data"));
			bytes = AlgorithmUtils.decryptByPrivateKey(bytes, privateKey);
			String data = new String(bytes,"utf-8");
			String md5 = md5WithKey(data,md5Key);
			if(!md5.equals(jsonMap.get("md5"))){
				//验签失败
				return null;
			}
			return JsonUtils.parseJson2Map(data, Object.class, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 客户端非对称解密
	 * @param jsonStr
	 * @return
	 */
	public static String decryptDataReturnString(String jsonStr,String privateKey,String md5Key){
		Map<String,String> jsonMap = JsonUtils.parseJson2Map(jsonStr, String.class, null);
		try {
			byte[] bytes = AlgorithmUtils.decodeBase64(jsonMap.get("data"));
			bytes = AlgorithmUtils.decryptByPrivateKey(bytes, privateKey);
			String data = new String(bytes,"utf-8");
			String md5 = md5WithKey(data,md5Key);
			if(!md5.equals(jsonMap.get("md5"))){
				//验签失败
				return null;
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 下订单
	 * @param subSite 域名
	 * @param isWap 是否是移动web(返回值为json)
	 * @param isRobot 是否是机器人支付(邮箱是否为gaoyuan@bus365.com)
	 * @param isPay 是否要支付完成的订单
	 * @param afterDate 需要买几天后的车票
	 * @return map (key有orderno,queryOrderUrl,wapFuncStr)
	 */
	public static Map<String,String> createOrder(String subSite,boolean isWap, boolean isRobot,boolean isPay,int afterDate){
		boolean isInsurance = false;//是否带保险
		boolean isShuangRen = false;//是否是双人
		//boolean isFixedUser = false;//是否为指定用户购票
		String userId = ""; //76971060000000164, 76201060000000212
		String scheduleId = "";//指定班次,没有的话会自动获取热门线路班次
		
		return createOrder(subSite,isWap,isRobot,isPay,afterDate,isInsurance,isShuangRen,userId,scheduleId);
	}
	
	/**
	 * 下订单
	 * @param subSite 域名
	 * @param isWap 是否是移动web(返回值为json)
	 * @param isRobot 是否是机器人支付(邮箱是否为gaoyuan@bus365.com)
	 * @param isPay 是否要支付完成的订单
	 * @param afterDate 需要买几天后的车票
	 * @param isInsurance 是否带保险
	 * @param isShuangRen 是否是双人
	 * @param isFixedUser 是否为指定用户购票
	 * @param scheduleId 指定班次,没有的话会自动获取热门线路班次
	 * @return map (key有orderno,queryOrderUrl,wapFuncStr)
	 */
	public static Map<String,String> createOrder(String subSite,boolean isWap, boolean isRobot,boolean isPay,int afterDate,boolean isInsurance,boolean isShuangRen,String userId,String scheduleId){
		Map<String,String> returnMap = new HashMap<String,String>();
		String urlPath = subSite+"/order/createorder";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("order.seattype", "普通座");
		paramMap.put("order.authenticitytoken", "b10b0c7ffb499816321290b0f1410240face5676");
		paramMap.put("ismock", "0");
		paramMap.put("order.verifycode", "aaaa");
		
		paramMap.put("order.issavepassenger", "1");
		paramMap.put("clientinfo", "{\"clienttype\":\"0\",\"browsername\":\"Netscape\",\"browserversion\":\"5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36\",\"osinfo\":\"Win32\",\"computerinfo\":\"null\"}");
		
		paramMap.put("order.passengers", "[{\"name\":\"李凯昊\",\"idnum\":\"\",\"phonenum\":\"15210235251\",\"passmark\":\"tableDefid76971050000000654\",\"premiumstate\":0,\"premiumcount\":\"0\"}]");
		paramMap.put("order.passengername", "李凯昊");
		paramMap.put("order.passengerphone", "15210235251");
		paramMap.put("order.travelOrderInfo.startstationname", "");
		paramMap.put("order.travelOrderInfo.venue", "");
		paramMap.put("order.buscityname1", "beijing");
		
		//双人
		if(isShuangRen){
			paramMap.put("order.passengers","[{\"name\":\"张三\",\"idnum\":\"\",\"phonenum\":\"18734179970\",\"passmark\":\"tableDef\",\"premiumstate\":0,\"premiumcount\":\"0\"},{\"name\":\"李四\",\"idnum\":\"\",\"phonenum\":\"18734179970\",\"passmark\":\"tableDef1\",\"premiumstate\":0,\"premiumcount\":\"0\"}]");
		}
		
		//带保险
		if(isInsurance){
			String idnum = "14273119950813033X";
			idnum = IDCardGeneratorUtils.generate();
			paramMap.put("order.passengers", "[{\"name\":\"李凯昊\",\"idnum\":\""+idnum+"\",\"phonenum\":\"15210235251\",\"passmark\":\"tableDefid76971050000000654\",\"premiumstate\":1,\"premiumcount\":\"1\"}]");
		}
		
		//为指定user购票
		if(userId!=null){
			paramMap.put("order.userid", userId);
		}
		
		//是否为机器人购票
		if(isRobot || isPay){
			paramMap.put("order.passengeremail", "gaoyuan@bus365.com");
		}else{
			//paramMap.put("order.passengeremail", "likaihao@bus365.com");
		}
		
		//如果是移动web,则返回json
		Map<String,String> headerMap = new HashMap<String,String>();
		if(isWap){
			headerMap.put("accept", "application/json,*/*");
		}
		
		//下订单
		String str = null;
		String orderno = null;
		for(;;){
			try {
				if(scheduleId==null || scheduleId.length()==0){
					scheduleId = getScheduleId(subSite,afterDate);//获取两天后的一个班次
				}
				paramMap.put("order.scheduleid",scheduleId);
				str = sendPost(urlPath, paramMap, headerMap);
				if(str.contains("该班次班次已停售")){
					throw new RuntimeException("该班次班次已停售");
				}
				//带保险的话,每个班次只能买一次
				if(isInsurance){
					scheduleIdMap.get(subSite+"_"+afterDate).remove(scheduleId);
				}
			
				if(!isWap){
					//获取订单号 和 orderToken
					List<String> ordernoList = RegexUtils.getSubstrByRegexReturnList(str, "<span hname=\"orderno\">(.*?)</span>");
					if(ordernoList.size()==0){
						String errMsg = RegexUtils.getSubstrByRegex(str, "value=\"(.*?)\" id=\"noRemain\"/>");
						if(errMsg!=null){
							throw new RuntimeException(errMsg);
						}else{
							throw new RuntimeException("下订单失败,返回内容:"+str);
						}
					}
					orderno = ordernoList.get(0);
				}else{
					orderno = RegexUtils.getSubstrByRegex(str, "\"orderno\":\"(.*?)\"");
				}
			}catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				System.out.println("使用班次id:"+scheduleId+"下订单异常,"+e.getMessage());
				if(scheduleIdMap.containsKey(subSite+"_"+afterDate)){
					scheduleIdMap.get(subSite+"_"+afterDate).remove(scheduleId);
					scheduleId = null;
				}
				continue;
			}
			break;
		}
		
		
		if(!isWap){
			String orderid = RegexUtils.getSubstrByRegexReturnList(str, "<span hname=\"orderid\">(.*?)</span>").get(0);
			String ordertoken = getOrderToken(orderno, new Long(orderid));
			try {
				if(!isPay){
					//解析继续支付页面url
					
						//String ordertoken = CommonUtils.getSubstrByRegex(str, "<span hname=\"ordertoken\">(.*?)</span>");
						String queryOrderUrl = subSite+"/orderinfoaction/showcreatedorder?id="+orderid+"&ordertoken="+URLEncoder.encode(ordertoken,"utf-8");
						returnMap.put("queryOrderUrl", queryOrderUrl);
						System.out.println("继续支付页面:"+queryOrderUrl);
						//System.out.println("查询支付结果页面:http://nmd.bus365.cn/ticket/checkTicketResult/0?orderno="+orderno+"&ordertoken="+URLEncoder.encode(ordertoken,"utf-8"));
						System.out.println("查询支付结果页面:"+subSite+"/ticket/checkTicketResult/0?orderno="+orderno);
				}else{
					//调用机器人支付
					urlPath = subSite + "/ticket/robotsellticket";
					paramMap = new HashMap<String,String>();
					paramMap.put("orderno", orderno);
					paramMap.put("gatewayid", "65");
					str = sendPost(urlPath, paramMap, null);
					
					//拼凑查询非会员订单url(不能直接跳转)
					String queryOrderInfoUrl = subSite + "/order/nonmember/0?ismock=1&passengerphone=15210235251&orderno="+orderno+"&verificationcode=aaaa";
					System.out.println("查询非会员订单页面:"+queryOrderInfoUrl);
					//System.out.println("查询支付结果页面:http://nmd.bus365.cn/ticket/checkTicketResult/0?orderno="+orderno+"&ordertoken="+URLEncoder.encode(ordertoken,"utf-8"));
					System.out.println("查询支付结果页面:"+subSite+"/ticket/checkTicketResult/0?orderno="+orderno);
				}
			} catch (UnsupportedEncodingException e) {
			}
			System.out.println("订单号:"+orderno);
			returnMap.put("orderno", orderno);
			return returnMap;
		}else{
			//移动web
			System.out.println("订单号:"+orderno);
			System.out.println("进入以下页面: "+subSite+"/m_ticket/#user_login");
//			System.out.println("在浏览器中执行: localStorage.orderdata=\""+StringUtils.getEscapeString(str)+"\";");
//			System.out.println("在浏览器中执行: $.ui.loadContent(\"order_pay\", false, false, \"slide\")");
			System.out.println("在浏览器中执行: \n");
			
			String showApplyOrderSucc = "function showApplyOrderSucc(data){localStorage.orderdata=JSON.stringify(data);if(!data[\"maxcanceltimes\"]){$(\"#ordermaxCanelCount\").text(\"3\")}else{$(\"#ordermaxCanelCount\").text(data[\"maxcanceltimes\"])}localStorage.opayValueWc=$(\"#ordermaxCanelCount\").text();var bankDataStr=\"<td>\";$.ajax({url:localStorage.domainUrl+\"/ticket/createWapGetways/0?callback=?\",success:function(pdata){da=pdata[\"data\"];for(var i=0,ilen=da.length;i<ilen;i++){if(da[i][\"paywaytype\"]==4||da[i][\"paywaytype\"]==5){bankDataStr+='<div style=\"float:left;width:50%;\" align=\"center\"><div style=\"width:100%;height:60px;padding-top:20px;\"><input id='+da[i][\"bankcode\"]+' type=\"radio\" style=\"padding-top:3px;\" name=\"br\" changpayrate='+da[i][\"changpayrate\"]+\" gatepaylowest=\"+da[i][\"gatepaylowest\"]+\" payid=\"+da[i][\"id\"]+\" payrate=\"+da[i][\"payrate\"]+\" gatewayuserid=\"+da[i][\"gatewayuserid\"]+\" passengerpaylowest=\"+da[i][\"passengerpaylowest\"]+' onclick=\"countPirce(event);\"/>'+\"<label for=\"+da[i][\"bankcode\"]+'><span style=\"display:inline-block;width:100px;height:38px;position:relative;top:-7px;background:url(/public/www/images/'+da[i][\"paytradename\"]+'.png) no-repeat;\">'+\"</span></label></div></div>\"}}if(da.length==1){bankDataStr+=\"<div style='float:left;width:50%;'>&nbsp;</div>\"}bankDataStr+=\"</td>\";$(\"#bankTr\").html(bankDataStr);localStorage.bankDataStr=bankDataStr},error:function(request,status,err){alert(\"获取网关信息失败\")}});var takePositon=data[\"businfo\"][\"takeposition\"];if(!takePositon){takePositon=\"暂无\"}if(localStorage.userid){var datacoupon=getcoupon(1);var istrue=false;$.each(datacoupon,function(n,o){if(o.status==\"0\"){istrue=true;return}});if(istrue){$(\".couponsli\").show()}if(lotteryactivity){$(\"#gotorotate\").show();$(\"#baygotocon\").click(function(){$.ui.loadContent(\"lotteryId\",false,false,\"slide\");$(\"#bus_lotterymsg\").attr(\"isselect\",\"f\");$(\"#backgroundDiv\").hide();messageObj.addClick($(\"#bus_lotterymsg\"),function(){messageObj.isselect($(\"#bus_lotterymsg\"),\"isselect\",function(){$(\"#backgroundDiv\").show();$(\"#bus_lotterymsg\").attr(\"isselect\",\"t\")},function(){$(\"#backgroundDiv\").hide();$(\"#bus_lotterymsg\").attr(\"isselect\",\"f\")})});lottery.loadimg([\"/public/www/images/lottery1.png\",\"/public/www/images/lottery2.png\",\"/public/www/images/lottery3.png\",\"/public/www/images/lottery4.png\",\"/public/www/images/lottery5.png\",\"/public/www/images/lottery6.png\"],function(){initlottery()})})}}$(\"#getTicketAddress\").text(takePositon);var premium=data[\"order\"][\"premium\"];premium=premium==null?\"0.00\":premium;$(\"#orderPremiumPrice\").text();$(\"#hideOrderPrice\").text(data[\"order\"][\"price\"]);$.ui.loadContent(\"order_pay\",false,false,\"slide\")};";
			String funcStr = "(function(){"
					+ showApplyOrderSucc
					+ " var subSite = \""+subSite+"\";"
					+ "	var str = \""+StringUtils.getJavaEscapeString(str)+"\";"
					+ "	location = subSite+\"/m_ticket/#user_login\";"
					+ "	localStorage.domainUrl = subSite;"
					+ "	var data = jQuery.parseJSON(str);"
					+ "	showApplyOrderSucc(data);"
					+ " })();";
			returnMap.put("wapFuncStr", funcStr);
			System.out.println(funcStr);
			return returnMap;
		}
	}
	
	/**
	 * 退票
	 * @param subSite 域名
	 * @param orderno 订单号
	 */
	@SuppressWarnings("unchecked")
	public static boolean refundTicket(String subSite,String orderno){
		//先生成一下验证码
		//System.out.println("生成验证码...");
		String createImgUrl = subSite + "/imagevalidate/createValidateImage";
		Bus365Utils.sendGet(createImgUrl,null,null);
		
		//拼凑查询非会员订单url,获取ticketid
		//System.out.println("获取ticketid...");
		Map<String,String> headerMap = new HashMap<String,String>();
		String queryOrderInfoUrl = subSite + "/order/nonmember/0?ismock=1&passengerphone=15210235251&orderno="+orderno+"&verificationcode=aaaa";
		headerMap.put("accept", "application/json,*/*");
		String jsonStr = Bus365Utils.sendGet(queryOrderInfoUrl,null,headerMap);
		headerMap.remove("accept");
		
		Map<String,Object> map = JsonUtils.parseJson2Map(jsonStr, Object.class, null);
		List<?> list = (List<?>) map.get("orderDetails");
		map = (Map<String, Object>) list.get(0);
		String ticketid = map.get("id").toString();
		//System.out.println(ticketid);
		
		//退票
		//System.out.println("正在退票...");
		String lastrefundcost = "5";
		String urlPath = subSite + "/refundticket/refundTicket";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("ticketid", ticketid);
		paramMap.put("orderno", orderno);
		headerMap.put("accept", "application/json,*/*");
		for(int i=0;i<2;i++){
			paramMap.put("lastrefundcost", lastrefundcost);
			System.out.println("退票开始...");
			jsonStr = Bus365Utils.sendPost(urlPath, paramMap, headerMap);
			if(jsonStr.contains("variables")){
				System.out.println(jsonStr);
				return false;
			}
			
			map = JsonUtils.parseJson2Map(jsonStr, Object.class, null);
			if(map.containsKey("refundmess")){
				System.out.println(map.get("refundmess"));
				if(map.get("refundmess").equals("退票成功")){
					return true;
				}
			}else{
				if(i==0){
					try {
						lastrefundcost = map.get("refundcost").toString();
						continue;
					} catch (Exception e) {
					}
				}
				System.out.println("退票失败,返回字符串:"+jsonStr);
				return false;
			}
			break;
		}
		return false;
	}
	
	/**
	 * 保证退票
	 * @param subSite 域名
	 * @param orderno 订单号
	 */
	@SuppressWarnings("unchecked")
	public static void refundFailProcess(String subSite,String orderno){
		//先生成一下验证码
		//System.out.println("生成验证码...");
		String createImgUrl = subSite + "/imagevalidate/createValidateImage";
		Bus365Utils.sendGet(createImgUrl,null,null);
		
		//拼凑查询非会员订单url,获取ticketid
		//System.out.println("获取ticketid...");
		Map<String,String> headerMap = new HashMap<String,String>();
		String queryOrderInfoUrl = subSite + "/order/nonmember/0?ismock=1&passengerphone=15210235251&orderno="+orderno+"&verificationcode=aaaa";
		headerMap.put("accept", "application/json,*/*");
		String jsonStr = Bus365Utils.sendGet(queryOrderInfoUrl,null,headerMap);
		headerMap.remove("accept");
		
		Map<String,Object> map = JsonUtils.parseJson2Map(jsonStr, Object.class, null);
		List<?> list = (List<?>) map.get("orderDetails");
		map = (Map<String, Object>) list.get(0);
		String ticketid = map.get("id").toString();
		//System.out.println(ticketid);
		
		//退票
		//System.out.println("正在退票...");
		String urlPath = subSite + "/refundticket/refundFailProcess";
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("ticketid", ticketid);
		paramMap.put("orderno", orderno);
		headerMap.put("accept", "application/json,*/*");
		System.out.println("保证退票开始...");
		String str = Bus365Utils.sendPost(urlPath, paramMap, headerMap);
		System.out.println("返回值:"+str);
	}
	
}