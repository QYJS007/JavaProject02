package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.HttpDownTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.HttpDownTempletGenDao;

public class HttpDownTempletBackService{

	HttpDownTempletGenDao httpDownTempletGenDao = new HttpDownTempletGenDao();
	
	//条件查询(分页)
	public PageResult<HttpDownTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<HttpDownTemplet> page = new Page<HttpDownTemplet>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		httpDownTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<HttpDownTemplet>(page);
	}
	
}