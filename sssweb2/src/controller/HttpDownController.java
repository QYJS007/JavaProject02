package controller;

import java.util.List;

import model.generate.HttpDownTemplet;
import model.vo.UrlInfo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.HttpDownService;
import service.generate.HttpDownTempletGenService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/HttpDownController")
public class HttpDownController extends BaseController{
	
	HttpDownService httpDownService = new HttpDownService();
	HttpDownTempletGenService httpDownTempletGenService = new HttpDownTempletGenService();
	
	//获取页面
	@RequestMapping("/httpDown_page.do")
	public String httpDown_page(){
		return "httpDown.html";
	}
	
	//获取所有模板
	@RequestMapping("/getAllTemplet.do")
	@ResponseBody
	public void getAllTemplet(){
		List<HttpDownTemplet> list = httpDownTempletGenService.findAll();
		writeJsonToPage(list);
	}
	
	//批量下载_检测
	@RequestMapping("/check.do")
	@ResponseBody
	public void check(String baseUrl,String encoding,int runInterval,String regex,String nameRegex,String nextLayer,String nextPage){
		String[] reArr = regex.split("[\r\n]+");
		String[] nextLayerArr = nextLayer.split(",");
		String[] nextPageArr = nextPage.split(",");
		List<List<UrlInfo>> urlList = httpDownService.check(baseUrl, reArr, nextLayerArr, nextPageArr, nameRegex, encoding, new Integer(runInterval));
		writeJsonToPage(urlList);
	}
	
	//批量下载_下载
	@RequestMapping("/down.do")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public void down(String savePath,String newDirNum,String checkResult){
		String[] newDirNumArr = newDirNum.split(",");
		List<List> checkList = JsonUtils.parseJson2List(checkResult, List.class);
		httpDownService.down(savePath, newDirNumArr, checkList);
		writeStrToPage("sucess");
	}
	
	//批量下载_下载结果查询
	@RequestMapping("/query.do")
	@ResponseBody
	public void query(){
		Boolean[] arr = httpDownService.getDownResult();
		writeJsonToPage(arr);
	}
	
	//批量下载_停止
	@RequestMapping("/stop.do")
	@ResponseBody
	public void stop(){
		HttpDownService.stopTask();
	}
}