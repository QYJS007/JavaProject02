package service.generate;

import java.util.List;

import model.generate.RemoteCommandGroup;

import dao.generate.RemoteCommandGroupGenDao;

public class RemoteCommandGroupGenService{

	RemoteCommandGroupGenDao remoteCommandGroupGenDao = new RemoteCommandGroupGenDao();
	
	//通过id查询对象
	public RemoteCommandGroup findById(Integer id) {
		return remoteCommandGroupGenDao.findById(id);
	}
	
	//获取全部
	public List<RemoteCommandGroup> findAll(){
		return remoteCommandGroupGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(RemoteCommandGroup remoteCommandGroup) {
		remoteCommandGroupGenDao.updateAndCommit(remoteCommandGroup);
	}
	
	//保存
	public void save(RemoteCommandGroup remoteCommandGroup) {
		remoteCommandGroupGenDao.saveAndCommit(remoteCommandGroup);
	}
	
	//删除
	public void delete(RemoteCommandGroup remoteCommandGroup){
		remoteCommandGroupGenDao.deleteAndCommit(remoteCommandGroup);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		remoteCommandGroupGenDao.deleteByIDsAndCommit(ids);
	}
	
}