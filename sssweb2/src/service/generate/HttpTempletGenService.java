package service.generate;

import java.util.List;

import model.generate.HttpTemplet;

import dao.generate.HttpTempletGenDao;

public class HttpTempletGenService{

	HttpTempletGenDao httpTempletGenDao = new HttpTempletGenDao();
	
	//通过id查询对象
	public HttpTemplet findById(Integer id) {
		return httpTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<HttpTemplet> findAll(){
		return httpTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(HttpTemplet httpTemplet) {
		httpTempletGenDao.updateAndCommit(httpTemplet);
	}
	
	//保存
	public void save(HttpTemplet httpTemplet) {
		httpTempletGenDao.saveAndCommit(httpTemplet);
	}
	
	//删除
	public void delete(HttpTemplet httpTemplet){
		httpTempletGenDao.deleteAndCommit(httpTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		httpTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}