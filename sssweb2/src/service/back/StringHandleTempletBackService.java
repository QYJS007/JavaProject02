package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.StringHandleTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.StringHandleTempletGenDao;

public class StringHandleTempletBackService{

	StringHandleTempletGenDao stringHandleTempletGenDao = new StringHandleTempletGenDao();
	
	//条件查询(分页)
	public PageResult<StringHandleTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<StringHandleTemplet> page = new Page<StringHandleTemplet>(pageNum,pageSize);
		
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

		String code = (String)paramMap.get("code");
		if(code!=null && code.length()>0){
			condition += " and code like ?";
			paramList.add('%'+code+'%');
		}

		stringHandleTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<StringHandleTemplet>(page);
	}
	
}