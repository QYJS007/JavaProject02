package controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("prototype")
@RequestMapping("/RegexController")
public class RegexController extends BaseController{
	
	//正则匹配_返回页面
	@RequestMapping("/regex_page.do")
	public String regex_page(){
		return "regex.html";
	}
		
}
