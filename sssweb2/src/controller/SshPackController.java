package controller;

import java.util.List;
import java.util.Map;

import model.generate.LocalProject;
import model.generate.RemoteProject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.LocalProjectService;
import service.SshPackService;
import service.RemoteProjectService;

@Controller
@Scope("prototype")
@RequestMapping("/SshPackController")
public class SshPackController extends BaseController{
	
	LocalProjectService localProjectService = new LocalProjectService();
	RemoteProjectService remoteProjectService = new RemoteProjectService();
	SshPackService packService = new SshPackService();
	
	//打包_获取页面
	@RequestMapping("/sshPack_page.do")
	public String sshPack_page(){
		return "sshPack.html";
	}
	
	//打包_获取本地项目模板
	@RequestMapping("/getLocalTemplet.do")
	@ResponseBody
	public void getLocalTemplet(){
		List<LocalProject> list = localProjectService.getLocalProjectList();
		writeJsonToPage(list);
	}
	
	//打包_获取远程项目模板
	@RequestMapping("/getRemoteTemplet.do")
	@ResponseBody
	public void getRemoteTemplet(String localProject){
		if(localProject.startsWith("workspace")){
			localProject = localProject.substring(localProject.indexOf("> ")+2);
		}
		List<RemoteProject> list = remoteProjectService.getPackRemoteProjectVOList(localProject);
		writeJsonToPage(list);
	}
	
	//打包_查询
	@RequestMapping("/query.do")
	@ResponseBody
	public void query(String localPath,String pathPattern,String type){
		String str = packService.query(localPath, pathPattern, type);
		writeStrToPage(str);
	}
	
	//打包_导出
	@RequestMapping("/export.do")
	@ResponseBody
	public void export(String localPath,String text){
		Map<String,String> map = packService.export(localPath,text);
		writeJsonToPage(map);
	}
	
	//打包_打包
	@RequestMapping("/pack.do")
	@ResponseBody
	public synchronized void pack(String remoteProjectIds,String localFilePath,String type,String pathPattern){
		//将系统输出输出到页面
		response.setHeader("Connection", "keep-alive");
		packService.pack(remoteProjectIds, localFilePath, type, pathPattern, getCurrentResponseOutputStream());
	}
}