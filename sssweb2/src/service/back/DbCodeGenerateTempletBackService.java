package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.DbCodeGenerateTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.DbCodeGenerateTempletGenDao;

public class DbCodeGenerateTempletBackService{

	DbCodeGenerateTempletGenDao dbCodeGenerateTempletGenDao = new DbCodeGenerateTempletGenDao();
	
	//条件查询(分页)
	public PageResult<DbCodeGenerateTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<DbCodeGenerateTemplet> page = new Page<DbCodeGenerateTemplet>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		String type = (String)paramMap.get("type");
		if(type!=null && type.length()>0){
			condition += " and type like ?";
			paramList.add('%'+type+'%');
		}

		String code = (String)paramMap.get("code");
		if(code!=null && code.length()>0){
			condition += " and code like ?";
			paramList.add('%'+code+'%');
		}

		String param = (String)paramMap.get("param");
		if(param!=null && param.length()>0){
			condition += " and param like ?";
			paramList.add('%'+param+'%');
		}

		dbCodeGenerateTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<DbCodeGenerateTemplet>(page);
	}
	
}