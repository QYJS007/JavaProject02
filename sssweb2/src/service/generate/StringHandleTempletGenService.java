package service.generate;

import java.util.List;

import model.generate.StringHandleTemplet;

import dao.generate.StringHandleTempletGenDao;

public class StringHandleTempletGenService{

	StringHandleTempletGenDao stringHandleTempletGenDao = new StringHandleTempletGenDao();
	
	//通过id查询对象
	public StringHandleTemplet findById(Integer id) {
		return stringHandleTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<StringHandleTemplet> findAll(){
		return stringHandleTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(StringHandleTemplet stringHandleTemplet) {
		stringHandleTempletGenDao.updateAndCommit(stringHandleTemplet);
	}
	
	//保存
	public void save(StringHandleTemplet stringHandleTemplet) {
		stringHandleTempletGenDao.saveAndCommit(stringHandleTemplet);
	}
	
	//删除
	public void delete(StringHandleTemplet stringHandleTemplet){
		stringHandleTempletGenDao.deleteAndCommit(stringHandleTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		stringHandleTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}