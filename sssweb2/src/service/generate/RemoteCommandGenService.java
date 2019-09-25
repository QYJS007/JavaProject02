package service.generate;

import java.util.List;

import model.generate.RemoteCommand;

import dao.generate.RemoteCommandGenDao;

public class RemoteCommandGenService{

	RemoteCommandGenDao remoteCommandGenDao = new RemoteCommandGenDao();
	
	//通过id查询对象
	public RemoteCommand findById(Integer id) {
		return remoteCommandGenDao.findById(id);
	}
	
	//获取全部
	public List<RemoteCommand> findAll(){
		return remoteCommandGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(RemoteCommand remoteCommand) {
		remoteCommandGenDao.updateAndCommit(remoteCommand);
	}
	
	//保存
	public void save(RemoteCommand remoteCommand) {
		remoteCommandGenDao.saveAndCommit(remoteCommand);
	}
	
	//删除
	public void delete(RemoteCommand remoteCommand){
		remoteCommandGenDao.deleteAndCommit(remoteCommand);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		remoteCommandGenDao.deleteByIDsAndCommit(ids);
	}
	
}