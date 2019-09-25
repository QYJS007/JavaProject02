package controller;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.QuickPositionService;

@Controller
@Scope("prototype")
@RequestMapping("/QuickPositionController")
public class QuickPositionController extends BaseController{
	
	QuickPositionService quickPositionService = new QuickPositionService();
	
	//获取全部项目类型
	@RequestMapping("/getAllTypeAndName.do")
	@ResponseBody
	public void getAllTypeAndName(){
		List<Map<String,String>> list = quickPositionService.getAllTypeAndName();
		writeJsonToPage(list);
	}
	
}