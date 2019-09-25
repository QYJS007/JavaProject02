package controller;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.WordInterGenerateService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/WordInterGenerateController")
public class WordInterGenerateController extends BaseController{
	
	WordInterGenerateService wordInterGenerateBP = new WordInterGenerateService();
	
	//返回模板代码生成功能页面
	@RequestMapping("/wordInter_page.do")
	public String wordSql_page(){
		return "index2/wordInterGenerate.html";
	}
	
	//查询所有的word文档
	@RequestMapping("/getAllWordPath.do")
	@ResponseBody
	public void getAllWordPath(){
		List<String> wordPathList = wordInterGenerateBP.getAllWordPath();
		writeJsonToPage(wordPathList);
	}
	
	//查询所有接口名称
	@RequestMapping("/getAllInterName.do")
	@ResponseBody
	public void getAllInterName(String wordPath){
		List<String> list = wordInterGenerateBP.getAllInterName(wordPath);
		writeJsonToPage(list);
	}
	
	//查询模板和参数
	@RequestMapping("/getTempletAndParam.do")
	@ResponseBody
	public void getTempletAndParam(){
		Map<String,String> map = wordInterGenerateBP.getTempletAndParam();
		writeStrToPage(JsonUtils.parseObject(map));
	}
	
	//生成
	@RequestMapping("/generate.do")
	@ResponseBody
	public void generate(String wordPath, String interName, String templet, String paramStr){
		String result = wordInterGenerateBP.generate(wordPath, interName, templet, paramStr);
		writeStrToPage(result);
	}
	
	//查看全部错误
	@RequestMapping("/getAllError.do")
	@ResponseBody
	public void getAllError(String wordPath){
		String result = wordInterGenerateBP.getAllError(wordPath);
		writeStrToPage(result);
	}
	
	
	
}