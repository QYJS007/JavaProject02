package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.RedisInfo;
import model.vo.PageResult;
import utils.Page;
import dao.generate.RedisInfoGenDao;

public class RedisInfoBackService{

	RedisInfoGenDao redisInfoGenDao = new RedisInfoGenDao();
	
	//条件查询(分页)
	public PageResult<RedisInfo> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<RedisInfo> page = new Page<RedisInfo>(pageNum,pageSize);
		
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

		String port = (String)paramMap.get("port");
		if(port!=null && port.length()>0){
			condition += " and port = ?";
			paramList.add(new Integer(port));
		}

		redisInfoGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<RedisInfo>(page);
	}
	
}