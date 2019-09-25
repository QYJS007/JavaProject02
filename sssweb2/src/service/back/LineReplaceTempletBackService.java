package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.LineReplaceTemplet;
import model.vo.PageResult;
import utils.Page;
import dao.generate.LineReplaceTempletGenDao;

public class LineReplaceTempletBackService{

	LineReplaceTempletGenDao lineReplaceTempletGenDao = new LineReplaceTempletGenDao();
	
	//条件查询(分页)
	public PageResult<LineReplaceTemplet> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<LineReplaceTemplet> page = new Page<LineReplaceTemplet>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		lineReplaceTempletGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<LineReplaceTemplet>(page);
	}
	
}