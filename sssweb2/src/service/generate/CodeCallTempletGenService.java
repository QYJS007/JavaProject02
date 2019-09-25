package service.generate;

import java.util.List;

import model.generate.CodeCallTemplet;

import dao.generate.CodeCallTempletGenDao;

public class CodeCallTempletGenService{

	CodeCallTempletGenDao codeCallTempletGenDao = new CodeCallTempletGenDao();
	
	//通过id查询对象
	public CodeCallTemplet findById(Integer id) {
		return codeCallTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<CodeCallTemplet> findAll(){
		return codeCallTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(CodeCallTemplet codeCallTemplet) {
		codeCallTempletGenDao.updateAndCommit(codeCallTemplet);
	}
	
	//保存
	public void save(CodeCallTemplet codeCallTemplet) {
		codeCallTempletGenDao.saveAndCommit(codeCallTemplet);
	}
	
	//删除
	public void delete(CodeCallTemplet codeCallTemplet){
		codeCallTempletGenDao.deleteAndCommit(codeCallTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		codeCallTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}