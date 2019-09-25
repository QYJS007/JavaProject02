package work.store.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.DateUtils;
import utils.StringUtils;
import utils.TempletUtils;

public class StringHandlePubStore {
	
	/**
	 * 获得今天的日期部分
	 * @author likaihao
	 * @date 2016年10月27日 下午1:58:42
	 * @return
	 */
	public String getNewDateStr(){
		return DateUtils.dateToString(new Date(), "yyyy-MM-dd");
	}
	
	/**
	 * 获得当前的日期时间部分
	 * @author likaihao
	 * @date 2016年10月27日 下午1:58:42
	 * @return
	 */
	public String getNewDateTimeStr(){
		return DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 获得以后时间的日期部分
	 * @author likaihao
	 * @date 2016年11月24日 下午2:55:08
	 * @param diff 与今天相差的天数
	 * @return
	 */
	public String getNextDateStr(int diff){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, diff);
		Date date = c.getTime();
		return DateUtils.dateToString(date, "yyyy-MM-dd");
	}
	
	/**
	 * 打印所有编码的字符串
	 * @param str
	 */
	public String printAllEncodingStr(String str){
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("utf-8\t\t-->\tgbk:\t\t"+new String(str.getBytes("utf-8"),"gbk")).append("\r\n");
			builder.append("utf-8\t\t-->\tiso8859-1:\t"+new String(str.getBytes("utf-8"),"iso8859-1")).append("\r\n");
			builder.append("gbk\t\t-->\tutf-8:\t\t"+new String(str.getBytes("gbk"),"utf-8")).append("\r\n");
			builder.append("gbk\t\t-->\tiso8859-1:\t"+new String(str.getBytes("gbk"),"iso8859-1")).append("\r\n");
			builder.append("iso8859-1\t-->\tgbk:\t\t"+new String(str.getBytes("iso8859-1"),"gbk")).append("\r\n");
			builder.append("iso8859-1\t-->\tutf-8:\t\t"+new String(str.getBytes("iso8859-1"),"utf-8")).append("\r\n");
//			System.out.println("utf-8\t\t-->\tgbk:\t\t"+new String(str.getBytes("utf-8"),"gbk"));
//			System.out.println("utf-8\t\t-->\tiso8859-1:\t"+new String(str.getBytes("utf-8"),"iso8859-1"));
//			System.out.println("gbk\t\t-->\tutf-8:\t\t"+new String(str.getBytes("gbk"),"utf-8"));
//			System.out.println("gbk\t\t-->\tiso8859-1:\t"+new String(str.getBytes("gbk"),"iso8859-1"));
//			System.out.println("iso8859-1\t-->\tgbk:\t\t"+new String(str.getBytes("iso8859-1"),"gbk"));
//			System.out.println("iso8859-1\t-->\tutf-8:\t\t"+new String(str.getBytes("iso8859-1"),"utf-8"));
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 替换字符串
	 * @author likaihao
	 * @date 2015年11月16日 下午6:37:18
	 * @param str
	 * @param re1
	 * @param re2
	 * @param matchCase
	 * @return
	 */
	public String replace(String str,String re1,String re2,boolean matchCase){
		//将\\r\\n替换为\r\n
		//re1 = re1.replaceAll("\\\\([rnt])", "\\\\$1");
		re2 = re2.replaceAll("[^\\\\]\\\\r", "\r").replaceAll("[^\\\\]\\\\n", "\n").replaceAll("[^\\\\]\\\\t", "\t");
		
		Pattern pattern = null;
		if(matchCase){
			pattern = Pattern.compile(re1);
		}else{
			pattern = Pattern.compile(re1, Pattern.CASE_INSENSITIVE);
		}
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll(re2);
	}
	
	/**
	 * 将字符串转换为 url后的参数形式
	 * @author likaihao
	 * @date 2016年12月26日 下午6:02:18
	 * @param str
	 * @return
	 */
	public String getUrlParamByStr(String paramStr){
		//获取参数
		Map<String,Object> baseParamMap = TempletUtils.getBaseParamMap();
		//获取参数
		Map<String,String> paramMap = TempletUtils.getParamStrMap(paramStr,baseParamMap,null);
		
		StringBuilder builder = new StringBuilder();
		for(String name : paramMap.keySet()){
			builder.append(name + "=" + paramMap.get(name) + "&");
		}
		if(builder.length()>0){
			builder.deleteCharAt(builder.length()-1);
		}
		return builder.toString();
	}
	
	/**
	 * 排序
	 * @param str
	 * @return
	 */
	public String sort(String str){
		String[] arr = str.split("[\r\n]+");
		Arrays.sort(arr);
		return StringUtils.join(arr, "\r\n");
	}
	
	/**
	 * 排序并去除重复
	 * @param str
	 * @return
	 */
	public String sort_distinct(String str){
		String[] arr = str.split("[\r\n]+");
		return StringUtils.join(new TreeSet<String>(Arrays.asList(arr)), "\r\n");
	}
	
	/**
	 * 获得重复值
	 * @param str
	 * @return
	 */
	public String getDouble(String str){
		String[] arr = str.split("[\r\n]+");
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(arr));
		Set<String> set = new TreeSet<String>(list);
		//list删除一份,则重复的会剩下一份
		for(String s : set){
			for(int i=0;i<list.size();i++){
				if(list.get(i).equals(s)){
					list.remove(i);
					break;
				}
			}
		}
		return StringUtils.join(new TreeSet<String>(list), "\r\n");
	}
	
	/**
	 * 获取没有重复的值
	 * @param str
	 * @return
	 */
	public String getNotDouble(String str){
		String[] arr = str.split("[\r\n]+");
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(arr));
		
		//获取重复的
		String doubleStr = getDouble(str);
		String[] doubleArr = doubleStr.split("[\r\n]+");
		ArrayList<String> doubleList = new ArrayList<String>(Arrays.asList(doubleArr));
		
		//去除所有重复值,剩下不重复的
		list.removeAll(doubleList);
		
		return StringUtils.join(new TreeSet<String>(list), "\r\n");
	}
	
	/**
	 * 将\t填充为空格
	 * @author likaihao
	 * @date 2016年10月21日 上午9:08:26
	 * @param content 内容
	 * @param num \t最多转换的空格数
	 * @return 转换后的内容
	 */
	public String ltToSpace(String content, int num){
		try {
			String spaceStr = "        ";
			
			//匹配\t
			StringBuffer buffer = new StringBuffer();
			Matcher matcher = Pattern.compile("\t").matcher(content);
			int lastEndIndex = 0;
			while(matcher.find()){
				int startIndex = matcher.start();
				int endIndex = matcher.end();
				
				//计算前边占用的字符数(字节数,汉字占两个字符位置)
				String subStr = content.substring(lastEndIndex, startIndex);
				if(subStr.contains("\n")){
					//如果中间包含换行,则只取最后一行,前边的行不计数
					String[] arr = subStr.split("\n",9999999); //9999999为保留最后的空字符串
					subStr = arr[arr.length-1];
				}
				int subLength = subStr.getBytes("gbk").length;
				int addLength = num - subLength%num;
				String addStr = spaceStr.substring(0,addLength);
				//添加前边的内容 和 要添加的空格
				matcher.appendReplacement(buffer, addStr);
				
				//记录这一次的位置
				lastEndIndex = endIndex;
			}
			//添加剩余的内容
			matcher.appendTail(buffer);
			return buffer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//通过href列表打印js语句(可在浏览器打开全部页面)
	public String getJSByHrefList(String str){
		List<String> list = new ArrayList<String>();
		String[] arr = str.split("[\r\n]+");
		for(String href : arr){
			if(href.startsWith("http://")){
				list.add(href);
			}
		}
		return getJSByHrefList(list);
	}
	
	//通过href列表打印js语句(可在浏览器打开全部页面)
	private String getJSByHrefList(List<String> list){
		StringBuilder builder = new StringBuilder();
		builder.append("(function(){");
		for (String href : list) {
			builder.append("window.open(\"" + href + "\");");
		}
		builder.append("})();");
		return builder.toString();
	}
	
		
}