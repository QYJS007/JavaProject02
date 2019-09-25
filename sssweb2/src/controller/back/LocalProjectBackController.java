package controller.back;

import java.util.Map;

import model.generate.LocalProject;
import model.vo.PageResult;
import model.vo.MessageResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import controller.BaseController;
import service.back.LocalProjectBackService;
import service.generate.LocalProjectGenService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/LocalProjectBackController")
public class LocalProjectBackController extends BaseController{
	
	LocalProjectGenService localProjectGenService = new LocalProjectGenService();
	LocalProjectBackService localProjectBackService = new LocalProjectBackService();
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/localProject.jsp";
	}
	
	//条件查询
	@RequestMapping("/search.do")
	@ResponseBody
	public void search(String params, Integer pageNum, Integer pageSize){
		Map<String,Object> paramMap = JsonUtils.parseJson2Map(params, Object.class);
		PageResult<LocalProject> pageResult = localProjectBackService.findCollectionByConditionWithPage(paramMap, pageNum, pageSize);
		writeJsonToPage(pageResult);
	}
	
	//添加或修改
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(LocalProject localProject) throws Exception{
		if(localProject.getId()==null){
			localProjectGenService.save(localProject);
		}else{
			localProjectGenService.update(localProject);
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
		localProjectGenService.deleteByIDs(idArr2);
		MessageResult result = new MessageResult(true,"操作成功");
		writeJsonToPage(result);
	}
}