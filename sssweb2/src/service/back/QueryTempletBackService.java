package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.QueryTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.QueryTempletGenDao;

public class QueryTempletBackService{

	QueryTempletGenDao queryTempletGenDao = new QueryTempletGenDao();
	
	//条件查询(分页)
	public PageResult<QueryTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<QueryTemplet> page = new Page<QueryTemplet>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		String path = (String)paramMap.get("path");
		if(path!=null && path.length()>0){
			condition += " and path like ?";
			paramList.add('%'+path+'%');
		}

		queryTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<QueryTemplet>(page);
	}
	
}