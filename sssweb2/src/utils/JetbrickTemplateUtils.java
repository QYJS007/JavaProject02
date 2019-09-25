package utils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;


public class JetbrickTemplateUtils {
	
	// 1.获得引擎对象
	private static JetEngine engine = JetEngine.create();
	
	/**
	 * 渲染模板
	 * @author likaihao
	 * @date 2016年7月16日 下午1:11:21
	 * @param templetStr
	 * @param paramMap
	 * @return
	 */
	public static String render(String templetStr, Map<String,Object> paramMap){
		//控制格式,将#if和 #for之前的空格删除
		if(templetStr.contains("#if") || templetStr.contains("#for")){
			templetStr = templetStr.replaceAll("(^|[\r\n])[\t　   ]+#(if|for|end|else|#)", "$1#$2");
		}
		
		// 2. 获取模板对象
		JetTemplate template = engine.createTemplate(templetStr);
		// 3. 渲染模板
		StringWriter writer = new StringWriter();
		template.render(paramMap, writer);
		String result = writer.toString();
		
		//控制格式,替换#b(如果碰到#b,去掉本身和前一个空白字符,空格除外)
		String[] spaceArr = {"\t","\r","\n"};
		if(result.contains("#b")){
			StringBuilder builder = new StringBuilder(result);
			//查找#b
			int index = -1;
			while( (index = builder.indexOf("#b"))!=-1 ){
				//去除本身
				builder.delete(index, index+2);
				//去除上一个空白字符
				List<Integer> indexList = new ArrayList<Integer>();
				for(String spaceStr : spaceArr){
					indexList.add(builder.lastIndexOf(spaceStr,index-1));
				}
				Collections.sort(indexList);
				int lastIndex = indexList.get(indexList.size()-1);
				if(lastIndex!=-1){
					//如果删除的是\n,则判断前一个是不是\r,如果是,一块删除
					if(builder.charAt(lastIndex)=='\n' && builder.charAt(lastIndex-1)=='\r'){
						builder.delete(lastIndex-1, lastIndex+1);
					}else{
						builder.deleteCharAt(lastIndex);
					}
				}
			}
			result = builder.toString();
		}
		return result;
	}
}