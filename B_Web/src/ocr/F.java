package ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;


public class F {
	public static void main(String[] args) {
		//获取本地的绝对路径图片
		File file = new File("C:\\Users\\Administrator\\Desktop\\1234.png");
		//进行BASE64位编码
		String imageBase = BASE64.encodeImgageToBase64(file);
		imageBase = imageBase.replaceAll("\r\n","");
		imageBase = imageBase.replaceAll("\\+","%2B");
		long t1 = System.currentTimeMillis();
		//System.out.println(System.currentTimeMillis());
		
		//百度云的文字识别接口,后面参数为获取到的token
		String httpUrl=" https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token="+baiduOcr.getAuth();
		String httpArg = "detect_direction=true&id_card_side=front&image="+imageBase;
		String jsonResult = request(httpUrl, httpArg);
		System.out.println("返回的结果--------->"+jsonResult);
		HashMap<String, String> map = getHashMapByJson(jsonResult);
		Collection<String> values=map.values();
		Iterator<String> iterator2=values.iterator();
		while (iterator2.hasNext()){
			System.out.print(iterator2.next()+", ");
		}
		//Set<String> keySet = map.keySet();
		
		Set<Entry<String,String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			entry.getKey();
		}
		
		
		long t2 = System.currentTimeMillis();
		System.out.println("用时："+(t2-t1)/1000+ "秒");
	}

	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			//用java JDK自带的URL去请求
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//设置该请求的消息头
			//设置HTTP方法：POST
			connection.setRequestMethod("POST");
			//设置其Header的Content-Type参数为application/x-www-form-urlencoded
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", "uml8HFzu2hFd8iEG2LkQGMxm");
			//将第二步获取到的token填入到HTTP header
			connection.setRequestProperty("access_token", baiduOcr.getAuth());
			connection.setDoOutput(true);
			connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
			connection.connect();
			java.io.InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public static HashMap<String,String> getHashMapByJson(String jsonResult){
		HashMap<String, String> map = new HashMap<String,String>();
		try {
			JSONObject jsonObject = new JSONObject(jsonResult);
			JSONObject words_result= jsonObject.getJSONObject("words_result");
			Iterator<String> it = words_result.keys();
			while (it.hasNext()){
				String key = it.next();
				JSONObject result = words_result.getJSONObject(key);
				String value=result.getString("words");
				switch (key){
				case "姓名":
					map.put("name",value);
					break;
				case "民族":
					map.put("nation",value);
					break;
				case "住址":
					map.put("address",value);
					break;
				case "公民身份号码":
					map.put("IDCard",value);
					break;
				case "出生":
					map.put("Birth",value);
					break;
				case "性别":
					map.put("sex",value);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
