package controller;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.SshQueryService;

@Controller
@Scope("prototype")
@RequestMapping("/SshQueryController")
public class SshQueryController extends BaseController{
	
	//ssh查询_返回页面
	@RequestMapping("/sshQuery_page.do")
	public String sshQuery_page(){
		return "sshQuery.html";
	}
	
	//ssh查询
	@RequestMapping("/sshQuery.do")
	@ResponseBody
	public void sshQuery(String filePath,String queryStr,boolean matchCase,Integer showLineNum){
		Map<String,String> map = new SshQueryService().pageSshQuery(filePath,queryStr,matchCase,showLineNum);
		writeJsonToPage(map);
	}
}