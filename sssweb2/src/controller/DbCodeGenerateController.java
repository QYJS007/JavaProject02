package controller;

import java.util.List;
import java.util.Set;

import model.generate.DbCodeGenerateTemplet;
import model.generate.DbInfo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.DbCodeGenerateService;
import service.generate.DbCodeGenerateTempletGenService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/DbCodeGenerateController")
public class DbCodeGenerateController extends BaseController{
	
	DbCodeGenerateService dbCodeGenerateService = new DbCodeGenerateService();
	DbCodeGenerateTempletGenService dbCodeGenerateTempletGenService = new DbCodeGenerateTempletGenService();
	
	//返回模板代码生成功能页面
	@RequestMapping("/dbCode_page.do")
	public String dbcode_page(){
		return "index2/dbCodeGenerate.html";
	}
	
	//获取所有数据库信息
	@RequestMapping("/queryAllDbInfo.do")
	@ResponseBody
	public void queryAllDbInfo(){
		List<DbInfo> list = dbCodeGenerateService.queryAllDbInfo();
		String jsonStr = JsonUtils.parseObjectWithIgnoreNull(list, null);
		writeStrToPage(jsonStr);
	}
	
	//获取所有列名
	@RequestMapping("/queryAllColumnName.do")
	@ResponseBody
	public void queryAllColumnName(String dbInfoName, String tableName){
		Set<String> set = dbCodeGenerateService.queryAllColumnName(dbInfoName, tableName);
		writeJsonToPage(set);
	}
	
	//获取指定type的模板
	@RequestMapping("/queryTempletByType.do")
	@ResponseBody
	public void queryTempletByType(String type){
		List<DbCodeGenerateTemplet> list = dbCodeGenerateService.queryTempletByType(type);
		writeJsonToPage(list);
	}
	
	//生成代码
	@RequestMapping("/generation.do")
	@ResponseBody
	public void generation(String dbInfoName, String tableName, String columnNameArr, String templet, String paramStr){
		String code = dbCodeGenerateService.generation(dbInfoName, tableName, columnNameArr, templet, paramStr);
		writeStrToPage(code);
	}
	
}