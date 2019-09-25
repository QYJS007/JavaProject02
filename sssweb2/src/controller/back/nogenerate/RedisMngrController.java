package controller.back.nogenerate;

import java.util.List;
import java.util.Map;

import model.vo.MessageResult;
import model.vo.RedisKeyVo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.back.nogenerate.RedisMngrService;
import utils.JsonUtils;
import controller.BaseController;

@Controller
@Scope("prototype")
@RequestMapping("/RedisMngrController")
public class RedisMngrController extends BaseController{
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/redisMngr.jsp";
	}
	
	//获取所有库号
	@RequestMapping("/getAllDbNo.do")
	@ResponseBody
	public void getAllDbNo(String redisInfoName){
		List<String> list = new RedisMngrService(redisInfoName).getAllDbNo();
		writeJsonToPage(list);
	}
	
	//获取key,value
	@RequestMapping("/getKeyValue.do")
	@ResponseBody
	public void getKeyValue(String params){
		Map<String,String> paramMap = JsonUtils.parseJson2Map(params, String.class);
		String redisInfoName = paramMap.get("redisInfoName");
		Integer dbNo = new Integer(paramMap.get("dbNo"));
		String pattern = paramMap.get("pattern");
		boolean queryTime = false;
		boolean queryValue = false;
		Integer showNum = 500;
		try {
			queryTime = new Boolean(paramMap.get("queryTime"));
		} catch (Exception e) {
		}
		try {
			queryValue = new Boolean(paramMap.get("queryValue"));
		} catch (Exception e) {
		}
		try {
			showNum = new Integer(paramMap.get("num"));
		} catch (NumberFormatException e) {
		}
		
		List<RedisKeyVo> list = new RedisMngrService(redisInfoName).getKeyValue(dbNo, pattern, queryTime, queryValue, showNum);
		writeJsonToPage(list);
	}
	
	//获取key个数
	@RequestMapping("/queryKeyCount.do")
	@ResponseBody
	public void queryKeyCount(String redisInfoName, Integer dbNo, String pattern){
		int count = new RedisMngrService(redisInfoName).queryKeyCount(dbNo, pattern);
		writeStrToPage("指定模式("+pattern+")的key个数:"+count);
	}
	
	//删除
	@RequestMapping("/delete.do")
	@ResponseBody
	public void delete(String redisInfoName, Integer dbNo, String keyStr){
		String[] keyArr = null;
		if(keyStr!=null && keyStr.length()>0){
			keyArr = keyStr.split("@@");
		}
		MessageResult result = new RedisMngrService(redisInfoName).delete(dbNo, keyArr);
		writeJsonToPage(result);
	}
	
	//清库
	@RequestMapping("/flushDB.do")
	@ResponseBody
	public void flushDB(String redisInfoName, Integer dbNo){
		MessageResult result = new RedisMngrService(redisInfoName).flushDB(dbNo);
		writeJsonToPage(result);
	}
	
}