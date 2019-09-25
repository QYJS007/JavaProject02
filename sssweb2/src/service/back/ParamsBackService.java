package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.Params;
import model.vo.PageResult;
import utils.Page;
import dao.generate.ParamsGenDao;

public class ParamsBackService{

	ParamsGenDao paramsGenDao = new ParamsGenDao();
	
	//条件查询(分页)
	public PageResult<Params> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<Params> page = new Page<Params>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String remark = (String)paramMap.get("remark");
		if(remark!=null && remark.length()>0){
			condition += " and remark like ?";
			paramList.add('%'+remark+'%');
		}

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		String value = (String)paramMap.get("value");
		if(value!=null && value.length()>0){
			condition += " and value like ?";
			paramList.add('%'+value+'%');
		}

		paramsGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<Params>(page);
	}
	
}