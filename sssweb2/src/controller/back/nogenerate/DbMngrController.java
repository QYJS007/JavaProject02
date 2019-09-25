package controller.back.nogenerate;

import java.util.List;
import java.util.Map;

import model.vo.MessageResult;
import model.vo.PageResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.back.nogenerate.DbMngrService;
import utils.JsonUtils;
import work.codegenerate.base.model.TableInfo;
import controller.BaseController;

@Controller
@Scope("prototype")
@RequestMapping("/DbMngrController")
public class DbMngrController extends BaseController{
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/dbMngr.jsp";
	}
	
	//获取所有表名
	@RequestMapping("/getAllTableName.do")
	@ResponseBody
	public void getAllTableName(String dbInfoName){
		List<String> tableNameList = new DbMngrService(dbInfoName).getAllTableName();
		writeJsonToPage(tableNameList);
	}
	
	//获取所有表名
	@RequestMapping("/getTableInfo.do")
	@ResponseBody
	public void getTableInfo(String dbInfoName, String tableName){
		TableInfo tableInfo = new DbMngrService(dbInfoName).getTableInfo(tableName);
		writeJsonToPage(tableInfo);
	}
	
	@RequestMapping("/query.do")
	@ResponseBody
	public void query(String params,Integer pageNum,Integer pageSize){
		Map<String,Object> paramMap = JsonUtils.parseJson2Map(params, Object.class);
		String dbInfoName = (String)paramMap.get("dbInfoName");
		String tableName = (String)paramMap.get("tableName");
		
		PageResult<Map<String,Object>> result = new DbMngrService(dbInfoName).query(tableName, paramMap, pageNum, pageSize);
		//id长度过长, js接收时造成精度丢失, 需要转成字符串传输
		String jsonStr = JsonUtils.parseObjectWithNumberToStr(result, null);
		writeStrToPage(jsonStr);
	}
	
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(String dbInfoName, String tableName, String operateType, String showColumn){
		String[] showColumnArr = showColumn.split(",");
		MessageResult result = new DbMngrService(dbInfoName).save(tableName, operateType, showColumnArr, request);
		writeJsonToPage(result);
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public void delete(String dbInfoName, String tableName, String ids){
		MessageResult result = new DbMngrService(dbInfoName).delete(tableName, ids);
		writeJsonToPage(result);
	}
	
}