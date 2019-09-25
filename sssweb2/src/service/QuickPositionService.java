package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jetbrick.util.StringUtils;
import dao.CommonDao;


public class QuickPositionService {

	/**
	 * 描述:获得所有 可快速定位的type和name 
	 * @author: 李凯昊
	 * @date:2017年5月15日 上午9:10:04
	 * @param content
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getAllTypeAndName(){
		List<String> sqlList = new ArrayList<String>();
		
		//文件查询
		sqlList.add("select '文件查询' type,name from t_query_templet");
		//发送http请求
		sqlList.add("select '发送http请求' type,name from t_http_templet");
		//字符串处理
		sqlList.add("select '字符串处理' type,name from t_string_handle_templet");
		//代码块调用
		sqlList.add("select '代码块调用' type,name from t_code_call_templet");
		//ssh客户端操作
		sqlList.add("select 'ssh客户端操作' type,name from t_remote_project");
		
		//查询
		String sql = StringUtils.join(sqlList, " union all ");
		List<Object[]> resultList = CommonDao.execSqlQuery(sql);
		
		//转换返回值
		List<Map<String,String>> returnList = new ArrayList<Map<String,String>>();
		for(Object[] arr : resultList){
			Map<String,String> map = new HashMap<String,String>();
			map.put("type", (String)arr[0]);
			map.put("name", (String)arr[1]);
			returnList.add(map);
		}
		return returnList;
	}
	
}
