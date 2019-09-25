package controller;

import java.util.List;
import java.util.Map;

import model.generate.QueryTemplet;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.QueryService;

@Controller
@Scope("prototype")
@RequestMapping("/QueryController")
public class QueryController extends BaseController{
	
	QueryService queryService = new QueryService();
	
	//返回文件查询页面
	@RequestMapping("/queryFrame_page.do")
	public String queryFrame_page(){
		return "queryFrame.html";
	}
	
	//返回文件查询页面
	@RequestMapping("/query_page.do")
	public String query_page(){
		return "query.html";
	}
	
	//查询_获取模板
	@RequestMapping("/getAllTemplet.do")
	@ResponseBody
	public void getAllTemplet(){
		List<QueryTemplet> list = queryService.getQueryList();
		writeJsonToPage(list);
	}
	
	//查询
	@RequestMapping("/query.do")
	@ResponseBody
	public void query(String path,String pathPattern,String noPathPattern,String fileNamePattern,String noFileNamePattern,boolean matchCase,String queryStr,String encoding,int showLineNum){
		Map<String,String> map = queryService.query(path, pathPattern, noPathPattern, fileNamePattern, noFileNamePattern, matchCase, queryStr,encoding,showLineNum);
		writeJsonToPage(map);
	}
	
	//替换
	@RequestMapping("/replace.do")
	@ResponseBody
	public void replace(String path,String pathPattern,String noPathPattern,String fileNamePattern,String noFileNamePattern,boolean matchCase,String queryStr,String replaceStr,String encoding){
		if(!getCurrentRequestRemoteAddr().equals("127.0.0.1")){
			writeStrToPage("当前操作只能在本地执行");
		}else{
			String returnStr = queryService.replace(path, pathPattern, noPathPattern, fileNamePattern, noFileNamePattern, matchCase, queryStr, replaceStr, encoding);
			writeStrToPage(returnStr);
		}
	}
}