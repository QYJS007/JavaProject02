package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.CodeCallTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.CodeCallTempletGenDao;

public class CodeCallTempletBackService{

	CodeCallTempletGenDao codeCallTempletGenDao = new CodeCallTempletGenDao();
	
	//条件查询(分页)
	public PageResult<CodeCallTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<CodeCallTemplet> page = new Page<CodeCallTemplet>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

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

		codeCallTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<CodeCallTemplet>(page);
	}
	
}