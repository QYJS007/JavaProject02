package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.Dictionary;
import model.vo.PageResult;
import utils.Page;
import dao.generate.DictionaryGenDao;

public class DictionaryBackService{

	DictionaryGenDao dictionaryGenDao = new DictionaryGenDao();
	
	//条件查询(分页)
	public PageResult<Dictionary> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<Dictionary> page = new Page<Dictionary>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		String tableName = (String)paramMap.get("tableName");
		if(tableName!=null && tableName.length()>0){
			condition += " and tableName like ?";
			paramList.add('%'+tableName+'%');
		}

		String columnName = (String)paramMap.get("columnName");
		if(columnName!=null && columnName.length()>0){
			condition += " and columnName like ?";
			paramList.add('%'+columnName+'%');
		}

		dictionaryGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<Dictionary>(page);
	}
	
}