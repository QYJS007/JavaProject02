package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.generate.StringHandleTemplet;
import service.generate.StringHandleTempletGenService;
import sys.SystemConf;
import utils.ListUtils;
import utils.TempletUtils;
import dao.generate.StringHandleTempletGenDao;


public class StringHandleService {
	
	StringHandleTempletGenService stringHandleTempletGenService = new StringHandleTempletGenService();
	StringHandleTempletGenDao stringHandleTempletGenDao = new StringHandleTempletGenDao();
	
	/**
	 * 获取全部类型
	 * @author likaihao
	 * @date 2016年12月20日 上午9:22:48
	 * @return
	 */
	public List<String> getAllType(){
		List<StringHandleTemplet> list = stringHandleTempletGenService.findAll();
		Set<String> typeSet = new HashSet<String>();
		for(StringHandleTemplet s : list){
			typeSet.add(s.getType());
		}
		List<String> typeList = new ArrayList<String>(typeSet);
		//对list进行排序
		String stringHandleTypeSort = SystemConf.getResourceValue("stringHandleTypeSort");
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
	public List<StringHandleTemplet> getTempletListByType(String type){
		List<StringHandleTemplet> list = null;
		if(type!=null && type.length()>0){
			//查询指定type的值
			list = stringHandleTempletGenDao.findCollectionByConditionNoPage(" and type=?", new Object[]{type}, null);
		}else{
			list = stringHandleTempletGenService.findAll();
		}
		
		//按名称进行排序
		ListUtils.sort(list, "name", true);
		return list;
	}
	
	/**
	 * 字符串处理
	 * @author likaihao
	 * @date 2016年12月20日 上午11:11:25
	 * @param text
	 * @param code
	 * @throws Exception
	 */
	 public String generation(String text,String code) throws Exception{
		//去除奇怪的空格问题(160-->32)
		text = text.replace(" ", " ");
		Map<String,Object> paramMap = TempletUtils.getBaseParamMap();
		paramMap.put("_value",text);
		String _newValue = (String) TempletUtils.getParamMap(code, paramMap, null).get("_newValue");
		return _newValue;
	}
}