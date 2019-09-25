package work.protec.bus365.store.bus365;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.AlgorithmUtils;
import utils.IOUtils;
import utils.JsonUtils;
import work.protec.bus365.store.bus365.other.Constants;

public class PayServerBP {
	
	/**
	 * 调用支付服务器 获取支付方式 接口的参数
	 * @author likaihao
	 * @date 2015年11月17日 下午5:51:40
	 * @param settlementunitid
	 * @param orderno
	 * @param ordertoken
	 */
	public Map<String,String> gateways_param(String settlementunitid,String orderno,String ordertoken){
		Map<String,String> keyMap = getSettlementKey(settlementunitid);
		
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("orderno", orderno);
		dataMap.put("ordertoken", ordertoken);
		
		Map<String,String> paramMap = Bus365Utils.encryptData(dataMap, keyMap.get("privateKey"), keyMap.get("md5Key"), false);
		paramMap.put("settlementunitid", settlementunitid);
		return paramMap;
	}
	
	/**
	 * 调用支付服务器 创建支付参数 接口的参数
	 * @author likaihao
	 * @date 2015年11月20日 下午2:08:12
	 * @param settlementunitid
	 * @param orderno
	 * @param totalprice
	 * @param topayinfoid
	 * @param successnotify
	 * @param frontnotify
	 * @return
	 */
	public Map<String,String> payparams_param(String settlementunitid,String orderno,String totalprice,String topayinfoid,String successnotify,String frontnotify){
		Map<String,String> keyMap = getSettlementKey(settlementunitid);
		
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("orderno", orderno);
		dataMap.put("totalprice", totalprice);
		dataMap.put("topayinfoid", topayinfoid);
		dataMap.put("successnotify", successnotify);
		dataMap.put("frontnotify", frontnotify);
		
		Map<String,String> paramMap = Bus365Utils.encryptData(dataMap, keyMap.get("privateKey"), keyMap.get("md5Key"), false);
		paramMap.put("settlementunitid", settlementunitid);
		return paramMap;
	}
	
	//获得结算单位的相关密钥
	public static Map<String,String> getSettlementKey(String settlementunitid){
		Map<String,String> map = new HashMap<String,String>();
		if(settlementunitid.equals("901000000000001")){
			map.put("privateKey", "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK3aX+bArvKZlvt3n6B5WpsQfm2Wjfy6vxtWltv31bgR/GT25yA2yZ9ls4u8oLvJ9YiQnbxkrG2Sg+yJjVHSvNYy8C3bcJT+E8AzDcPE78ihTnrF36vV6m2NmSs9t1cHhn9zwLwvsuJq9fRitKHyiFge3QzomOFJXlExhVdJD9YxAgMBAAECgYEAmBA479G7dDEJUZy5P5jFunn7y4jzWopRW+0p+O7WvbDlrb4UPSYOxuAi4mezw22L2M59WWpJM3IC+CL8PARy7Am0QyuJo6XdNQLaAycYW6Ar3FeqZauaSctjiAlir26j+rQJ5WJpe9OquVHdQyHQivByPqicF2olGzlA4+9bKsECQQDlk250oBZBJmjFtNEoinqb4iFUImsADxzqYciy1cUZyz46ar1uh2HdgRnw0EOv9SkX0GDJ8CNiZxLOW6Omb0h5AkEAwd0KbhOt96MC4xGr7IDNfVcJJQnJ6ZCjd+wYtlca3sTPcO8YDXmp5JatoZcIKCETd+gvb7UyeHslwrqH2s/9eQJBAMbjwDinjEArD2/g46dUuT4dXTRvkPV/IlToHCDa1CQxkRq46J4+CYoF07xuIYXqRgyMiZ3JPk5876z5dUC2fjkCQA+IaWSGeSLlUb7l1JOfgldvmND55s6rAx4Bsb0yX3nH5SMUVSYHQICLPiA7XLDybX/m/54As6mJnG3syiPTFGkCQQCH2thnybK3QsoGMDcFnIEj9wKGe7kz3NjWZIaMhz+FZa0J02tGLTeXd2eOLDTzZtxARPtbHD2YBr5nwrRys6gE");
			map.put("publicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt2l/mwK7ymZb7d5+geVqbEH5tlo38ur8bVpbb99W4Efxk9ucgNsmfZbOLvKC7yfWIkJ28ZKxtkoPsiY1R0rzWMvAt23CU/hPAMw3DxO/IoU56xd+r1eptjZkrPbdXB4Z/c8C8L7LiavX0YrSh8ohYHt0M6JjhSV5RMYVXSQ/WMQIDAQAB");
			map.put("md5Key", "bus365_2014");
		}else if(settlementunitid.equals("901000000000002")){
			map.put("privateKey", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMCrfEShAxzLqiUcuff5SHx2U8N2MAnISsfazDAlvsu+5ePqaFQH73hHhEKz2TDcFHE/7F4O6bSrDkmRf0N3cYCt6q9WGJfzdKo4nmGrK7fggHf0oZ+rxgdfQwTJE9tdWKqibnWGNhmsGuBaSvzaqvcq5FIPN/1S3WN2lAZwJkJ7AgMBAAECgYAin25gYtLNXOgBbU0Z+6kD/LuGt3lgeKa/jVj+GfpUYMStFVWULCzjX26sEeeouRPPWh5etK1YsxiJLeVBuXgiyIlZZMaXMFHfz+YvCZSrCB/QBrLiKPh6ULpEE3zkbQtKUvT6KFPXN4sOiVTq0sJhYYwnRF+Q/RiQTSPArxyFQQJBAPDJpsx06JAjFMQYUfUq0damUXS5xnW8LjkjoHtDuXfA5xNgMR94EbSO85BA7DDggD3zvddb5j7mEu2oJmNzRSECQQDM15s316NTYkZx7+E948HfRa50if1ttyzVP05xDMJzJvYMlP4NiFQEc9wsaZNKJHQwLuzwN540TJTRb5BBRvgbAkBcoXblmOQO7zAB8Ek3Q+3N4dXcTcunZ2lw2d/XC8rB4xgCQDMlpA9LouLYT+GNPBCZxLb3rob5E5Gck9+QyruBAkA+gKc28dA/GCSAjX4/4Sf91Yg2vQgcGyFN7CQmMIuAkWnQSwH95RQly6hp0MkFfXwKW7cmUQIv2RIrmYv9rwbzAkEAy6wl40Wis7eu7kpR7iIf2hANI41eX+JYSwyJ9tjRIU4u4jwKCbdelVtQH9LTeL/aSg+rEFGIOYs1i85xw9yDEA==");
			map.put("publicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAq3xEoQMcy6olHLn3+Uh8dlPDdjAJyErH2swwJb7LvuXj6mhUB+94R4RCs9kw3BRxP+xeDum0qw5JkX9Dd3GAreqvVhiX83SqOJ5hqyu34IB39KGfq8YHX0MEyRPbXViqom51hjYZrBrgWkr82qr3KuRSDzf9Ut1jdpQGcCZCewIDAQAB");
			map.put("md5Key", "7ecd6471b655732e27e11fde67fd08ea");
		}else if(settlementunitid.equals("901000000000003")){
			map.put("privateKey", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL5KXSQoIcf5z55nuejM8UpZqx/xAmqNkpI8BKAYVJZf8xkisrhNnQG/YcTq8QTVOeF6cnnpfhHcXQF7g5b+a8RUdYq7Lv/gSGoS066212UBu5cieXnsKad6+ThCEojZpArswWKgn1Aa+Fptdw70hJrPVlnDPGxhGGonxLQ7FZJ7AgMBAAECgYEAsrPPaKcvokddzRsIIjJk3zf6VtLVPA+b+CzZAygWN+n/uDk3sZFhw0RhorYohitgYfnGC+tU850I0t2VpHVl6affIaNZHPyZfo+q8fNBLgrXFiBNwuKmW9IQGpG/MKUD4i0vP1Lv7Cmlead/IxUBZje977C1f8XLn+bjwW31YEECQQD0Sz5cW08Ny5xwftdEsq15YN7wXjHbIKfuO6/IRT571LJKxpgwwExodAkMeyijQ6CbAy1TlwgknmrEFsy8r1mRAkEAx2io0TfVTVk5+cY22NyLW9fXXpawSxixtzNk0BzClUucNjFpPLzNXiwW9XuaGSpNlK7Z4ln+n3EQdTEzkE6FSwJBAJkkwkcrhndjeQKcwV5zlIRO0fNlWWPBKQUTrbKilfUS9WIi9S06n6urcjC81FDlCD2k1DleqOAr30pl8JsoYcECQDNqVuzrGoKBGSO/6fBsnTonIw7uh7I1Qc1c4QeQAtP/1N1cTCgu/jzj3V+aaRdIAwztMJzeW3q86mNtNkBlHpUCQEGpXuyyVNWVPFWsprVmYZ+3MZ1YcWbdJvL7wfzuDLtfafL64mtOEBT2R7PEtrhRWEe1BuuF0pITGkEq5GyqX1k=");
			map.put("publicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+Sl0kKCHH+c+eZ7nozPFKWasf8QJqjZKSPASgGFSWX/MZIrK4TZ0Bv2HE6vEE1TnhenJ56X4R3F0Be4OW/mvEVHWKuy7/4EhqEtOuttdlAbuXInl57Cmnevk4QhKI2aQK7MFioJ9QGvhabXcO9ISaz1ZZwzxsYRhqJ8S0OxWSewIDAQAB");
			map.put("md5Key", "5ab7142d6bc6fbd2cebbed9c07239c3c");
		}
		return map;
	}
	
	//通过支付商户id加密
	public Map<String,String> encryptByPaybussinessKey(Map<String,Object> dataMap,String paybussinessid){
		Map<String,String> keyMap = getPaybussinessKey(paybussinessid);
		Map<String,String> paramMap = Bus365Utils.encryptData(dataMap, keyMap.get("privateKey"), keyMap.get("md5Key"), false);
		return paramMap;
	}
	
	//通过支付商户id解密
	public String decryptByPaybussinessKey(String jsonStr,String paybussinessid){
		if(jsonStr.contains("data") && jsonStr.contains("md5")){
			Map<String,String> keyMap = getPaybussinessKey(paybussinessid);
			return Bus365Utils.decryptDataReturnString(jsonStr, keyMap.get("privateKey"), keyMap.get("md5Key"));
		}
		return jsonStr;
	}
	
	/**
	 * 仅通过data解密,不验签,支付服务器请求参数解密用
	 * @author likaihao
	 * @date 2016年6月15日 下午7:13:12
	 * @param data
	 * @return
	 */
	public static String decryptRequestParamOnlyData(String data,String paybussinessid){
		String publickey = null;
		if(paybussinessid==null || paybussinessid.length()==0){
			publickey = Constants.publickey;
		}else{
			publickey = getPaybussinessKey(paybussinessid).get("publicKey");
		}
		try {
			byte[] bytes = AlgorithmUtils.decodeBase64(data);
			bytes = AlgorithmUtils.decryptByPublicKey(bytes, publickey);
			return new String(bytes,"utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 仅通过data解密,不验签,支付服务器响应参数解密用
	 * @author likaihao
	 * @date 2016年6月15日 下午7:13:12
	 * @param data
	 * @return
	 */
	public static String decryptResponseParamOnlyData(String data,String paybussinessid){
		String privateKey = null;
		if(paybussinessid==null || paybussinessid.length()==0){
			privateKey = Constants.privateKey;
		}else{
			privateKey = getPaybussinessKey(paybussinessid).get("privateKey");
		}
		try {
			byte[] bytes = AlgorithmUtils.decodeBase64(data);
			bytes = AlgorithmUtils.decryptByPrivateKey(bytes, privateKey);
			return new String(bytes,"utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} 
	}
	
	//获得支付商户的相关密钥
	public static Map<String,String> getPaybussinessKey(String paybussinessid){
		Map<String,String> map = new HashMap<String,String>();
		String path = PayServerBP.class.getResource("paybusiness.txt").getPath();
		List<String> lineList = IOUtils.readFileReturnList(path);
		String businessStr = null;
		for(String line : lineList){
			if(line.startsWith(paybussinessid)){
				businessStr = line;
				break;
			}
		}
		if(businessStr==null){
			throw new RuntimeException("没有发现指定商户的密钥:"+paybussinessid);
		}
		String[] arr = businessStr.split("\t");
		map.put("privateKey",arr[4]);
		map.put("publicKey",arr[5]);
		map.put("md5Key",arr[6]);
		return map;
	}
	
	//将获取支付参数接口的返回值转换为表单(支付宝网站)
	public String getForm(String jsonStr){
		Map<String,String> map = JsonUtils.parseJson2Map(jsonStr, String.class);
		if(map.containsKey("string")){
			String stringStr = map.get("string");
			map = JsonUtils.parseJson2Map(stringStr, String.class);
		}
		
		//下边是网站的代码
		String actionUrl = map.get("actionUrl").toString();
		String remark = map.get("remark").toString();
		String formSubmitMethod = map.get("formSubmitMethod") == null ? "GET" : map.get("formSubmitMethod").toString();
		map.remove("actionUrl");
		map.remove("remark");
		map.remove("formSubmitMethod");

		// 判断跳转窗口方式
		String targetStr = "target='_blank'";
		if ("robot".equals(remark)) {
			targetStr = "target='_self'";
		} else if (remark != null && remark.startsWith("wxqrcode")) {
			targetStr = "";
		} else if (remark != null && remark.startsWith("unionall_web_wtz")) {
			targetStr = "target='_blank'";
		}
		//拼接表单
		StringBuilder builder = new StringBuilder();
		builder.append("<form name='form_payment0' action='"+actionUrl+"' method='"+formSubmitMethod+"' "+targetStr+" accept-charset=\"utf-8\">\r\n");
		for(String key : map.keySet()){
			Object value = map.get(key);
			if("yl".equals(remark) && key.length()>0){
				key = key.substring(0,1).toUpperCase()+key.substring(1);
			}
			builder.append("<input type='hidden' name='"+key+"' value='"+value+"' />\r\n");
		}
		builder.append("<input id='submit_payment0' type='submit' value='确认支付'>\r\n");
		builder.append("</form>");
		
		return builder.toString();
		
	}
}