package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.generate.Params;
import service.model.Model;
import service.model.WordTitle;
import utils.ExceptionUtils;
import utils.JsonUtils;
import utils.TempletUtils;

public class WordSqlGenerateService {
	
	ParamsService paramsService = new ParamsService();
	WordService wordService = new WordService();
	
	//查询所有的word文档
	public List<String> getAllWordPath(){
		Params params = paramsService.findParamsByName("wordSqlPath");
		String wordPathJson = params.getValue();
		List<String> wordPathList = null;
		try {
			wordPathList = JsonUtils.parseJson2List(wordPathJson.replace("\\", "\\\\"), String.class, null);
		} catch (Exception e) {
			wordPathList = JsonUtils.parseJson2List(wordPathJson, String.class, null);
		}
		return wordPathList;
	}
	
	//查询所有表名
	public List<String> getAllTableName(String wordPath){
		if(wordPath.endsWith("docx")){
			throw new RuntimeException("sorry,暂不支持docx格式的word文档,请转换为doc格式后重试!");
		}
		
		//获取word标题信息
		Map<String, WordTitle> wordTitleMap = wordService.getWordTitleMap(wordPath);
		
		//筛选
		List<String> list = new ArrayList<String>();
		for(String name : wordTitleMap.keySet()){
			try {
				WordTitle wordTitle = wordTitleMap.get(name);
				Model model = wordService.getTableModelByTable(wordTitle);
				if(model!=null){
					list.add(model.getComment());
				}
			} catch (Exception e) {
				list.add("解析出错:"+name);
			}
		}
		return list;
	}
	
	//查询模板和参数
	public Map<String,String> getTempletAndParam(){
		Params params1 = paramsService.findParamsByName("wordSqlTemplet");
		Params params2 = paramsService.findParamsByName("wordSqlTempletParam");
		Map<String,String> map = new HashMap<String,String>();
		map.put("templet", params1.getValue());
		map.put("templetParam", params2.getValue());
		return map;
	}
	
	//生成
	public String generate(String wordPath, String tableName, String templet, String paramStr){
		Map<String,Object> paramMap = TempletUtils.getBaseParamMap();
		
		//获取word标题信息
		Map<String, WordTitle> wordTitleMap = wordService.getWordTitleMap(wordPath);
		
		//获取表信息
		tableName = tableName.replace("解析出错:", "");
		WordTitle wordTitle = wordTitleMap.get(tableName);
		Model model = wordService.getTableModelByTable(wordTitle);
		
		//添加参数
		paramMap.put("model", model);
		paramMap.putAll(TempletUtils.getParamMap(paramStr,paramMap,null));
		
		//替换模板
		templet = templet.replace("　　", "\t");
		String result = TempletUtils.templetFillMultiLine(templet, paramMap);
		return result;
	}
	
	/**
	 * 获得所有错误
	 * @author likaihao
	 * @datetime 2017年3月11日 下午2:54:31
	 * @param wordPath
	 */
	public String getAllError(String wordPath){
		if(wordPath.endsWith("docx")){
			throw new RuntimeException("sorry,暂不支持docx格式的word文档,请转换为doc格式后重试!");
		}
		
		//获取word标题信息
		Map<String, WordTitle> wordTitleMap = wordService.getWordTitleMap(wordPath);
		
		//筛选
		StringBuilder builder = new StringBuilder();
		for(String name : wordTitleMap.keySet()){
			try {
				WordTitle wordTitle = wordTitleMap.get(name);
				wordService.getTableModelByTable(wordTitle);
			} catch (Exception e) {
				builder.append(name+":\n\t"+ExceptionUtils.getErrMsg(e)+"\n");
			}
		}
		return builder.toString();
	}
}
