package cg.baidu;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test1 {

	public static void main(String[] args) {
		//System.out.println("sdsd");

		//4、测试

		//测试转换坐标方法 
		//40.060237 ,116.308352 

		String longitude = "40.060237";
		String latitude = "116.308352";
		String proxyAddress = "0.0.0.0";
		String proxyPort = "0";
		String ak = "6ym09CKz8aYevgEd3e4nZaLikMg51SmL";
		JSONObject coordinate;
		try {
			coordinate = GetAddressByLatitudeAndLongitude.convertCoordinate(longitude, latitude, proxyAddress, proxyPort, ak);
			JSONArray jsonArray = coordinate.getJSONArray("result");//retData下为一个JSONArray,只有一个数据  

			longitude = jsonArray.getJSONObject(0).getString("x");
			latitude = jsonArray.getJSONObject(0).getString("y");

			System.out.println(jsonArray);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}





		//测试根据坐标获取地址方法

		/*	JSONObject obj;
		try {
			obj = GetAddressByLatitudeAndLongitude.getAddress(longitude, latitude, proxyAddress, proxyPort, ak);
			if( obj != null){
				String district = obj.getJSONObject("result").getJSONObject("addressComponent").getString("district");


			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// json 内容为 {"status":0,"result":{"location":{"lng":96.32298699999997,"lat":39.98342407140365},"formatted_address":"甘肃省酒泉市瓜州县","business":"","addressComponent":{"country":"中国","country_code":0,"province":"甘肃省","city":"酒泉市","district":"瓜州县","adcode":"620922","street":"","street_number":"","direction":"","distance":""},"pois":[],"poiRegions":[],"sematic_description":"","cityCode":37}}

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
