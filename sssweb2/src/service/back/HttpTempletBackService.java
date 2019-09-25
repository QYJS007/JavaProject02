package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.HttpTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.HttpTempletGenDao;

public class HttpTempletBackService{

	HttpTempletGenDao httpTempletGenDao = new HttpTempletGenDao();
	
	//条件查询(分页)
	public PageResult<HttpTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<HttpTemplet> page = new Page<HttpTemplet>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String type = (String)paramMap.get("type");
		if(type!=null && type.length()>0){
			condition += " and type like ?";
			paramList.add('%'+type+'%');
		}

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		String url = (String)paramMap.get("url");
		if(url!=null && url.length()>0){
			condition += " and url like ?";
			paramList.add('%'+url+'%');
		}

		httpTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<HttpTemplet>(page);
	}
	
}