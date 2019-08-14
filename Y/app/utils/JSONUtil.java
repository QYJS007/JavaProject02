package utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import play.Logger;

public class JSONUtil {
	public enum JSON_TYPE{
        /**JSONObject*/    
        JSON_TYPE_OBJECT,
        /**JSONArray*/
        JSON_TYPE_ARRAY,
        /**不是JSON格式的字符串*/
        JSON_TYPE_ERROR
    }
	/**
	 * @描述：将对象转换为json
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param object 要转换的对象
	 * @return json字符串
	 */
	public static String parseObject(final Object object) {
		ObjectMapper om=new ObjectMapper();
		String ret=null;
		try {
			om.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			ret=om.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String parseObject(String returntype,final Object object) throws Exception{
		ObjectMapper om=new ObjectMapper();
		if("1".equals(returntype)){
			om.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
		}
		String ret = om.writeValueAsString(object);
		return ret;
	}
	
	/**
	 * @描述：将对象转换为json
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param object 要转换的对象
	 * @return json字符串
	 */
	public static String parseObject(final Object object,boolean isNumberToString) {
		ObjectMapper om=new ObjectMapper();
		String ret=null;
		try {
			om.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			
			if(isNumberToString){
				om.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
			}
			/*//临时解决方案，null转为字符串（数值类型 现在也会转，后期找解决方案）
			om.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()  
		        {  
					@Override
					public void serialize(Object arg0, JsonGenerator arg1,
							SerializerProvider arg2) throws IOException,
							JsonProcessingException {
						
						arg1.writeString("");
					}  
		        }); */
			ret=om.writeValueAsString(object);
		} catch (Exception e) {
			Logger.error(e,"转换json串异常");
		}
		return ret;
	}
	
	/**
	 * @描述：将对象转换为json
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param object 要转换的对象
	 * @param filterMap 过滤器map
	 * @param dateFormat 日期的格式化模式
	 * @return json字符串
	 */
	public static String parseObject(Object object,Map<String, String[]> filterMap,String dateFormat){
		ObjectMapper om = new ObjectMapper();
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		om.setDateFormat(format);
		SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
		for (Map.Entry<String, String[]> mapItem : filterMap.entrySet()) {
			filter.addFilter(mapItem.getKey(), SimpleBeanPropertyFilter.filterOutAllExcept(mapItem.getValue()));
		}
		om.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		om.setFilters(filter);
		String ret=null;
		try {
			ret=om.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
	/**
	 * @描述：将对象转换为json
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param object 要转换的对象
	 * @param filterMap 过滤器map
	 * @param dateFormat 日期的格式化模式
	 * @return json字符串
	 */
	public static String parseObject(Object object,String dateFormat){
		ObjectMapper om = new ObjectMapper();
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		om.setDateFormat(format);
		String ret=null;
		try {
			ret=om.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
	/**
	 * @描述：将对象转换为json
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param object 要转换的对象
	 * @param filterMap 过滤器map
	 * @return json字符串
	 */
	public static String parseObject(Object object,Map<String, String[]> filterMap){
		ObjectMapper om = new ObjectMapper();
		SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
		for (Map.Entry<String, String[]> mapItem : filterMap.entrySet()) {
			filter.addFilter(mapItem.getKey(), SimpleBeanPropertyFilter.filterOutAllExcept(mapItem.getValue()));
		}
		om.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		om.setFilters(filter);
		String ret=null;
		try {
			ret=om.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
	/**
	 * 获取对象的json表达式,忽略对象的空字段
	 * @param object
	 * @return
	 */
	public static String parseObjectIgnoreNull(final Object object,String datePattern){
		String json = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		SimpleDateFormat sf = new SimpleDateFormat(datePattern);
		mapper.setDateFormat(sf);
		try {
			json = mapper.writeValueAsString(object);
		}catch(Exception e) {
			Logger.log4j.error("parseObject Exception.", e);
		}
		return json;
	}
	
	/**
	 * @描述：将json解析为对象
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param jsonstr 要解析的json字符串
	 * @param clazz 目标对象的字节码对象
	 * @return 转换后的对象
	 */
	public static  <T> T readJson2Entity(String jsonstr,Class<T> clazz){
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); 
			om.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	        T acc = om.readValue(jsonstr, clazz);
	        return acc;
	    } catch (JsonParseException e) {
	       Logger.error(e,"json转对象失败,数据：%s",jsonstr);
	    } catch (JsonMappingException e) {
	    	 Logger.error(e,"json转对象失败,数据：%s",jsonstr);
	    } catch (IOException e) {
	    	 Logger.error(e,"json转对象失败,数据：%s",jsonstr);
	    } catch(Exception e){
	    	Logger.error(e,"json转对象失败,数据：%s",jsonstr);
	    }
		return null;
	}
	
	/**
	 * @描述：将json解析为对象
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param jsonstr 要解析的json字符串
	 * @param object 目标对象同类型的对象
	 * @param filterob 过滤器名称
	 * @param specifypro 要转换的字段
	 * @return 转换后的对象
	 */
	public static Object readJson2Entity(String jsonstr , final Object object, final String filterob,final String[] specifypro){
		try {
			ObjectMapper om = new ObjectMapper();
			SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
			filter.addFilter(filterob, SimpleBeanPropertyFilter.filterOutAllExcept(specifypro));
			om.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
			om.setFilters(filter);
	        Object acc = om.readValue(jsonstr, object.getClass());
	        return acc;
	    } catch (JsonParseException e) {
	        e.printStackTrace();
	    } catch (JsonMappingException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * @描述：将json解析为List
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param string 要解析的json字符串
	 * @param x List中元素的字节码文件
	 * @return List
	 */
	public static List<?> parseJson2Collection(String string,Class<?>  x) throws Exception{  
    	ObjectMapper mapper = new ObjectMapper(); 
        JavaType javaType = getCollectionType(List.class, x); 
        List<Class<?>> list =  (List<Class<?>>)mapper.readValue(string, javaType); 
        return list;
    }   
	public static <T> List<T> paseModels(String string,Class<T> x) throws Exception{  
    	ObjectMapper mapper = new ObjectMapper(); 
        JavaType javaType = getCollectionType(ArrayList.class, x); 
        List<T> list =  (List<T>)mapper.readValue(string, javaType); 
        return list;
    }  
	
	/**
	 * 返回传入类型的对象
	 * @author dongchao
	 * @date Aug 5, 2015 8:21:46 PM
	 * @param json 要转义的字符串
	 * @param type eg:HashMap<Stirng,String>
	 * @return
	 * @throws Exception
	 */
	public static <T> T deserialize(String json,TypeReference<T> type) throws Exception{
    	ObjectMapper mapper = new ObjectMapper(); 
    	mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    	mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		return mapper.readValue(json, type);
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
			Logger.error( e, null, "json转Map", false, "发生异常", null, null, string);
			throw e;
		}
	}
	
	/**
	 * @描述：将json解析为Map
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
			Logger.error(e, null, "json转Map", false, "发生异常", null, null, string);
			throw e;
		}
	}
	
	/**
	 * @描述：获取泛型的Collection Type 
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param collectionClass 泛型的Collection   
     * @param elementClasses 元素类   
     * @return JavaType Java类型   
	 */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
    	ObjectMapper mapper = new ObjectMapper(); 
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
    }
    
    /***
     * 
     * 获取JSON类型
     *         判断规则
     *             判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本
     *         
     * @param str
     * @return
     */
    public static JSON_TYPE getJSONType(String str){
        if(CommonUtil.isNotEmptyString(str)){
        	 final char[] strChar = str.substring(0, 1).toCharArray();
             final char firstChar = strChar[0];
                          
             if(firstChar == '{'){
                 return JSON_TYPE.JSON_TYPE_OBJECT;
             }else if(firstChar == '['){
                 return JSON_TYPE.JSON_TYPE_ARRAY;
             }else{
                 return JSON_TYPE.JSON_TYPE_ERROR;
             }
        }else{
        	return JSON_TYPE.JSON_TYPE_ERROR;
        }        
    }
    
    public static JsonNode getJsonNode(String orgstr) throws IOException{
		ObjectMapper om = new ObjectMapper();
        try {
        	JsonNode jn = om.readTree(orgstr);
        	return jn;
        } catch (IOException e) {
            throw e;
        }
	}
    
    /**
	 * @author ldl
	 * 把json字符串转化为Map对象
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static Map paseJson2Map(String string) throws Exception{
		ObjectMapper mapper = new ObjectMapper(); 
		mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return mapper.readValue(string, Map.class);
	}
	
	 /**
	 * @author ldl
	 * 把json字符串转化为Map对象
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap paseJson2LinkedHashMap(String string) throws Exception{
		ObjectMapper mapper = new ObjectMapper(); 
		return mapper.readValue(string, LinkedHashMap.class);
	}
		
    public static String createSendJsonData(String[] filter,List list,String filtername,String facename){
    	if(filter.length>0 && list.size()>0){
    		Map<String, String[]> filterMap = new HashMap<String,String[]>();
    		Map<String, Object> voMap = new HashMap<String,Object>();
    		voMap.put("interfaceName", facename);
    		voMap.put("items", list);
    		filterMap.put(filtername, filter);
    		return parseObject(voMap, filterMap);
    	}else{
    		return null;
    	}
    }
    public static String createSendJsonData(List list,String facename){
    	if(list.size()>0){
    		Map<String, Object> voMap = new HashMap<String,Object>();
    		voMap.put("interfaceName", facename);
    		voMap.put("items", list);
    		return parseObject(voMap);
    	}else{
    		return null;
    	}
    }
}

