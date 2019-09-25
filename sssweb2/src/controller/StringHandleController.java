package controller;

import java.util.List;

import model.generate.StringHandleTemplet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.StringHandleService;
import service.generate.StringHandleTempletGenService;

@Controller
@Scope("prototype")
@RequestMapping("/StringHandleController")
public class StringHandleController extends BaseController{
	
	StringHandleTempletGenService stringHandleTempletGenService = new StringHandleTempletGenService();
	StringHandleService stringHandleService = new StringHandleService();
	
	//返回字符串处理页面
	@RequestMapping("/stringHandle_page.do")
	public String stringHandle_page(){
		return "stringHandle.html";
	}
	
	//获取全部项目类型
	@RequestMapping("/getAllType.do")
	@ResponseBody
	public void getAllType(){
		List<String> typeList = stringHandleService.getAllType();
		writeJsonToPage(typeList);
	}
	
	
	//获取指定类型的字符串处理模板
	@RequestMapping("/getTempletListByType.do")
	@ResponseBody
	public void getRemoteProjectListByType(String type){
		List<StringHandleTemplet> list = stringHandleService.getTempletListByType(type);
		writeJsonToPage(list);
	}
	
	//字符串处理
	@RequestMapping("/generation.do")
	@ResponseBody
	public void generation(String text,String code) throws Exception{
		String _newValue = stringHandleService.generation(text, code);
		writeStrToPage(_newValue);
	}
}