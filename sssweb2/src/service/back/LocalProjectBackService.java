package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.LocalProject;
import model.vo.PageResult;
import utils.Page;
import dao.generate.LocalProjectGenDao;

public class LocalProjectBackService{

	LocalProjectGenDao localProjectGenDao = new LocalProjectGenDao();
	
	//条件查询(分页)
	public PageResult<LocalProject> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<LocalProject> page = new Page<LocalProject>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		localProjectGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<LocalProject>(page);
	}
	
}