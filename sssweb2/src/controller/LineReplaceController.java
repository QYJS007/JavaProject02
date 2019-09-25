package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.generate.LineReplaceTemplet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.generate.LineReplaceTempletGenService;
import utils.StringUtils;
import utils.TempletUtils;

@Controller
@Scope("prototype")
@RequestMapping("/LineReplaceController")
public class LineReplaceController extends BaseController{

	LineReplaceTempletGenService lineReplaceTempletGenService = new LineReplaceTempletGenService();
	
	//返回单行模板替换功能页面
	@RequestMapping("/lineReplace_page.do")
	public String lineReplace_page(){
		return "lineReplace.html";
	}
	
	//获得指定功能的模板
	@RequestMapping("/getAllTemplet.do")
	@ResponseBody
	public void getAllTemplet(String funcName){
		List<LineReplaceTemplet> list = lineReplaceTempletGenService.findAll();
		writeJsonToPage(list);
	}
	
	//单行模板替换功能
	@RequestMapping("/replace.do")
	@ResponseBody
	public void replace(String pattern,String text,String newPattern){
		List<String> list = Arrays.asList(text.split("[\n\r]+"));
		List<Map<String,Object>> nameValueMapList = TempletUtils.templetExtractOneLineBatch(list,pattern);
		List<String> list2 = TempletUtils.templetFillOneLineBatch(newPattern,nameValueMapList);
		writeStrToPage(StringUtils.join(list2,"\n"));
	}
}