package work.codegenerate.base;

import java.util.Collection;

import utils.StringUtils;
import work.codegenerate.base.model.ColumnInfo;
import work.codegenerate.base.model.TableInfo;

public class CommonUtils {
	
	/**
	 * 根据表名获取模型名称
	 * @author likaihao
	 * @date 2016年7月10日 下午9:39:58
	 * @param tableName
	 * @param tablePrefix
	 * @return
	 */
	public String getModelNameByTableName(String tableName,String tablePrefix){
		//去掉表前缀
		String modelName = tableName.substring(tablePrefix.length());
		//按_分割,首字母大写
		String[] modelName_arr = modelName.split("_");
		for(int i=0;i<modelName_arr.length;i++){
			modelName_arr[i] = StringUtils.firstCharUpperCase(modelName_arr[i]);
		}
		modelName = StringUtils.join(modelName_arr, "");
		return modelName;
	}
	
	/**
	 * 判断指定的表中是否含有日期类型的字段
	 * @author likaihao
	 * @date 2016年7月17日 上午10:48:38
	 * @param tableInfo
	 * @return
	 */
	public boolean containsDateColumn(TableInfo tableInfo){
		Collection<ColumnInfo> coll = tableInfo.getColumnInfoMap().values();
		for(ColumnInfo info : coll){
			if(info.getJavaType().contains("Date")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 首字母大写
	 * @author likaihao
	 * @date 2015年9月9日 上午10:31:58
	 * @param str
	 * @return
	 */
	public String firstCharUpperCase(String str){
		if(str==null || str.length()==0){
			return "";
		}
		return str.substring(0,1).toUpperCase()+str.substring(1);
	}
	
	/**
	 * 首字母小写
	 * @author likaihao
	 * @date 2015年9月9日 上午10:31:58
	 * @param str
	 * @return
	 */
	public String firstCharLowerCase(String str){
		if(str==null || str.length()==0){
			return "";
		}
		return str.substring(0,1).toLowerCase()+str.substring(1);
	}
	
	/**
	 * 将数组以指定分隔符拼接成字符串
	 * @param arr
	 * @param splitChar
	 * @return
	 */
	public <T> String join(T[] arr,String splitChar){
		if(arr==null || arr.length==0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<arr.length;i++){
			builder.append(arr[i]);
			if(i<arr.length-1){
				builder.append(splitChar);
			}
		}
		return builder.toString();
	}
	
	/**
	 * 将集合以指定分隔符拼接成字符串
	 * @param arr
	 * @param splitChar
	 * @return
	 */
	public String join(Collection<String> list,String splitChar){
		if(list==null || list.size()==0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for(String str : list){
			builder.append(str);
			if(i<list.size()-1){
				builder.append(splitChar);
			}
			i++;
		}
		return builder.toString();
	}
}