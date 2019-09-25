package controller.back;

import java.util.Map;

import model.generate.DictionaryDetail;
import model.vo.PageResult;
import model.vo.MessageResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import controller.BaseController;
import service.back.DictionaryDetailBackService;
import service.generate.DictionaryDetailGenService;
import utils.JsonUtils;

@Controller
@Scope("prototype")
@RequestMapping("/DictionaryDetailBackController")
public class DictionaryDetailBackController extends BaseController{
	
	DictionaryDetailGenService dictionaryDetailGenService = new DictionaryDetailGenService();
	DictionaryDetailBackService dictionaryDetailBackService = new DictionaryDetailBackService();
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/dictionaryDetail.jsp";
	}
	
	//条件查询
	@RequestMapping("/search.do")
	@ResponseBody
	public void search(String params, Integer pageNum, Integer pageSize){
		Map<String,Object> paramMap = JsonUtils.parseJson2Map(params, Object.class);
		PageResult<DictionaryDetail> pageResult = dictionaryDetailBackService.findCollectionByConditionWithPage(paramMap, pageNum, pageSize);
		writeJsonToPage(pageResult);
	}
	
	//添加或修改
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(DictionaryDetail dictionaryDetail) throws Exception{
		if(dictionaryDetail.getId()==null){
			dictionaryDetailGenService.save(dictionaryDetail);
		}else{
			dictionaryDetailGenService.update(dictionaryDetail);
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
		dictionaryDetailGenService.deleteByIDs(idArr2);
		MessageResult result = new MessageResult(true,"操作成功");
		writeJsonToPage(result);
	}
}