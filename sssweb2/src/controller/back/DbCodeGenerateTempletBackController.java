package controller.back;

import java.util.Map;

import model.generate.DbCodeGenerateTemplet;
import model.vo.PageResult;
import model.vo.MessageResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import controller.BaseController;
import service.back.DbCodeGenerateTempletBackService;
import service.generate.DbCodeGenerateTempletGenService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/DbCodeGenerateTempletBackController")
public class DbCodeGenerateTempletBackController extends BaseController{
	
	DbCodeGenerateTempletGenService dbCodeGenerateTempletGenService = new DbCodeGenerateTempletGenService();
	DbCodeGenerateTempletBackService dbCodeGenerateTempletBackService = new DbCodeGenerateTempletBackService();
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/dbCodeGenerateTemplet.jsp";
	}
	
	//条件查询
	@RequestMapping("/search.do")
	@ResponseBody
	public void search(String params, Integer pageNum, Integer pageSize){
		Map<String,Object> paramMap = JsonUtils.parseJson2Map(params, Object.class);
		PageResult<DbCodeGenerateTemplet> pageResult = dbCodeGenerateTempletBackService.findCollectionByConditionWithPage(paramMap, pageNum, pageSize);
		writeJsonToPage(pageResult);
	}
	
	//添加或修改
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(DbCodeGenerateTemplet dbCodeGenerateTemplet) throws Exception{
		if(dbCodeGenerateTemplet.getId()==null){
			dbCodeGenerateTempletGenService.save(dbCodeGenerateTemplet);
		}else{
			dbCodeGenerateTempletGenService.update(dbCodeGenerateTemplet);
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
		dbCodeGenerateTempletGenService.deleteByIDs(idArr2);
		MessageResult result = new MessageResult(true,"操作成功");
		writeJsonToPage(result);
	}
}