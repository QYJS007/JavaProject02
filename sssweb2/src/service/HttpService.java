package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.generate.HttpTemplet;
import service.generate.HttpTempletGenService;
import sys.SystemConf;
import utils.HttpUtils;
import utils.ListUtils;
import utils.LoggerUtils;
import utils.TempletUtils;
import dao.generate.HttpTempletGenDao;

public class HttpService {
	
	HttpTempletGenService httpTempletGenService = new HttpTempletGenService();
	HttpTempletGenDao httpTempletGenDao = new HttpTempletGenDao();
	
	/**
	 * 获取全部类型
	 * @author likaihao
	 * @date 2016年12月20日 上午9:22:48
	 * @return
	 */
	public List<String> getAllType(){
		List<HttpTemplet> list = httpTempletGenService.findAll();
		Set<String> typeSet = new HashSet<String>();
		for(HttpTemplet h : list){
			typeSet.add(h.getType());
		}
		List<String> typeList = new ArrayList<String>(typeSet);
		//对list进行排序
		String stringHandleTypeSort = SystemConf.getResourceValue("httpTempletTypeSort");
		List<String> valueOrderBy = Arrays.asList(stringHandleTypeSort.split(","));
		ListUtils.sort(typeList, null, new ArrayList<Object>(valueOrderBy));
		return typeList;
	}
	
	/**
	 * 根据类型获取模板
	 * @author likaihao
	 * @date 2016年4月25日 下午5:11:56
	 * @return
	 */
	public List<HttpTemplet> getTempletListByType(String type){
		List<HttpTemplet> list = null;
		if(type!=null && type.length()>0){
			//查询指定type的值
			list = httpTempletGenDao.findCollectionByConditionNoPage(" and type=?", new Object[]{type}, null);
		}else{
			list = httpTempletGenService.findAll();
		}
		
		//按名称进行排序
		ListUtils.sort(list, "name", true);
		return list;
	}
	
	/**
	 * 发送http请求
	 * @author likaihao
	 * @date 2015年12月17日 下午2:51:04
	 * @param url 请求地址
	 * @param method 请求方式
	 * @param encoding 编码
	 * @param paramStr 参数字符串
	 * @param headerStr 请求头字符串
	 * @return 响应结果
	 */
	public String send(String url,String method,String encoding,String paramStr,String headerStr){
		/**
		 * 发送http请求相关的规则
		 *  	_sendParam:str,map //保留的参数,其他参数不会被发送
		 * 		_noSendParam:map2 //移除的参数,此参数不会被发送
		 *  	_body:{
		 *  		xxxx{:str}xxx
		 *  	} //_body会认为没有key,作为请求体发送,其他参数全部不发送,暂时不支持变量填充(暂时没有需求)
		 *  	_after:{:_value.substring(1)} //_after是对返回结果进行的操作,_value为原来的返回结果,_after将返回一个string为新的返回结果(此标签中只能写一句语句)
		 *  	_codeBlock:{:xxx} //不会发送http请求,将执行指定的方法,将方法的结果返回(此标签中只能写一句语句)
		 */
		
		//获取参数
		Map<String,Object> baseParamMap = TempletUtils.getBaseParamMap();
		
		//原样输出的变量
		List<String> onlyOutList = Arrays.asList(new String[]{"_sendParam","_noSendParam","_body","_after"});
		
		//如果存在_body,需要将\n后添加\t
		if(paramStr.contains("_body")){
			String bodyStr = TempletUtils.getAllTwinTagContent(paramStr, "_body:{").get(0);
			paramStr = paramStr.replace(bodyStr, bodyStr.replace("\n", "\n\t"));
		}
		
		//获取参数
		Map<String,String> paramMap = TempletUtils.getParamStrMap(paramStr,baseParamMap,onlyOutList);
		
		//获取之后要执行的操作
		String _afterStr = paramMap.get("_after");
		paramMap.remove("_after");
		
		String str = null;
		//判断参数中是否含有_codeBlock, 如果有则不发送请求,直接执行指定代码块
		if(!paramMap.containsKey("_codeBlock")){
			//不是代码块,要发送http请求
			
			//处理url
			if(url.contains(TempletUtils.getTag()[0])){
				url = TempletUtils.templetFillOneLine(url, baseParamMap);
			}
			
			//获取请求头
			Map<String,String> headerMap = TempletUtils.getParamStrMap(headerStr,baseParamMap,null);
			
			//判断是否存在_body参数
			if(paramMap.containsKey("_body")){
				String bodyStr = paramMap.get("_body");
				bodyStr = bodyStr.substring(1,bodyStr.length()-1).trim();//去掉前后大括号
				bodyStr = bodyStr.replace("\n\t", "\n");//将前面替换的\n\t替换回\n
				paramMap.clear();
				paramMap.put("", bodyStr);
			}else{
				//判断sendParam参数
				if(paramMap.containsKey("_sendParam")){
					String sendParamStr = paramMap.get("_sendParam").toString();
					String[] paramArr = sendParamStr.split(",");
					Map<String,String> paramMap2 = new HashMap<String,String>();
					for(String param : paramArr){
						paramMap2.put(param, paramMap.get(param));
					}
					if(paramMap.containsKey("_noSendParam")){
						paramMap2.put("_noSendParam", paramMap.get("_noSendParam"));
					}
					paramMap = paramMap2;
				}
				
				//判断noSendParam参数
				if(paramMap.containsKey("_noSendParam")){
					String noSendParamStr = paramMap.get("_noSendParam").toString();
					String[] paramArr = noSendParamStr.split(",");
					for(String param : paramArr){
						paramMap.remove(param);
					}
					paramMap.remove("_noSendParam");
				}
			}
			
			//发送请求
			Long startTime = System.currentTimeMillis();
			LoggerUtils.info("发送http请求,url:"+url+", 请求方式:"+method+", 参数:"+paramMap);
			
			//发送http请求
			byte[] bytes = new HttpUtils().sendReturnByteArr(method, url, paramMap, headerMap, true);
			str = HttpUtils.parseHttpByteToString(bytes,encoding);
			
			Long endTime = System.currentTimeMillis();
			LoggerUtils.info("发送http请求,已返回,耗时:"+(endTime-startTime)+",返回结果:"+str);
		}else{
			//已调用了代码块,返回结果
			str = paramMap.get("_codeBlock");
		}
		
		//执行之后要执行的操作
		if(_afterStr!=null){
			baseParamMap.put("_value", str);
			str = TempletUtils.computeEngine(_afterStr, baseParamMap).toString();
		}
		
		return str;
	}
}