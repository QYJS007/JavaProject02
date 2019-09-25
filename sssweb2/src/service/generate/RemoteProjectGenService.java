package service.generate;

import java.util.List;

import model.generate.RemoteProject;

import dao.generate.RemoteProjectGenDao;

public class RemoteProjectGenService{

	RemoteProjectGenDao remoteProjectGenDao = new RemoteProjectGenDao();
	
	//通过id查询对象
	public RemoteProject findById(Integer id) {
		return remoteProjectGenDao.findById(id);
	}
	
	//获取全部
	public List<RemoteProject> findAll(){
		return remoteProjectGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(RemoteProject remoteProject) {
		remoteProjectGenDao.updateAndCommit(remoteProject);
	}
	
	//保存
	public void save(RemoteProject remoteProject) {
		remoteProjectGenDao.saveAndCommit(remoteProject);
	}
	
	//删除
	public void delete(RemoteProject remoteProject){
		remoteProjectGenDao.deleteAndCommit(remoteProject);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		remoteProjectGenDao.deleteByIDsAndCommit(ids);
	}
	
}