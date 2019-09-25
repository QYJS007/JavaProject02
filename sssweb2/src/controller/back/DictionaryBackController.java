package controller.back;

import java.util.Map;

import model.generate.Dictionary;
import model.vo.PageResult;
import model.vo.MessageResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import controller.BaseController;
import service.back.DictionaryBackService;
import service.generate.DictionaryGenService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/DictionaryBackController")
public class DictionaryBackController extends BaseController{
	
	DictionaryGenService dictionaryGenService = new DictionaryGenService();
	DictionaryBackService dictionaryBackService = new DictionaryBackService();
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/dictionary.jsp";
	}
	
	//条件查询
	@RequestMapping("/search.do")
	@ResponseBody
	public void search(String params, Integer pageNum, Integer pageSize){
		Map<String,Object> paramMap = JsonUtils.parseJson2Map(params, Object.class);
		PageResult<Dictionary> pageResult = dictionaryBackService.findCollectionByConditionWithPage(paramMap, pageNum, pageSize);
		writeJsonToPage(pageResult);
	}
	
	//添加或修改
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(Dictionary dictionary) throws Exception{
		if(dictionary.getId()==null){
			dictionaryGenService.save(dictionary);
		}else{
			dictionaryGenService.update(dictionary);
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
		dictionaryGenService.deleteByIDs(idArr2);
		MessageResult result = new MessageResult(true,"操作成功");
		writeJsonToPage(result);
	}
}