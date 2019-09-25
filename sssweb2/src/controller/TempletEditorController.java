package controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.TempletEditorService;

@Controller
@Scope("prototype")
@RequestMapping("/TempletEditorController")
public class TempletEditorController extends BaseController{
	
	TempletEditorService templetEditorBP = new TempletEditorService();
	
	//返回模板代码生成功能页面
	@RequestMapping("/editor_page.do")
	public String editor_page(){
		return "index2/templetEditor.html";
	}
	
	//获得模板内容
	@RequestMapping("/getTempletStr.do")
	@ResponseBody
	public void getTempletStr(){
		String templetStr = templetEditorBP.getTempletStr();
		writeStrToPage(templetStr);
	}
	
	//保存模板内容
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(String templetStr){
		templetEditorBP.save(templetStr);
	}
	
}