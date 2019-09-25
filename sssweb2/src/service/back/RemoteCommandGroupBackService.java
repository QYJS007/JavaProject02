package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.RemoteCommandGroup;
import model.vo.PageResult;
import utils.Page;
import dao.generate.RemoteCommandGroupGenDao;

public class RemoteCommandGroupBackService{

	RemoteCommandGroupGenDao remoteCommandGroupGenDao = new RemoteCommandGroupGenDao();
	
	//条件查询(分页)
	public PageResult<RemoteCommandGroup> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<RemoteCommandGroup> page = new Page<RemoteCommandGroup>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		remoteCommandGroupGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<RemoteCommandGroup>(page);
	}
	
}