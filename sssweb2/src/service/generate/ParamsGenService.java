package service.generate;

import java.util.List;

import model.generate.Params;

import dao.generate.ParamsGenDao;

public class ParamsGenService{

	ParamsGenDao paramsGenDao = new ParamsGenDao();
	
	//通过id查询对象
	public Params findById(Integer id) {
		return paramsGenDao.findById(id);
	}
	
	//获取全部
	public List<Params> findAll(){
		return paramsGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(Params params) {
		paramsGenDao.updateAndCommit(params);
	}
	
	//保存
	public void save(Params params) {
		paramsGenDao.saveAndCommit(params);
	}
	
	//删除
	public void delete(Params params){
		paramsGenDao.deleteAndCommit(params);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		paramsGenDao.deleteByIDsAndCommit(ids);
	}
	
}