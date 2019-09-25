package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.DbInfo;
import model.vo.PageResult;
import utils.Page;
import dao.generate.DbInfoGenDao;

public class DbInfoBackService{

	DbInfoGenDao dbInfoGenDao = new DbInfoGenDao();
	
	//条件查询(分页)
	public PageResult<DbInfo> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<DbInfo> page = new Page<DbInfo>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		String ip = (String)paramMap.get("ip");
		if(ip!=null && ip.length()>0){
			condition += " and ip like ?";
			paramList.add('%'+ip+'%');
		}

		String dbname = (String)paramMap.get("dbname");
		if(dbname!=null && dbname.length()>0){
			condition += " and dbname like ?";
			paramList.add('%'+dbname+'%');
		}

		dbInfoGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<DbInfo>(page);
	}
	
}