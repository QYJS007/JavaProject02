package work.protec.bus365.store.bus365;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import utils.OfficeUtils;
import utils.RegexUtils;

public class WordCodeUtils {
	private static Map<String,Map<String,Object>> cacheMap = new HashMap<String,Map<String,Object>>();
	/**
	 * 获取指定表名的word信息
	 * @param tableName 表名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getWordMap(String wordPath,String tableName,boolean reloadWord){
		try {
			/*
			 * 返回值
			 * 	 tableName			String
			 *   tableChineseName	String
			 * 	 primaryKey			String
			 *   indexNameArr		String[]
			 *   columnMapMap		Map<String,Map<String,String>>
			 *     name,type,isNull,isUnique,explain,remark,javaType
			 *     值示例: id varhcar(32) false true 主键 没有备注 String
			 */
			if(!cacheMap.containsKey(wordPath)){
				cacheMap.put(wordPath, new HashMap<String,Object>());
			}
			Map<String,Object> map = (Map<String, Object>) cacheMap.get(wordPath).get(tableName);
			if(!reloadWord && map!=null){
				return map;
			}
			map = new HashMap<String,Object>();
			tableName = tableName.toLowerCase();
			map.put("tableName", tableName);
			//解析word内容
			File wordFile = new File(wordPath);
			if(!wordFile.exists()){
				throw new RuntimeException("文件不存在:"+wordPath);
			}
			
			List<String> wordStrList = OfficeUtils.readWordReturnList(wordFile);
			String tableNameStr = null;//表的汉字名称
			String lastStr = null;//上一行的内容
			String tableStr = null;//指定表名的表格的全部字符串
			a:for(int i=0;i<wordStrList.size();i++){
				String str = wordStrList.get(i).toLowerCase();
				//获取表的中文名称
				if(tableNameStr==null && str.contains("("+tableName+")")){
					tableNameStr = str.substring(0,str.indexOf("(")).trim();
					int start = tableNameStr.lastIndexOf("");
					if(start!=-1){
						tableNameStr = tableNameStr.substring(start+1).trim();
						start = tableNameStr.indexOf("	");
						if(start!=-1){
							tableNameStr = tableNameStr.substring(start+1).trim();
						}
					}
				}
				
				//获取表格字符串
				if(tableStr==null && str.contains("表名")){
					//根据 表名 和表名获取
					if(RegexUtils.getSubstrByRegex(str, "表名([^]*?)"+tableName+"(?=\\b)")!=null){
						tableStr = str;
					}
//				if(str.contains("("+tableName+")") || str.contains("（"+tableName+"）") || str.contains(""+tableName+"")){
//					tableStr = str;
//				}
					//根据 表名 和表的中文名获取
					if(tableNameStr!=null && (str.contains(tableNameStr))){
						tableStr = str;
					}
				}
				if(tableStr!=null){
					//判断是否以结尾,如果不是,则添加下一行的字符串
					for(int j=1;;j++){
						if(tableStr.endsWith("")){
							break a;
						}
						tableStr += wordStrList.get(i+j).toLowerCase();
					}
				}
				lastStr = str;
			}
			if(tableStr==null){
				throw new RuntimeException("没有找到指定表名的word内容,查找字符串:"+"表名"+tableName+",表名"+tableNameStr);
			}
			//表名活动策略信息表(activestrategyinfo)数据库用户wt主键id其他排序字段索引字段字段说明字段名称数据类型（精度范围）允许为空y/n唯一y/n备注ididbigint(12)ny活动策略id活动idactiveidbigint(12)n策略名称strategynamevarchar(50)y策略奖项strategylotteryinty0-特等奖1-一等奖2-二等奖3-三等奖4-无奖有效开始次数validbegintimeinty1有效结束次数validendtimeinty3概率（权重）probabilityinty1-10红包金额couponamountdecimal(12,2)y有效天数validnuminty2015-04-07更新时间updatetimedatetimey创建时间createtimedatetimenmysql脚本记录数增长量表的并发[此表的并发程度]补充说明[补充说明]通过活动产生优惠卷
			//表示分割表格的一列,表示分割表格的一行
			//中文名
			if(lastStr!=null){
				lastStr = lastStr.trim();
				if(lastStr.endsWith("表")){
					map.put("tableChineseName", lastStr);
				}
			}
			//主键
			String primaryKey = RegexUtils.getSubstrByRegexReturnList(tableStr, "主键(.*?)").get(0);
			map.put("primaryKey", primaryKey.trim());
			//索引列
			String indexName = RegexUtils.getSubstrByRegexReturnList(tableStr, "索引字段(.*?)").get(0);
			if(indexName.length()!=0){
				String[] arr = indexName.split("[,\\+ ]+");
				//去除 含有中文"索引"的列
				List<String> indexNameList = new ArrayList<String>();
				for(String str : arr){
					if(str.length()>0 && !str.contains("索引")){
						indexNameList.add(str);
					}
				}
				map.put("indexNameArr", indexNameList.toArray(new String[0]));
			}else{
				map.put("indexNameArr", null);
			}
			//列
			Map<String,Map<String,String>> columnMapMap = new LinkedHashMap<String,Map<String,String>>();
			String columnStr = RegexUtils.getSubstrByRegexReturnList(tableStr, "备注(.*?)mysql脚本").get(0);
			String[] columnArr = columnStr.split("",-1);
			for(int i=0;i*7+5<columnArr.length;i++){
				Map<String,String> columnMap = new HashMap<String,String>();
				String name = columnArr[i*7+1].replace(" ", "").replace(" ", "");//去除空格
				if(name.length()==0 && columnArr[i*7+2].length()==0){
					//跳过空列
					continue;
				}
				columnMap.put("name", name);
				columnMap.put("type", columnArr[i*7+2]);
				columnMap.put("isNull", columnArr[i*7+3].equalsIgnoreCase("N")?"false":"true");
				columnMap.put("isUnique", columnArr[i*7+4].equalsIgnoreCase("Y")?"true":"false");
				columnMap.put("explain", columnArr[i*7+0]);
				columnMap.put("remark", columnArr[i*7+5]);
				columnMapMap.put(name, columnMap);
				
				//判断列的类型是否正确(错误的类型会报错)
				try{
					getJavaType(columnMap.get("type"));
				}catch(Exception e){
					//修改常用出错字段类型
					if(columnMap.get("type").startsWith("decimail")){
						columnMap.put("type", columnMap.get("type").replaceAll("decimail", "decimal"));
					}else if(columnMap.get("type").startsWith("varchcar")){
						columnMap.put("type", columnMap.get("type").replaceAll("varchcar", "varchar"));
					}else if(columnMap.get("type").startsWith("varcar")){
						columnMap.put("type", columnMap.get("type").replaceAll("varcar", "varchar"));
					}else{
						System.err.println("请注意,类型不正确:"+columnMap+",talbeName:"+tableName);
					}
				}
				columnMap.put("javaType",getJavaType(columnMap.get("type")));
			}
			map.put("columnMapMap", columnMapMap);
			cacheMap.get(wordPath).put(tableName, map);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 获得数据库类型对应的java类型
	 * @author likaihao
	 * @date 2015年7月29日 上午9:31:02
	 * @param dbType
	 * @return
	 */
	public static String getJavaType(String dbType){
		String type = null;
		dbType = dbType.toLowerCase();
		if(dbType.startsWith("varchar") || dbType.startsWith("char") || dbType.startsWith("text") || dbType.startsWith("mediumtext")){
			type = "String";
		}else if(dbType.startsWith("int") || dbType.startsWith("tinyint")){
			type = "Integer";
		}else if(dbType.startsWith("bigint") || dbType.startsWith("number")){
			type = "Long";
		}else if(dbType.startsWith("decimal")){
			type = "Double";
		}else if(dbType.startsWith("datetime") || dbType.startsWith("date") || dbType.startsWith("time")){
			type = "Date";
		}else{
			//一般纠错
			if(dbType.startsWith("varhcar")){
				type = "String";
			}else{
				throw new RuntimeException("未知的dbType:"+dbType);
			}
		}
		return type;
	}
	
	
	/**
	 * 获取指定表名的所有列
	 * @author likaihao
	 * @date 2015年8月4日 上午11:52:04
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("all")
	public static List<String> getColumnNameList(String wordPath,String tableName){
		Map<String,Object> columnMapMap = (Map<String, Object>) getWordMap(wordPath,tableName,false).get("columnMapMap");
		return new ArrayList<String>(columnMapMap.keySet());
	}
}