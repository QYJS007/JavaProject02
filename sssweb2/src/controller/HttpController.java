package controller;

import java.util.List;

import model.generate.HttpTemplet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.HttpService;
import service.generate.HttpTempletGenService;

@Controller
@Scope("prototype")
@RequestMapping("/HttpController")
public class HttpController extends BaseController{
	
	HttpTempletGenService httpTempletGenService = new HttpTempletGenService();
	HttpService httpService = new HttpService();
	
	//返回发送http请求的模板
	@RequestMapping("/httpFrame_page.do")
	public String httpFrame_page(){
		return "httpFrame.html";
	}
	
	//返回发送http请求的模板
	@RequestMapping("/http_page.do")
	public String http_page(){
		return "http.html";
	}
	
	//获取全部项目类型
	@RequestMapping("/getAllType.do")
	@ResponseBody
	public void getAllType(){
		List<String> typeList = httpService.getAllType();
		writeJsonToPage(typeList);
	}
	
	
	//获取指定类型的字符串处理模板
	@RequestMapping("/getTempletListByType.do")
	@ResponseBody
	public void getRemoteProjectListByType(String type){
		List<HttpTemplet> list = httpService.getTempletListByType(type);
		writeJsonToPage(list);
	}
	
	//发送http请求
	@RequestMapping("/send.do")
	@ResponseBody
	public void send(String url,String method,String encoding,String paramStr,String headerStr){
		String result = httpService.send(url, method, encoding, paramStr, headerStr);
		writeStrToPage(result);
	}
}