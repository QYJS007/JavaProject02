package service.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.generate.RemoteCommand;
import model.vo.PageResult;
import utils.Page;
import dao.generate.RemoteCommandGenDao;

public class RemoteCommandBackService{

	RemoteCommandGenDao remoteCommandGenDao = new RemoteCommandGenDao();
	
	//条件查询(分页)
	public PageResult<RemoteCommand> findCollectionByConditionWithPage(Map<String,Object> paramMap, Integer pageNum, Integer pageSize) {
		Page<RemoteCommand> page = new Page<RemoteCommand>(pageNum,pageSize);
		
		String condition = "";
		List<Object> paramList = new ArrayList<Object>();

		String commandGroupId = (String)paramMap.get("commandGroupId");
		if(commandGroupId!=null && commandGroupId.length()>0){
			condition += " and commandGroupId = ?";
			paramList.add(new Integer(commandGroupId));
		}

		String name = (String)paramMap.get("name");
		if(name!=null && name.length()>0){
			condition += " and name like ?";
			paramList.add('%'+name+'%');
		}

		String command = (String)paramMap.get("command");
		if(command!=null && command.length()>0){
			condition += " and command like ?";
			paramList.add('%'+command+'%');
		}

		String func = (String)paramMap.get("func");
		if(func!=null && func.length()>0){
			condition += " and func like ?";
			paramList.add('%'+func+'%');
		}

		remoteCommandGenDao.findCollectionByConditionWithPage(condition, paramList.toArray(), null, page);
		
		return new PageResult<RemoteCommand>(page);
	}
	
}