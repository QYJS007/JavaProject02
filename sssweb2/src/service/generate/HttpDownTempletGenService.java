package service.generate;

import java.util.List;

import model.generate.HttpDownTemplet;

import dao.generate.HttpDownTempletGenDao;

public class HttpDownTempletGenService{

	HttpDownTempletGenDao httpDownTempletGenDao = new HttpDownTempletGenDao();
	
	//通过id查询对象
	public HttpDownTemplet findById(Integer id) {
		return httpDownTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<HttpDownTemplet> findAll(){
		return httpDownTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(HttpDownTemplet httpDownTemplet) {
		httpDownTempletGenDao.updateAndCommit(httpDownTemplet);
	}
	
	//保存
	public void save(HttpDownTemplet httpDownTemplet) {
		httpDownTempletGenDao.saveAndCommit(httpDownTemplet);
	}
	
	//删除
	public void delete(HttpDownTemplet httpDownTemplet){
		httpDownTempletGenDao.deleteAndCommit(httpDownTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		httpDownTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}