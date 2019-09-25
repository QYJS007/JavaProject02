package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.DictionaryDetail;
import model.vo.PageResult;
import utils.Page;
import dao.generate.DictionaryDetailGenDao;

public class DictionaryDetailBackService{

	DictionaryDetailGenDao dictionaryDetailGenDao = new DictionaryDetailGenDao();
	
	//条件查询(分页)
	public PageResult<DictionaryDetail> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<DictionaryDetail> page = new Page<DictionaryDetail>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String dictionaryId = (String)paramMap.get("dictionaryId");
		if(dictionaryId!=null && dictionaryId.length()>0){
			condition += " and dictionaryId = ?";
			paramList.add(new Integer(dictionaryId));
		}

		String value = (String)paramMap.get("value");
		if(value!=null && value.length()>0){
			condition += " and value like ?";
			paramList.add('%'+value+'%');
		}

		String showText = (String)paramMap.get("showText");
		if(showText!=null && showText.length()>0){
			condition += " and showText like ?";
			paramList.add('%'+showText+'%');
		}

		dictionaryDetailGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<DictionaryDetail>(page);
	}
	
}