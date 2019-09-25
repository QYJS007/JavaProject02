package controller;

import java.util.List;
import java.util.Map;

import model.generate.CodeGenerateTemplet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.generate.CodeGenerateTempletGenService;
import utils.TempletUtils;

@Controller
@Scope("prototype")
@RequestMapping("/CodeGenerateController")
public class CodeGenerateController extends BaseController{
	
	CodeGenerateTempletGenService codeGenerateTempletGenService = new CodeGenerateTempletGenService();
	
	//返回模板代码生成功能页面
	@RequestMapping("/codeGenerate_page.do")
	public String codeGenerate_page(){
		return "codeGenerate.html";
	}
	
	//返回所有模板
	@RequestMapping("/getAllTemplet.do")
	@ResponseBody
	public void getAllTemplet(){
		List<CodeGenerateTemplet> list = codeGenerateTempletGenService.findAll();
		writeJsonToPage(list);
	}
	
	//模板代码生成功能
	@RequestMapping("/generation.do")
	@ResponseBody
	public void generation(String templet,String paramStr){
		//添加页面传递的参数
		Map<String,Object> paramMap = TempletUtils.getBaseParamMap();
		paramMap.putAll(TempletUtils.getParamMap(paramStr,paramMap,null));
		
		//替换模板
		templet = templet.replace("　　", "\t");
		String result = TempletUtils.templetFillMultiLine(templet, paramMap);
		
		writeStrToPage(result);
	}
}