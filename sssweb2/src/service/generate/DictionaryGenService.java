package service.generate;

import java.util.List;

import model.generate.Dictionary;

import dao.generate.DictionaryGenDao;

public class DictionaryGenService{

	DictionaryGenDao dictionaryGenDao = new DictionaryGenDao();
	
	//通过id查询对象
	public Dictionary findById(Integer id) {
		return dictionaryGenDao.findById(id);
	}
	
	//获取全部
	public List<Dictionary> findAll(){
		return dictionaryGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(Dictionary dictionary) {
		dictionaryGenDao.updateAndCommit(dictionary);
	}
	
	//保存
	public void save(Dictionary dictionary) {
		dictionaryGenDao.saveAndCommit(dictionary);
	}
	
	//删除
	public void delete(Dictionary dictionary){
		dictionaryGenDao.deleteAndCommit(dictionary);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		dictionaryGenDao.deleteByIDsAndCommit(ids);
	}
	
}