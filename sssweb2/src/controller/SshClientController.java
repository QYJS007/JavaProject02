package controller;

import java.util.List;

import model.generate.RemoteCommand;
import model.generate.RemoteProject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.RemoteProjectService;
import service.SshClientService;
import service.generate.RemoteProjectGenService;
import utils.LoggerUtils;
import utils.TimerManager;

@Controller
@Scope("prototype")
@RequestMapping("/SshClientController")
public class SshClientController extends BaseController{
	
	RemoteProjectGenService remoteProjectGenService = new RemoteProjectGenService();
	RemoteProjectService remoteProjectService = new RemoteProjectService();
	
	//ssh_返回页面
	@RequestMapping("/sshClient_page.do")
	public String sshClient_page(){
		return "sshClient.html";
	}
	
	//ssh_获取全部项目类型
	@RequestMapping("/getAllType.do")
	@ResponseBody
	public void getAllType(){
		List<String> typeList = remoteProjectService.getAllType();
		writeJsonToPage(typeList);
	}
	
	//ssh_获取远程项目
	@RequestMapping("/getRemoteProjectListByType.do")
	@ResponseBody
	public void getRemoteProjectListByType(String type){
		List<RemoteProject> list = remoteProjectService.getRemoteProjectListByType(type);
		writeJsonToPage(list);
	}
	
	//ssh_模板改变时的操作
	@RequestMapping("/openConn.do")
	@ResponseBody
	public void openConn(String lastId,String remoteProjectId) throws Exception{
		response.setHeader("Connection", "keep-alive");
		if(lastId!=null && lastId.length()>0){
			try {
				SshClientService.getInstance(lastId).closeConn(true);
			} catch (Exception e) {
			}
		}
		SshClientService.getInstance(null).openConn(remoteProjectId, getCurrentResponseOutputStream());
	}
	
	//ssh_获取远程命令
	@RequestMapping("/getRemoteCommand.do")
	@ResponseBody
	public void getRemoteCommand(Integer remoteProjectId){
		List<RemoteCommand> list = remoteProjectService.getRemoteCommandList(remoteProjectId, "ssh-client", null);
		writeJsonToPage(list);
	}
	
	//ssh_执行命令
	@RequestMapping("/execCommand.do")
	@ResponseBody
	public void execCommand(String nowId, String command){
		if(nowId==null || nowId.length()==0){
			throw new RuntimeException("nowId不能为空");
		}
		SshClientService.getInstance(nowId).execCommand(command);
	}
	
	//ssh_关闭连接
	@RequestMapping("/closeConn.do")
	@ResponseBody
	public void closeConn(String nowId){
		if(nowId==null || nowId.length()==0){
			throw new RuntimeException("nowId不能为空");
		}
		SshClientService.getInstance(nowId).closeConn(true);
	}
	
	//ssh_下载日志
	@RequestMapping("/downFile.do")
	@ResponseBody
	public void downFile(String nowId,String path) throws Exception{
		if(nowId==null || nowId.length()==0){
			throw new RuntimeException("nowId不能为空");
		}
		SshClientService.getInstance(nowId).downFile(path);
	}
	
	//ssh_下载日志
	@RequestMapping("/openSecureCRT.do")
	@ResponseBody
	public void openSecureCRT(String nowId) throws Exception{
		if(nowId==null || nowId.length()==0){
			throw new RuntimeException("nowId不能为空");
		}
		SshClientService.getInstance(nowId).openSecureCRT();
	}
	
	//ssh_定时报告状态,重置定时器
	@RequestMapping("/state.do")
	@ResponseBody
	public void state(String nowId){
		if(nowId!=null && nowId.length()>0){
			try {
				LoggerUtils.info("ssh-报告状态,id:"+nowId+",定时器id:"+SshClientService.getInstance(nowId).pageCloseTimerManager.getTimerName());
				SshClientService.getInstance(nowId).pageCloseTimerManager.reset();
				writeStrToPage("true");
			} catch (Exception e) {
				LoggerUtils.info("ssh_state,没有发现id:"+nowId);
				writeStrToPage("false");
			}
		}
	}
	
	//ssh_打印所有连接
	@RequestMapping("/allConn.do")
	@ResponseBody
	public void allConn(){
		writeStrToPage("页面列表:\n");
		writeStrToPage(SshClientService.sshBPMap.keySet().toString());
		writeStrToPage("\n定时器列表:\n");
		writeStrToPage(TimerManager.getStatus());
	}
	
}