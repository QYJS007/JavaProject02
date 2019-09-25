package service.generate;

import java.util.List;

import model.generate.DbInfo;

import dao.generate.DbInfoGenDao;

public class DbInfoGenService{

	DbInfoGenDao dbInfoGenDao = new DbInfoGenDao();
	
	//通过id查询对象
	public DbInfo findById(Integer id) {
		return dbInfoGenDao.findById(id);
	}
	
	//获取全部
	public List<DbInfo> findAll(){
		return dbInfoGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(DbInfo dbInfo) {
		dbInfoGenDao.updateAndCommit(dbInfo);
	}
	
	//保存
	public void save(DbInfo dbInfo) {
		dbInfoGenDao.saveAndCommit(dbInfo);
	}
	
	//删除
	public void delete(DbInfo dbInfo){
		dbInfoGenDao.deleteAndCommit(dbInfo);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		dbInfoGenDao.deleteByIDsAndCommit(ids);
	}
	
}