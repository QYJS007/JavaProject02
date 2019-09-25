package work.protec.bus365.store.bus365;

import java.util.HashMap;
import java.util.Map;

import work.protec.bus365.store.bus365.other.RSAUtil;
import utils.AlgorithmUtils;
import utils.HttpUtils;
import utils.JsonUtils;

public class QtripBP {
private static Map<String,String> drivertokenMap = new HashMap<String,String>();//drivertoken,登录时为空
	
	//获得司机端token
	public String get_driver_token(String subSite,String driverPhone,String password){
		Map<String,String> tokenMap = new HashMap<String,String>();
		tokenMap.put("clienttype", "web");
		tokenMap.put("clienttoken", get_driver_driverToken(subSite,driverPhone,password));
		tokenMap.put("ordertoken", "123");
		return JsonUtils.parseObject(tokenMap, null);
	}
	
	//获得bus365的usertoken
	public String get_bus365_token(String clienttype){
		return get_bus365_token(clienttype,null);
	}
	
	//获得bus365的usertoken
	public String get_bus365_token(String clienttype,String subSite){
		//获取:由于需要userid, 所以可以考虑 登录后 获取名称为5fe845d7c136951446ff6a80b8144467的cookie
		//测试:ABB7CA729D2C2A883589F8A3645BAB4495CD408E5DC376C225C87577541016EB263CEB5A7CFA027D35E47519F4AAF3DE3960957A0E40366C46FFB5DF98B5A1C9BE67D12BA8E7E1FAD7CF47CD4A8D9B93
		//return "1368CEBD7128BC48957454397158A3D99660C6D137523304E195A41664252ED730FAE42566B8DEB8DEB4BFDFDF767C42A4D6877FD2041AA6686F374E127B173DA089FAEC25AFEE98F057BAFED0D7B2F1";
		Map<String,String> tokenMap = new HashMap<String,String>();
		tokenMap.put("clienttype", clienttype);
		tokenMap.put("ordertoken", "123");
		if(subSite!=null && subSite.contains("3.34")){
			tokenMap.put("clienttoken", "5639D49E51F39B906D8839B96121D6D21DA907E2E7501C7F426DAAFE4007BA1DE829BE29B57007C7350496DD97521E7308B9D53F1DAB6711AA03621ECDF6FF9D1245FE6D0191DF0BA4F00F0C9A530963");
		}else{
			tokenMap.put("clienttoken", "7A4D2543F258FD7062F842ADD950496ADEADB75BEE76FFBD2B153AD5F2BFEE82501EA1C26CF70C6F0C0056DEB6F44E5DDFC669FD6A01E72ACF5FB7273D722B51A089FAEC25AFEE98F057BAFED0D7B2F1");
		}
		return JsonUtils.parseObject(tokenMap, null);
	
	}
	
	//获得司机端Token私有方法
	private String get_driver_driverToken(String subSite,String driverPhone,String password){
		String drivertoken = drivertokenMap.get(subSite+"_"+driverPhone);
		if(drivertoken!=null){
			return drivertoken;
		}
		//登录
		String jsonStr = null;
		try {
			password = AlgorithmUtils.byteToHexStr(RSAUtil.encrypt(RSAUtil.getKeyPair().getPublic(), password.getBytes()));
			//password = AlgorithmUtils.parseBytesHexStr(RSAUtil.encrypt(RSAUtil.getKeyPair().getPublic(), password.getBytes()));
			String url = subSite+"/specialcar/driverlogin0";
			Map<String,String> paramMap = new HashMap<String,String>();
			
			Map<String,String> tokenMap = new HashMap<String,String>();
			tokenMap.put("clienttype", "web");
			tokenMap.put("clienttoken", "");
			tokenMap.put("ordertoken", "123");
			
			paramMap.put("token", JsonUtils.parseObject(tokenMap, null));
			paramMap.put("phone", driverPhone);
			paramMap.put("password", password);//十六进制 
			jsonStr = HttpUtils.sendHttpPost(url, paramMap, null);
			drivertoken = JsonUtils.parseJson2Map(jsonStr, String.class).get("drivertoken");
			drivertokenMap.put(subSite+"_"+driverPhone, drivertoken);
			return drivertoken;
		} catch (Exception e) {
			throw new RuntimeException("登录失败,返回内容:"+jsonStr);
		}
	}
	
}
