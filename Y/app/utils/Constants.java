package utils;

import conf.ResourceConfig;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	/**AES加解密密钥*/
	public static final String AES_KEY;
	
	/**后台管理调用服务端接口加密密钥*/
	public static final String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK3aX+bArvKZlvt3n6B5WpsQfm2Wjfy6vxtWltv31bgR/GT25yA2yZ9ls4u8oLvJ9YiQnbxkrG2Sg+yJjVHSvNYy8C3bcJT+E8AzDcPE78ihTnrF36vV6m2NmSs9t1cHhn9zwLwvsuJq9fRitKHyiFge3QzomOFJXlExhVdJD9YxAgMBAAECgYEAmBA479G7dDEJUZy5P5jFunn7y4jzWopRW+0p+O7WvbDlrb4UPSYOxuAi4mezw22L2M59WWpJM3IC+CL8PARy7Am0QyuJo6XdNQLaAycYW6Ar3FeqZauaSctjiAlir26j+rQJ5WJpe9OquVHdQyHQivByPqicF2olGzlA4+9bKsECQQDlk250oBZBJmjFtNEoinqb4iFUImsADxzqYciy1cUZyz46ar1uh2HdgRnw0EOv9SkX0GDJ8CNiZxLOW6Omb0h5AkEAwd0KbhOt96MC4xGr7IDNfVcJJQnJ6ZCjd+wYtlca3sTPcO8YDXmp5JatoZcIKCETd+gvb7UyeHslwrqH2s/9eQJBAMbjwDinjEArD2/g46dUuT4dXTRvkPV/IlToHCDa1CQxkRq46J4+CYoF07xuIYXqRgyMiZ3JPk5876z5dUC2fjkCQA+IaWSGeSLlUb7l1JOfgldvmND55s6rAx4Bsb0yX3nH5SMUVSYHQICLPiA7XLDybX/m/54As6mJnG3syiPTFGkCQQCH2thnybK3QsoGMDcFnIEj9wKGe7kz3NjWZIaMhz+FZa0J02tGLTeXd2eOLDTzZtxARPtbHD2YBr5nwrRys6gE";
	public static final String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt2l/mwK7ymZb7d5+geVqbEH5tlo38ur8bVpbb99W4Efxk9ucgNsmfZbOLvKC7yfWIkJ28ZKxtkoPsiY1R0rzWMvAt23CU/hPAMw3DxO/IoU56xd+r1eptjZkrPbdXB4Z/c8C8L7LiavX0YrSh8ohYHt0M6JjhSV5RMYVXSQ/WMQIDAQAB";
	/**后台管理调用服务器端接口生成md5签名额外系数*/
	public static final String md5key = "bus365_2014";
	
	public static Map<String, String> regexmap = new HashMap<String, String>();
	
	static {
		regexmap.put("email_regex",
				"^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");//邮箱地址验证
		regexmap.put("phone_regex", "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$");//手机号码验证
		regexmap.put("id_card_regex", "^\\d{15}(\\d{2}[0-9xX])?$");//身份证号验证
		
		regexmap.put("chinese_regex", "^[\u4e00-\u9fa5]+$"); // 匹配汉字
		regexmap.put("letter_regex", "^[A-Za-z]+$"); // 匹配英文字符串
		regexmap.put("space_regex", "^(\\s|.*\\s+.*)$"); // 匹配有空格或TAB或换行的字符串
		regexmap.put("password_regex", "^[a-zA-Z0-9]{6,8}$"); // 6-8位字母或数字
		regexmap.put("eincode_regex", "^[A-Z0-9]{15}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$"); // 必须是大写字母和数字的组合，长度为15,18或20位
		
		try{
			AES_KEY = ResourceConfig.getValue("securitykey");
		}catch(Exception e){
			throw new RuntimeException("read property 'securitykey'  failed.");
		}
	}
	/**发送监管平台kafka消息主题 qtrip_ToSupervision*/
	public static final String KAFKATOPIC_SENDTOPLATFORM = "qtrip_ToSupervision";
	/**是否发送监管平台kafka平台参数 300003*/
	public static final String DICTIONARY_ISSENDTOPLATFORMCODE = "300003";
}
