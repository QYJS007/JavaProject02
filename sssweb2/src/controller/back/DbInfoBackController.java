package controller.back;

import java.util.Map;

import model.generate.DbInfo;
import model.vo.MessageResult;
import model.vo.PageResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.back.DbInfoBackService;
import service.generate.DbInfoGenService;
import utils.JsonUtils;
import controller.BaseController;

@Controller
@Scope("prototype")
@RequestMapping("/DbInfoBackController")
public class DbInfoBackController extends BaseController{
	
	DbInfoGenService dbInfoGenService = new DbInfoGenService();
	DbInfoBackService dbInfoBackService = new DbInfoBackService();
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/dbInfo.jsp";
	}
	
	//条件查询
	@RequestMapping("/search.do")
	@ResponseBody
	public void search(String params, Integer pageNum, Integer pageSize){
		Map<String,Object> paramMap = JsonUtils.parseJson2Map(params, Object.class);
		PageResult<DbInfo> pageResult = dbInfoBackService.findCollectionByConditionWithPage(paramMap, pageNum, pageSize);
		writeJsonToPage(pageResult);
	}
	
	//添加或修改
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(DbInfo dbInfo) throws Exception{
		if(dbInfo.getId()==null){
			dbInfoGenService.save(dbInfo);
		}else{
			dbInfoGenService.update(dbInfo);
		}
		MessageResult result = new MessageResult(true,"操作成功");
		writeJsonToPage(result);
	}
	
	//删除
	@RequestMapping("/delete.do")
	@ResponseBody
	public void delete(String ids) throws Exception{
		String[] idArr = ids.split(",");
		Integer[] idArr2 = new Integer[idArr.length];
		for(int i=0;i<idArr.length;i++){
			idArr2[i] = new Integer(idArr[i]);
		}
		dbInfoGenService.deleteByIDs(idArr2);
		MessageResult result = new MessageResult(true,"操作成功");
		writeJsonToPage(result);
	}
}