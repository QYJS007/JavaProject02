package controller;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import model.generate.CodeCallTemplet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.generate.CodeCallTempletGenService;
import utils.ListUtils;
import utils.TempletUtils;

@Controller
@Scope("prototype")
@RequestMapping("/CodeCallController")
public class CodeCallController extends BaseController{
	
	CodeCallTempletGenService callGenService = new CodeCallTempletGenService();
	
	//返回代码调用页面
	@RequestMapping("/codeCall_page.do")
	public String codeCall_page(){
		return "codeCall.html";
	}
	
	//返回所有模板
	@RequestMapping("/getAllTemplet.do")
	@ResponseBody
	public void getAllTemplet(){
		List<CodeCallTemplet> list = callGenService.findAll();
		//按name排序
		ListUtils.sort(list, "name", true);
		writeJsonToPage(list);
	}
	
	//代码调用_调用
	@RequestMapping("/exec.do")
	@ResponseBody
	public synchronized void exec(String code) throws Exception{
		//将http请求设置为长连接,并将system.out输出的地址设置到浏览器
		PrintStream out = System.out;
		response.setHeader("Connection", "keep-alive");
		System.setOut(new PrintStream(getCurrentResponseOutputStream(),true,"utf-8"));
		
		//执行
		String value = "执行完成,没有返回值";
		try {
			//获取参数,即执行代码的过程
			Map<String,Object> paramMap = TempletUtils.getBaseParamMap();
			Object obj = TempletUtils.getParamMap(code,paramMap,null).get("_codeBlock");
			if(obj!=null){
				value = obj.toString();
			}
		}finally{
			System.setOut(out);
		}
		writeStrToPage(value);
	}
}