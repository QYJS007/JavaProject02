package service.generate;

import java.util.List;

import model.generate.DbCodeGenerateTemplet;

import dao.generate.DbCodeGenerateTempletGenDao;

public class DbCodeGenerateTempletGenService{

	DbCodeGenerateTempletGenDao dbCodeGenerateTempletGenDao = new DbCodeGenerateTempletGenDao();
	
	//通过id查询对象
	public DbCodeGenerateTemplet findById(Integer id) {
		return dbCodeGenerateTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<DbCodeGenerateTemplet> findAll(){
		return dbCodeGenerateTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(DbCodeGenerateTemplet dbCodeGenerateTemplet) {
		dbCodeGenerateTempletGenDao.updateAndCommit(dbCodeGenerateTemplet);
	}
	
	//保存
	public void save(DbCodeGenerateTemplet dbCodeGenerateTemplet) {
		dbCodeGenerateTempletGenDao.saveAndCommit(dbCodeGenerateTemplet);
	}
	
	//删除
	public void delete(DbCodeGenerateTemplet dbCodeGenerateTemplet){
		dbCodeGenerateTempletGenDao.deleteAndCommit(dbCodeGenerateTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		dbCodeGenerateTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}