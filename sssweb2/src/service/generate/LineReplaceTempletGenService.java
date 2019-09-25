package service.generate;

import java.util.List;

import model.generate.LineReplaceTemplet;

import dao.generate.LineReplaceTempletGenDao;

public class LineReplaceTempletGenService{

	LineReplaceTempletGenDao lineReplaceTempletGenDao = new LineReplaceTempletGenDao();
	
	//通过id查询对象
	public LineReplaceTemplet findById(Integer id) {
		return lineReplaceTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<LineReplaceTemplet> findAll(){
		return lineReplaceTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(LineReplaceTemplet lineReplaceTemplet) {
		lineReplaceTempletGenDao.updateAndCommit(lineReplaceTemplet);
	}
	
	//保存
	public void save(LineReplaceTemplet lineReplaceTemplet) {
		lineReplaceTempletGenDao.saveAndCommit(lineReplaceTemplet);
	}
	
	//删除
	public void delete(LineReplaceTemplet lineReplaceTemplet){
		lineReplaceTempletGenDao.deleteAndCommit(lineReplaceTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		lineReplaceTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}