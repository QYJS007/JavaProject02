package service.generate;

import java.util.List;

import model.generate.DictionaryDetail;

import dao.generate.DictionaryDetailGenDao;

public class DictionaryDetailGenService{

	DictionaryDetailGenDao dictionaryDetailGenDao = new DictionaryDetailGenDao();
	
	//通过id查询对象
	public DictionaryDetail findById(Integer id) {
		return dictionaryDetailGenDao.findById(id);
	}
	
	//获取全部
	public List<DictionaryDetail> findAll(){
		return dictionaryDetailGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(DictionaryDetail dictionaryDetail) {
		dictionaryDetailGenDao.updateAndCommit(dictionaryDetail);
	}
	
	//保存
	public void save(DictionaryDetail dictionaryDetail) {
		dictionaryDetailGenDao.saveAndCommit(dictionaryDetail);
	}
	
	//删除
	public void delete(DictionaryDetail dictionaryDetail){
		dictionaryDetailGenDao.deleteAndCommit(dictionaryDetail);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		dictionaryDetailGenDao.deleteByIDsAndCommit(ids);
	}
	
}