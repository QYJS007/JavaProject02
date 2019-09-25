package controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.LoginService;
import service.ParamsService;
import utils.AlgorithmUtils;
import utils.CheckNumUtils;

@Controller
@Scope("prototype")
@RequestMapping("/LoginController")
public class LoginController extends BaseController {
	
	 LoginService loginService = new LoginService();
	
	//返回登录首页
	@RequestMapping("/login_page.do")
	public String login_page() {
		return "login.html";
	}
	
	//获取验证码
	@RequestMapping("/getCheckNum.do")
	@ResponseBody
	public void getCheckNum() {
		request.getSession();//先创建session
		String checkNum = CheckNumUtils.createCheckImage(response);
		request.getSession().setAttribute("checkNum", checkNum);
	}
	
	//登录
	@RequestMapping("/login.do")
	public String login(String username,String password,String checkNum){
		//验证验证码
		boolean result = loginService.checkCheckNum(request, checkNum);
		if(!result){
			return "login.html";
		}
		
		//验证用户名和密码
		if(loginService.checkUserPassword(username,password)){
			//将登录信息存入session
			String sessionStr = username + "_" + password;
			String desKey = new ParamsService().findParamsByName("desKey").getValue();
			byte[] sessionDate = AlgorithmUtils.encryptDES(sessionStr.getBytes(), desKey.getBytes());
			request.getSession().setAttribute("userinfo", AlgorithmUtils.encodeBase64(sessionDate));
			return "redirect:/index.do";
		}else{
			return "redirect:/LoginController/login_page.do";
		}
	}
	
}