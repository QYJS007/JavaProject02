package service.generate;

import java.util.List;

import model.generate.QueryTemplet;

import dao.generate.QueryTempletGenDao;

public class QueryTempletGenService{

	QueryTempletGenDao queryTempletGenDao = new QueryTempletGenDao();
	
	//通过id查询对象
	public QueryTemplet findById(Integer id) {
		return queryTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<QueryTemplet> findAll(){
		return queryTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(QueryTemplet queryTemplet) {
		queryTempletGenDao.updateAndCommit(queryTemplet);
	}
	
	//保存
	public void save(QueryTemplet queryTemplet) {
		queryTempletGenDao.saveAndCommit(queryTemplet);
	}
	
	//删除
	public void delete(QueryTemplet queryTemplet){
		queryTempletGenDao.deleteAndCommit(queryTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		queryTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}