package cg.baidu;

import java.io.IOException;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

public class Test02 {

	public static void main(String[] args) {
		
		String str = "{\"status\":0,\"result\":[{\"x\":40.060237,\"y\":116.308352}]}";
		//System.out.println("str:"+str);
		
		//JSONObject fromObject = JSONObject.fromObject(str);
		//ObjectMapper om = new ObjectMapper();
		try {
			//String writeValueAsString = om.writeValueAsString(str);
			
			JSONObject map  = (JSONObject) parseJson2Map(str);
			//obj = (JSONObject)
			System.out.println(map);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		//System.out.println(fromObject);
	}
	
	/* * @描述：将json解析为Map
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param string 要解析的json字符串
	 * @param filterMap 过滤器的Map
	 * @return Map
	 */
	public static Map parseJson2Map(String string ,Map<String, String[]> filterMap) throws Exception{
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
			for (Map.Entry<String, String[]> mapItem : filterMap.entrySet()) {
				filter.addFilter(mapItem.getKey(), SimpleBeanPropertyFilter.filterOutAllExcept(mapItem.getValue()));
			}
			mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
			mapper.setFilters(filter);
			return mapper.readValue(string, Map.class);
		} catch (Exception e) {
			throw new  Exception(e.getMessage());
		}
	}
	
	/**
	 * @描述：将json解析为Map
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
