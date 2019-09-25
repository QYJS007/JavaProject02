package work.protec.bus365.store.bus365.other;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Constants {
	
	public static final String coupon_privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK3aX+bArvKZlvt3n6B5WpsQfm2Wjfy6vxtWltv31bgR/GT25yA2yZ9ls4u8oLvJ9YiQnbxkrG2Sg+yJjVHSvNYy8C3bcJT+E8AzDcPE78ihTnrF36vV6m2NmSs9t1cHhn9zwLwvsuJq9fRitKHyiFge3QzomOFJXlExhVdJD9YxAgMBAAECgYEAmBA479G7dDEJUZy5P5jFunn7y4jzWopRW+0p+O7WvbDlrb4UPSYOxuAi4mezw22L2M59WWpJM3IC+CL8PARy7Am0QyuJo6XdNQLaAycYW6Ar3FeqZauaSctjiAlir26j+rQJ5WJpe9OquVHdQyHQivByPqicF2olGzlA4+9bKsECQQDlk250oBZBJmjFtNEoinqb4iFUImsADxzqYciy1cUZyz46ar1uh2HdgRnw0EOv9SkX0GDJ8CNiZxLOW6Omb0h5AkEAwd0KbhOt96MC4xGr7IDNfVcJJQnJ6ZCjd+wYtlca3sTPcO8YDXmp5JatoZcIKCETd+gvb7UyeHslwrqH2s/9eQJBAMbjwDinjEArD2/g46dUuT4dXTRvkPV/IlToHCDa1CQxkRq46J4+CYoF07xuIYXqRgyMiZ3JPk5876z5dUC2fjkCQA+IaWSGeSLlUb7l1JOfgldvmND55s6rAx4Bsb0yX3nH5SMUVSYHQICLPiA7XLDybX/m/54As6mJnG3syiPTFGkCQQCH2thnybK3QsoGMDcFnIEj9wKGe7kz3NjWZIaMhz+FZa0J02tGLTeXd2eOLDTzZtxARPtbHD2YBr5nwrRys6gE";
	public static final String coupon_md5key = "bus365_2014";
	
	/**支付宝服务窗，用户绑定请求地址*/
	/**天气查询url*/
	public static final String WEATHER_URL="http://open.weather.com.cn/data/?";
	/**天气查询key*/
	public static final String WEATHER_PRIVATE_KEY="e55fb7_SmartWeatherAPI_40b6df1";
	/**天气查询appid*/
	public static final String WEATHER_APPID="fd29b3180ab54f69";
	
	/*私钥客户端使用*/
	public static final String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK3aX+bArvKZlvt3n6B5WpsQfm2Wjfy6vxtWltv31bgR/GT25yA2yZ9ls4u8oLvJ9YiQnbxkrG2Sg+yJjVHSvNYy8C3bcJT+E8AzDcPE78ihTnrF36vV6m2NmSs9t1cHhn9zwLwvsuJq9fRitKHyiFge3QzomOFJXlExhVdJD9YxAgMBAAECgYEAmBA479G7dDEJUZy5P5jFunn7y4jzWopRW+0p+O7WvbDlrb4UPSYOxuAi4mezw22L2M59WWpJM3IC+CL8PARy7Am0QyuJo6XdNQLaAycYW6Ar3FeqZauaSctjiAlir26j+rQJ5WJpe9OquVHdQyHQivByPqicF2olGzlA4+9bKsECQQDlk250oBZBJmjFtNEoinqb4iFUImsADxzqYciy1cUZyz46ar1uh2HdgRnw0EOv9SkX0GDJ8CNiZxLOW6Omb0h5AkEAwd0KbhOt96MC4xGr7IDNfVcJJQnJ6ZCjd+wYtlca3sTPcO8YDXmp5JatoZcIKCETd+gvb7UyeHslwrqH2s/9eQJBAMbjwDinjEArD2/g46dUuT4dXTRvkPV/IlToHCDa1CQxkRq46J4+CYoF07xuIYXqRgyMiZ3JPk5876z5dUC2fjkCQA+IaWSGeSLlUb7l1JOfgldvmND55s6rAx4Bsb0yX3nH5SMUVSYHQICLPiA7XLDybX/m/54As6mJnG3syiPTFGkCQQCH2thnybK3QsoGMDcFnIEj9wKGe7kz3NjWZIaMhz+FZa0J02tGLTeXd2eOLDTzZtxARPtbHD2YBr5nwrRys6gE";
	/**网站接口调用服务端解密密钥*/
	public static final String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt2l/mwK7ymZb7d5+geVqbEH5tlo38ur8bVpbb99W4Efxk9ucgNsmfZbOLvKC7yfWIkJ28ZKxtkoPsiY1R0rzWMvAt23CU/hPAMw3DxO/IoU56xd+r1eptjZkrPbdXB4Z/c8C8L7LiavX0YrSh8ohYHt0M6JjhSV5RMYVXSQ/WMQIDAQAB";
	/**网站接口调用生成md5签名额外系数*/
	public static final String md5key = "bus365_2014";
	/**如果android端没有传入errorcode，使用此code,兼容之前没有传入errorcode的版本*/
	public static final String ANDROID_ERROR_CODE = "ANDROID-000";
	/**用户短信激活码在后台redis缓存失效时间*/
	public static final int SMS_ACTIVATION_CODE_LIFE = 8;
	
	/*    用户主题功能点码值      */
	public static final String FUNCTIONCODE_USER_REGIST = "210001";
	public static final String FUNCTIONCODE_USER_RESETPWD = "210002";
	public static final String FUNCTIONCODE_USER_UPDATEPWD = "210003";
	public static final String FUNCTIONCODE_USER_BINDINGPHONE = "210004";
	
	public static final String FUNCTIONCODE_USER_BINDINGEMAIL = "210005";
	public static final String FUNCTIONCODE_USER_UPDATEUSER = "210006";
	public static final String FUNCTIONCODE_USER_SENDACTIVECODE = "210007";
	public static final String FUNCTIONCODE_USER_ACTIVEPHONE = "210008";
	
	public static final String FUNCTIONCODE_USER_ACTIVEEMAIL = "210009";
	public static final String FUNCTIONCODE_USER_UPDATEGETWAY = "210010";
	
	
	/*    乘车人主题功能点码值      */
	public static final String FUNCTIONCODE_PASSENGER_MANAGE_ADD = "220001";
	public static final String FUNCTIONCODE_PASSENGER_MANAGE_UPDATE = "210002";
	public static final String FUNCTIONCODE_PASSENGER_MANAGE_DEL = "210003";
	public static final String FUNCTIONCODE_PASSENGER_CREATEORDER_ADD = "210004";
	public static final String FUNCTIONCODE_PASSENGER_CREATEORDER_UPDATE = "210005";
	
	
	/*   订单主题功能点码值      */
	public static final String FUNCTIONCODE_ORDER_CREATEORDER = "230001";
	public static final String FUNCTIONCODE_ORDER_CANCELORDER = "230002";
	public static final String FUNCTIONCODE_ORDER_REFUNDORDER = "230003";
	public static final String FUNCTIONCODE_ORDER_SELLRESULT = "230004";
	
	
	/*    订单详情主题功能点码值      */
	public static final String FUNCTIONCODE_ORDERDETAIL_CREATEORDER = "240001";
	public static final String FUNCTIONCODE_ORDERDETAIL_CANCELORDER = "240002";
	public static final String FUNCTIONCODE_ORDERDETAIL_SELLRESULT = "240003";
	
	public static int yes=1;
	public static int no=0;
	
	/**网站购票AB方案，缓存key前缀*/
	public static final String ORDER_KEY_PREFIX = "create_";
	/**数字正则表达式*/
	public static final Pattern numberPattern = Pattern.compile("^[-|1-9][0-9]*");
	/**手机号码正则表达式*/
	public static final Pattern phonePattern = Pattern.compile("^([\\+][0-9]{1,3}([ \\.\\-]))?([\\(]{1}[0-9]{2,6}[\\)])?([0-9 \\.\\-/]{3,20})((x|ext|extension)[ ]?[0-9]{1,4})?$");
	
	public static Map<String, String> regexmap = new HashMap<String, String>();
	public static Map<String, String> provincemap = new HashMap<String, String>();
	public static Map<String, String> tickettypemap = new HashMap<String, String>();
	
	
}
