package controller.back;

import java.util.Map;

import model.generate.RedisInfo;
import model.vo.MessageResult;
import model.vo.PageResult;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.back.RedisInfoBackService;
import service.generate.RedisInfoGenService;
import utils.JsonUtils;
import controller.BaseController;

@Controller
@Scope("prototype")
@RequestMapping("/RedisInfoBackController")
public class RedisInfoBackController extends BaseController{
	
	RedisInfoGenService redisInfoGenService = new RedisInfoGenService();
	RedisInfoBackService redisInfoBackService = new RedisInfoBackService();
	
	//返回页面
	@RequestMapping("/page.do")
	public String page(){
		return "back/redisInfo.jsp";
	}
	
	//条件查询
	@RequestMapping("/search.do")
	@ResponseBody
	public void search(String params, Integer pageNum, Integer pageSize){
		Map<String,Object> paramMap = JsonUtils.parseJson2Map(params, Object.class);
		PageResult<RedisInfo> pageResult = redisInfoBackService.findCollectionByConditionWithPage(paramMap, pageNum, pageSize);
		writeJsonToPage(pageResult);
	}
	
	//添加或修改
	@RequestMapping("/save.do")
	@ResponseBody
	public void save(RedisInfo redisInfo) throws Exception{
		if(redisInfo.getId()==null){
			redisInfoGenService.save(redisInfo);
		}else{
			redisInfoGenService.update(redisInfo);
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
		redisInfoGenService.deleteByIDs(idArr2);
		MessageResult result = new MessageResult(true,"操作成功");
		writeJsonToPage(result);
	}
}