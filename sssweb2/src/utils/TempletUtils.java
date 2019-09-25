package utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sys.SystemConf;

public class TempletUtils {
	
	private static final String format = "\\{:.+?\\}";//占位符格式
	//static final String format = "\\$\\{.*+\\}";//占位符格式
	
	/**
	 * 表达式引擎使用ognl
	 * 模板引擎使用jetbrick-template
	 * 即: 多行模板替换中参数使用ognl规则,模板使用jetbrick-template规则, 其余功能均使用ognl规则
	 * 占位符: 使用ognl表达式的地方使用{:xxx}作为占位符, 使用jetbrick-template规则的地方使用${xxx}作为占位符
	 * 
	 * 
	 * 单行模板替换:
	 * 		将每一行作为一个数据源,对一个单行模板进行替换,每一行产生一行新数据(模板只有一行,批量替换)
	 * 		表达式引擎使用ognl
	 * 		单行文本替换中可以使用{:_linenum}变量, 表示当前行号
	 * 多行文本替换:
	 * 		将全部行作为一个模板,用指定外部数据进行替换(模板有多行,只替换一次)
	 * 		模板引擎使用jetbrick-template, 但参数部分使用ognl进行计算
	 * 
	 */
	
	/**
	 * 获得占位符样式
	 * @author likaihao
	 * @date 2015年9月18日 上午10:25:37
	 * @return
	 */
	public static String[] getTag(){
		String[] arr = format.split("\\.\\+\\?");
		for(int i=0;i<arr.length;i++){
			arr[i] = arr[i].replace("\\{", "{").replace("\\}", "}").replace("\\$","$");
		}
		return arr;
	}
	
	/**
	 * 获得占位符的内容
	 * @author likaihao
	 * @date 2015年9月18日 上午10:30:10
	 * @param str
	 * @return
	 */
	public static String getStrTagContent(String str){
		String[] tagArr = getTag();
		return str.substring(tagArr[0].length(),str.length()-tagArr[1].length());
	}
	
	/**
	 * 获得字符串 添加占位符后的内容
	 * @author likaihao
	 * @date 2015年9月18日 上午10:32:18
	 * @param str
	 * @return
	 */
	public static String getStrAddTag(String str){
		String[] tagArr = getTag();
		return tagArr[0]+str+tagArr[1];
	}
	
	/**
	 * 模板提取(一行),从字符串中提取模板需要的值
	 * @author likaihao
	 * @date 2015年8月19日 下午2:19:28
	 * @param str 字符串
	 * @param templet 模板
	 * @return 模板的name和value
	 */
	public static Map<String,Object> templetExtractOneLine(String str,String templet){
		//str: drivertoken	String	Y	token
		//templet: {:name}	{:type}	{:notnull}	{:disc}
		String templetPattern = "^"+templet.replaceAll(format, "(.+?)")+"$";//^(.+?) (.+?) (.+?) (.+?)$
		
		if(!str.matches(templetPattern)){
			throw new RuntimeException("字符串不符合模式,"+str+" , "+templetPattern);
		}
		
		//从模板中获取名称 name,type,notnull,disc
		List<String> nameList = RegexUtils.getSubstrByRegexReturnList(templet, format.replace(".+?", "(.+?)"));//\{:(.*?)\}
		
		//从内容中获取对应的值 drivertoken,String,Y,token
		List<String> valueList = RegexUtils.getSubstrAllGroupByRegex(str, templetPattern);//^(.+?) (.+?) (.+?) (.+?)$
		
		//键值对 name=drivertoken,type=String,notnull=Y,disc=token
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<nameList.size();i++){
			map.put(nameList.get(i), valueList.get(i+1));
		}
		return map;
	}
	
	/**
	 * 模板提取(一行,批量),从每行字符串中提取模板需要的值
	 * @author likaihao
	 * @date 2015年8月19日 下午2:19:28
	 * @param str 字符串
	 * @param templet 模板
	 * @return 模板的name和value
	 */
	public static List<Map<String,Object>> templetExtractOneLineBatch(List<String> strList,String templet){
		//str: drivertoken	String	Y	token
		//templet: {:name}	{:type}	{:notnull}	{:disc}
		//获取旧模板格式正则
//		String templetPattern = templet.replaceAll(format, "@@@");
//		templetPattern = templetPattern.replaceAll("([()])", "\\\\$1");
//		templetPattern = templetPattern.replaceAll("@@@", "(.*?)");
//		templetPattern = "^"+templetPattern+"$";
		
		//获取旧模板格式正则
		String templetPattern = null;
		try {
			//获取占位符
			Matcher matcher = Pattern.compile(format).matcher(templet);
			int lastEnd = 0;
			StringBuilder builder = new StringBuilder();
			while(matcher.find()){
				//获取 前边的字符串,和占位符中的字符串
				String beforeStr = templet.substring(lastEnd, matcher.start());
				String matcherStr = templet.substring(matcher.start(), matcher.end());
				lastEnd = matcher.end();
				
				//判断占位符中是否 指定了正则
				String[] tagArr = getTag();
				String newRe = RegexUtils.getSubstrByRegex(matcherStr, Pattern.quote(tagArr[0])+"<(.*?)>");
				if(newRe!=null){
					newRe = "(" + newRe + ")";
				}else{
					newRe = "(.*?)";
				}
				
				//添加之前的字符串,和正则
				builder.append(beforeStr.replaceAll("([(){}])", "\\\\$1"));
				builder.append(newRe);
			}
			//添加之后的字符串
			builder.append(templet.substring(lastEnd).replaceAll("([(){}])", "\\\\$1"));
			templetPattern = builder.toString();
			templetPattern = "^"+templetPattern+"$";
		} catch (Exception e) {
			LoggerUtils.error("替换正则失败,templet:"+templet, e);
			throw new RuntimeException(e);
		}
		
		//判断所有字符串是否符合旧模板正则
		for(String str : strList){
			if(!str.matches(templetPattern)){
				throw new RuntimeException("字符串不符合模式,\n@"+str+"@\n@"+templetPattern+"@");
			}
		}
		
		//获取name和value,组成map
		List<String> nameList = RegexUtils.getSubstrByRegexReturnList(templet, format.replace(".+?", "(.+?)"));
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(String str : strList){
			List<String> valueList = RegexUtils.getSubstrAllGroupByRegex(str, templetPattern);
			Map<String,Object> map = new HashMap<String,Object>();
			for(int i=0;i<nameList.size();i++){
				//去掉name之前的<>
				String name = nameList.get(i);
				if(name.startsWith("<")){
					name = name.substring(name.indexOf(">")+1);
				}
				map.put(name, valueList.get(i+1));
			}
			map.put("stringUtils", new StringUtils());
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 模板填充(模板为一行),将模板中指定占位符替换为值(占位符中表达式的规则参见ognl规则)
	 * @author likaihao
	 * @date 2015年8月19日 下午2:01:33
	 * @param templet 模板
	 * @param nameValueMap 要替换的占位符和值
	 * @return 替换后的模板
	 */
	public static String templetFillOneLine(String templet,Map<String,Object> nameValueMap){
		//templet: private {type} {name};
		//nameValueMap: {name=drivertoken, notnull=Y, type=String, disc=token}
		
		//替换表达式
		List<String> nameList = RegexUtils.getSubstrByRegexReturnList(templet, format.replace(".+?", "(.+?)"));
		Set<String> set = new HashSet<String>(nameList);
		for(String name : set){
			Object result = OgnlUtils.getValue(name, nameValueMap);
			String sResult = "";
			if(result!=null){
				sResult = result.toString();
			}
			templet = templet.replace(getStrAddTag(name), sResult);
		}
		return templet;
	}
	
	/**
	 * 模板填充(一行,批量),将模板中指定占位符替换为值,每行替换一次
	 * @author likaihao
	 * @date 2015年8月19日 下午2:01:33
	 * @param templet 模板
	 * @param nameValueMap 要替换的占位符和值
	 * @return 替换后的模板
	 */
	public static List<String> templetFillOneLineBatch(String templet,List<Map<String,Object>> nameValueMapList){
		List<String> newStrList = new ArrayList<String>();
		int _linenum = 1;
		for(Map<String,Object> nameValueMap : nameValueMapList){
			nameValueMap.put("_linenum", _linenum);
			String newStr = templetFillOneLine(templet,nameValueMap);
			newStrList.add(newStr);
			_linenum ++;
		}
		return newStrList;
	}
	
	/**
	 * 填充多行模板
	 * @author likaihao
	 * @date 2016年7月18日 上午9:22:42
	 * @param templetStr
	 * @param paramMap
	 * @return
	 */
	public static String templetFillMultiLine(String templetStr, Map<String,Object> paramMap){
		return JetbrickTemplateUtils.render(templetStr, paramMap);
	}
	
	/**
	 * 计算表达式的值
	 * @author likaihao
	 * @date 2015年9月22日 下午3:31:31
	 * @param expression
	 * @param paramMap
	 * @return
	 */
	public static Object computeEngine(String expression,Map<String,Object> paramMap){
		List<String> list = getAllTwinTagContent(expression, "{:");
		if(list.size()==1 && list.get(0).length() == expression.length()){
			//如果expression是一个占位符,则返回Object
			return OgnlUtils.getValue(getStrTagContent(expression), paramMap);
		}else{
			//如果expression不只是一个占位符,则返回String
			return templetFillOneLine(expression, paramMap);
		}
	}
	
	
	/**
	 * 获得所有指定的成对的标记内容
	 * @param str 源字符串
	 * @param tagStartStr 标记声明部分(<div class="map_main">)
	 * @return 标记内容
	 */
	public static List<String> getAllTwinTagContent(String str,String tagStartStr){
		//确定成对的标签
		String twinStart = "";//成对开始标签
		String twinEnd = "";//成对结束标签
		if(tagStartStr.contains("(")){
			twinStart = "(";
			twinEnd = ")";
		}
		if(tagStartStr.contains("[")){
			twinStart = "[";
			twinEnd = "]";
		}
		if(tagStartStr.contains("{")){
			twinStart = "{";
			twinEnd = "}";
		}
		
		List<String> list = new ArrayList<String>();
		for(;;){
			//查询开始标记
			int index = str.indexOf(tagStartStr);
			if(index==-1){
				break;
			}
			
			//去除第一次开始标签出现之前的字符串
			str = str.substring(index);
			StringBuilder builder = new StringBuilder(str);
			
			//查找结束标记
			int twinStartCount = 0;
			int twinEndCount = 0;
			index = -1;
			while( (index = builder.indexOf(twinEnd))!=-1){
				//判断 开始标签 和 结束标签 是否成对(出现的次数相等)
				String s = builder.substring(0,index+twinEnd.length());//两个结束标签中间的字符串
				twinStartCount += StringUtils.getSubstrExistsCount(s,twinStart);
				twinEndCount++;
				builder.delete(0, s.length());//删除已经查找过的字符串,剩下余下的字符串
				if(twinStartCount == twinEndCount){
					list.add(str.substring(0,str.length()-builder.length()));
					break;
				}
			}
			str = str.substring(str.length()-builder.length());//删除已经查找过的字符串,剩下余下的字符串
			if(index==-1){
				throw new RuntimeException("标记不是成对的:"+twinStart+","+twinEnd);
			}
		}
		return list;
	}
	
	
	//基本的工具类对象的集合,缓存起来
	private static Map<String,Object> baseNameValueMap = null;
	
	/**
	 * 获得参数map,添加了常用的工具类
	 * @author likaihao
	 * @date 2015年11月16日 下午5:41:55
	 * @return
	 */
	public static Map<String,Object> getBaseParamMap(){
		//先判断有没有缓存起来的
		if(baseNameValueMap==null || baseNameValueMap.size()==0){
			//获取service和utils下的所有class文件
			Map<String,Object> nameValueMap = new HashMap<String,Object>();
			String classPath = SystemConf.getProjectClassPath();
			String[] subPathPattern = new String[]{"service/.*","utils/.*","work/store/.*","work/protec/.*"};
			List<File> fileList = IOUtils.getFileListByPattern(classPath, subPathPattern, null, new String[]{".*\\.class",".*\\.java"}, null);
			for(File classFile : fileList){
				String className = classFile.getAbsolutePath().substring(classPath.length()-1).replace("\\", ".").replace("/", ".");
				className = className.substring(0,className.lastIndexOf("."));
				if(className.startsWith(".")){
					className = className.substring(1);
				}
				//去除匿名内部类
				if(className.contains("$")){
					continue;
				}
				//获取变量名称
				//bp.BaseBP --> baseBP
				//bp.JDPayBP --> jdPayBP
				String subClassName = className.substring(className.lastIndexOf(".")+1);
				String start = RegexUtils.getSubstrByRegex(subClassName, "^([A-Z]*)[a-z1-9_]");//开头的大写字母
				if(start!=null && start.length()>0){
					if(start.length()>1){
						//第二个字母大写:JDPayBP
						String start2 = start.substring(0,start.length()-1);
						subClassName = subClassName.replaceFirst(start2, start2.toLowerCase());
					}else{
						//第二个字母小写:BaseBP
						subClassName = StringUtils.firstCharLowerCase(subClassName);
					}
				}
				
				//反射创建工具类的对象
				try {
					//如果直接使用Class.forName(className).newInstance(),异常捕获不住
					Constructor<?> c = Class.forName(className).getDeclaredConstructor();
					if(c!=null){
						nameValueMap.put(subClassName, c.newInstance());
					}
				} catch (Exception e) {
					//System.err.println(getErrMsg(e));
				}
			}
			baseNameValueMap = nameValueMap;
		}
		
		//拷贝一份返回
		return new HashMap<String,Object>(baseNameValueMap);
	}
	
	/**
	 * 将页面传入的参数字符串变为参数map
	 * @author likaihao
	 * @date 2016年7月18日 下午10:07:04
	 * @param paramStr
	 * @param nameValueMap
	 * @param onlyOutList 原样输出的字符串
	 * @return
	 */
	public static Map<String,Object> getParamMap(String paramStr,Map<String,Object> nameValueMap,List<String> onlyOutList){
		/**
		 *  注释
		 *  	开头是//的认为是注释, 末尾是 空格+//后的内容被认为是注释
		 *  字符串
		 *  	str:abc //字符串
		 *  表达式
		 *  	expression:{:5+2} //表达式,表达式使用ognl作为引擎,规则遵循ognl规则
		 *  	map2:{:map} //引用上面声明过的变量
		 *  
		 */
		Map<String,Object> paramMap = new LinkedHashMap<String,Object>();
		if(nameValueMap==null){
			nameValueMap = new HashMap<String,Object>();
		}
		//解析参数
		if(paramStr!=null && paramStr.length()>0){
			String[] tagArr = getTag();
			//按行分割参数(判断后边不直接跟\t的,配合http中的_body等多行参数)
			String[] arr = paramStr.split("\n(?!\t)");
			for(String str : arr){
				//去除注释
				if(str.startsWith("//")){
					continue;
				}
				
				//按:分割键值对
				int index = str.indexOf(":");
				if(index!=-1){
					String name = str.substring(0,index).trim();
					String value = str.substring(index+1);
					
					//去掉后面的注释 
					index = value.indexOf(" //");//去掉后面的注释 
					if(index!=-1){
						value = value.substring(0,index);
					}
					index = value.indexOf(" //");//去掉后面的注释(空格不一样..)
					if(index!=-1){
						value = value.substring(0,index);
					}
					index = value.indexOf("　//");//去掉后面的注释(空格不一样..)
					if(index!=-1){
						value = value.substring(0,index);
					}
					value = value.trim().replace(" ", "");
					
					Object value2 = value;
					//判断是否是需要原样输出的字符串
					if(onlyOutList==null || !onlyOutList.contains(name)){
						//判断是不是表达式,字符串和原样输出的属性不用处理
						if(value.contains(tagArr[0])){
							value2 = computeEngine(value, nameValueMap);
						}
					}
					//添加值
					paramMap.put(name, value2);
					nameValueMap.put(name, value2);
				}
			}
		}
		return paramMap;
	}
	
	/**
	 * 将页面传入的参数字符串变为参数map,返回值为Map<String,String>
	 * @author likaihao
	 * @date 2016年7月18日 下午10:17:45
	 * @param paramStr
	 * @param nameValueMap
	 * @param onlyOutList
	 * @return
	 */
	public static Map<String,String> getParamStrMap(String paramStr,Map<String,Object> nameValueMap,List<String> onlyOutList){
		Map<String,Object> map = getParamMap(paramStr,nameValueMap,onlyOutList);
		Map<String,String> map2 = new LinkedHashMap<String,String>();
		for(String key : map.keySet()){
			if(map.get(key)!=null){
				map2.put(key, map.get(key).toString());
			}
		} 
		return map2;
	}
	
	
}