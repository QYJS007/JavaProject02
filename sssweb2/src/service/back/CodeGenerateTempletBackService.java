package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.CodeGenerateTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.CodeGenerateTempletGenDao;

public class CodeGenerateTempletBackService{

	CodeGenerateTempletGenDao codeGenerateTempletGenDao = new CodeGenerateTempletGenDao();
	
	//条件查询(分页)
	public PageResult<CodeGenerateTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<CodeGenerateTemplet> page = new Page<CodeGenerateTemplet>(pageNum,pageSize);
		
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

		String param = (String)paramMap.get("param");
		if(param!=null && param.length()>0){
			condition += " and param like ?";
			paramList.add('%'+param+'%');
		}

		codeGenerateTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<CodeGenerateTemplet>(page);
	}
	
}