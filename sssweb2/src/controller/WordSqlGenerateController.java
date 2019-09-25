package controller;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.WordSqlGenerateService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/WordSqlGenerateController")
public class WordSqlGenerateController extends BaseController{
	
	WordSqlGenerateService wordSqlGenerateBP = new WordSqlGenerateService();
	
	//返回模板代码生成功能页面
	@RequestMapping("/wordSql_page.do")
	public String wordSql_page(){
		return "index2/wordSqlGenerate.html";
	}
	
	//查询所有的word文档
	@RequestMapping("/getAllWordPath.do")
	@ResponseBody
	public void getAllWordPath(){
		List<String> wordPathList = wordSqlGenerateBP.getAllWordPath();
		writeJsonToPage(wordPathList);
	}
	
	//查询所有表名
	@RequestMapping("/getAllTableName.do")
	@ResponseBody
	public void getAllTableName(String wordPath){
		List<String> list = wordSqlGenerateBP.getAllTableName(wordPath);
		writeJsonToPage(list);
	}
	
	//查询模板和参数
	@RequestMapping("/getTempletAndParam.do")
	@ResponseBody
	public void getTempletAndParam(){
		Map<String,String> map = wordSqlGenerateBP.getTempletAndParam();
		writeStrToPage(JsonUtils.parseObject(map));
	}
	
	//生成
	@RequestMapping("/generate.do")
	@ResponseBody
	public void generate(String wordPath, String tableName, String templet, String paramStr){
		String result = wordSqlGenerateBP.generate(wordPath, tableName, templet, paramStr);
		writeStrToPage(result);
	}
	
	//查看全部错误
	@RequestMapping("/getAllError.do")
	@ResponseBody
	public void getAllError(String wordPath){
		String result = wordSqlGenerateBP.getAllError(wordPath);
		writeStrToPage(result);
	}
	
}