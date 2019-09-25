package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.RemoteProject;
import model.vo.PageResult;
import utils.Page;
import dao.generate.RemoteProjectGenDao;

public class RemoteProjectBackService{

	RemoteProjectGenDao remoteProjectGenDao = new RemoteProjectGenDao();
	
	//条件查询(分页)
	public PageResult<RemoteProject> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<RemoteProject> page = new Page<RemoteProject>(pageNum,pageSize);
		
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

		String istest = (String)paramMap.get("istest");
		if(istest!=null && istest.length()>0){
			condition += " and istest = ?";
			paramList.add(new Integer(istest));
		}

		String localProject = (String)paramMap.get("localProject");
		if(localProject!=null && localProject.length()>0){
			condition += " and localProject = ?";
			paramList.add(localProject);
		}

		remoteProjectGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<RemoteProject>(page);
	}
	
}