package service.generate;

import java.util.List;

import model.generate.CodeGenerateTemplet;

import dao.generate.CodeGenerateTempletGenDao;

public class CodeGenerateTempletGenService{

	CodeGenerateTempletGenDao codeGenerateTempletGenDao = new CodeGenerateTempletGenDao();
	
	//通过id查询对象
	public CodeGenerateTemplet findById(Integer id) {
		return codeGenerateTempletGenDao.findById(id);
	}
	
	//获取全部
	public List<CodeGenerateTemplet> findAll(){
		return codeGenerateTempletGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(CodeGenerateTemplet codeGenerateTemplet) {
		codeGenerateTempletGenDao.updateAndCommit(codeGenerateTemplet);
	}
	
	//保存
	public void save(CodeGenerateTemplet codeGenerateTemplet) {
		codeGenerateTempletGenDao.saveAndCommit(codeGenerateTemplet);
	}
	
	//删除
	public void delete(CodeGenerateTemplet codeGenerateTemplet){
		codeGenerateTempletGenDao.deleteAndCommit(codeGenerateTemplet);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		codeGenerateTempletGenDao.deleteByIDsAndCommit(ids);
	}
	
}