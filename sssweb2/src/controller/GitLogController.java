package controller;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.GitLogService;

@Controller
@Scope("prototype")
@RequestMapping("/GitLogController")
public class GitLogController extends BaseController {
	
	GitLogService gitLogService = new GitLogService();
	
	//返回页面
	@RequestMapping("/gitLog_page.do")
	public String gitLog_page() {
		return "gitLog.html";
	}
	
	//获取所有本地仓库路径
	@RequestMapping("/getAllGitLocalPath.do")
	@ResponseBody
	public void getAllGitLocalPath(){
		List<String> pathList = gitLogService.getAllGitLocalPath();
		writeJsonToPage(pathList);
	}
	
	//获取指定本地仓库的所有分支
	@RequestMapping("/getAllBranch.do")
	@ResponseBody
	public void getAllBranch(String path){
		List<String> branchList = gitLogService.getAllBranch(path);
		writeJsonToPage(branchList);
	}
	
	//获取指定本地仓库的所有分支
	@RequestMapping("/getPageDefaultValue.do")
	@ResponseBody
	public void getPageDefaultValue(String path){
		Map<String,String> map = gitLogService.getPageDefaultValue(path);
		writeJsonToPage(map);
	}
	
	//查询git日志
	@RequestMapping("/searchLog.do")
	@ResponseBody
	public void searchLog(String path, String branch, String committer, String afterDate, String beforeDate, String comment){
		String result = gitLogService.searchLog(path, branch, committer, afterDate, beforeDate, comment);
		writeStrToPage(result);
	}
	
	//查询指定提交修改的文件路径
	@RequestMapping("/getChangeFilePath.do")
	@ResponseBody
	public void getChangeFilePath(String path, String branch, String hashIds){
		String result = gitLogService.getChangeFilePath(path, branch, hashIds);
		writeStrToPage(result);
	}
	
	
}
