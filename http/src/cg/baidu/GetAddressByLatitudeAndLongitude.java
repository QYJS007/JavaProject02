package cg.baidu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;



import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetAddressByLatitudeAndLongitude {
	/**
	 * 根据提供的经纬度，代理地址、端口号和ak获取地址
	 * @param longitude:经度
	 * @param latitude：纬度
	 * @param proxyAddress：代理地址(有的公司网络是有代理的，这时候如果不设置则会出现连接超时的异常)
	 * @param proxyPort：代理端口
	 * @param ak：秘钥
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getAddress(String longitude, String latitude, String proxyAddress, String proxyPort, String ak) throws Exception {  
		URL url = new URL("http://api.map.baidu.com/geocoder/v2/?ak=" + ak + "&location=" + latitude  + "," + longitude + "&output=json");  
		JSONObject object = getJSONObjectByUrl(url,proxyAddress,proxyPort);
		// 返回的json串格式 {"status":0,"result":{"location":{"lng":96.32298699999997,"lat":39.98342407140365},"formatted_address":"甘肃省酒泉市瓜州县","business":"","addressComponent":{"country":"中国","country_code":0,"province":"甘肃省","city":"酒泉市","district":"瓜州县","adcode":"620922","street":"","street_number":"","direction":"","distance":""},"pois":[],"poiRegions":[],"sematic_description":"","cityCode":37}}
		return object;
	} 


	/**
	 * 根据参数将非百度坐标转换为百度坐标，供getAddress方法使用
	 * @param longitude
	 * @param latitude
	 * @param proxyAddress
	 * @param proxyPort
	 * @param ak
	 * @return
	 * @throws Exception
	 */
	public static JSONObject convertCoordinate(String longitude, String latitude, String proxyAddress, String proxyPort, String ak) throws Exception{

		URL url = new URL("http://api.map.baidu.com/geoconv/v1/?coords="+ longitude + "," + latitude +"&from=1&to=5&ak="+ak);

		JSONObject object = getJSONObjectByUrl(url,proxyAddress,proxyPort);


		//返回的json格式 {"status":0,"result":[{"x":114.23074952312,"y":29.57908262908}]}


		return object;

	}

	/**
	 * 根据不同的url获取不同的json串
	 * @param url
	 * @param proxyAddress
	 * @param proxyPort
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJSONObjectByUrl(URL url, String proxyAddress, String proxyPort) throws Exception{

		HttpURLConnection connection = null;
		//如果代理地址没有或是代理的端口号为0，则说明该网络不存在代理
		if("notFound".equals(proxyAddress) || "0".equals(proxyPort)){
			connection = (HttpURLConnection) url.openConnection();
		}else{
			@SuppressWarnings("static-access")
			Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
			connection = (HttpURLConnection) url.openConnection(proxy); 
		}

		connection.addRequestProperty("User-Agent", "Mozilla/4.0");
		/** 
		 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。 
		 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做： 
		 */  
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);  
		connection.setUseCaches(false); 
		connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");  
		out.flush();  
		out.close();  
		//一旦发送成功，用以下方法就可以得到服务器的回应：  
		String res;  
		InputStream l_urlStream;  
		l_urlStream = connection.getInputStream();  
		BufferedReader in = new BufferedReader(new InputStreamReader( l_urlStream,"UTF-8"));  
		StringBuilder sb = new StringBuilder("");  
		while ((res = in.readLine()) != null) {  
			sb.append(res.trim());  
		}  
		String str = sb.toString();  
		JSONObject obj = null;

		//StringUtils.isBlank(str)判断转化后的str是否为空字符串
		//if(!StringUtils.isBlank(str)){
		if(null!=str){

			//将str转换为json
			//JSONArray.fromObject(str);
			//System.out.println(str);
			//str ="{\"status\":0,\"result\":{\"x\":40.060237,\"y\":116.308352}}";
			//obj = JSONObject.fromObject(str);  
			  Map map = parseJson2Map(str);
		}
		return obj;   
	} 

	/** @描述：将json解析为Map
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param string 要解析的json字符串
	 * @return Map
	 */
	public static Map parseJson2Map(String string) throws Exception{
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			return mapper.readValue(string, Map.class);
		} catch (Exception e) {
			throw new  Exception(e.getMessage());
		}
	}
}
