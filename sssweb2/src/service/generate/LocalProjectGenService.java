package service.generate;

import java.util.List;

import model.generate.LocalProject;

import dao.generate.LocalProjectGenDao;

public class LocalProjectGenService{

	LocalProjectGenDao localProjectGenDao = new LocalProjectGenDao();
	
	//通过id查询对象
	public LocalProject findById(Integer id) {
		return localProjectGenDao.findById(id);
	}
	
	//获取全部
	public List<LocalProject> findAll(){
		return localProjectGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(LocalProject localProject) {
		localProjectGenDao.updateAndCommit(localProject);
	}
	
	//保存
	public void save(LocalProject localProject) {
		localProjectGenDao.saveAndCommit(localProject);
	}
	
	//删除
	public void delete(LocalProject localProject){
		localProjectGenDao.deleteAndCommit(localProject);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		localProjectGenDao.deleteByIDsAndCommit(ids);
	}
	
}