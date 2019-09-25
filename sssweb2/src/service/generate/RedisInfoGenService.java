package service.generate;

import java.util.List;

import model.generate.RedisInfo;

import dao.generate.RedisInfoGenDao;

public class RedisInfoGenService{

	RedisInfoGenDao redisInfoGenDao = new RedisInfoGenDao();
	
	//通过id查询对象
	public RedisInfo findById(Integer id) {
		return redisInfoGenDao.findById(id);
	}
	
	//获取全部
	public List<RedisInfo> findAll(){
		return redisInfoGenDao.findCollectionByConditionNoPage(null, null, null);
	}
	
	//更新
	public void update(RedisInfo redisInfo) {
		redisInfoGenDao.updateAndCommit(redisInfo);
	}
	
	//保存
	public void save(RedisInfo redisInfo) {
		redisInfoGenDao.saveAndCommit(redisInfo);
	}
	
	//删除
	public void delete(RedisInfo redisInfo){
		redisInfoGenDao.deleteAndCommit(redisInfo);
	}
	
	//通过id删除记录(可以同时删除多个)
	@SuppressWarnings("all")
	public void deleteByIDs(Integer... ids) {
		redisInfoGenDao.deleteByIDsAndCommit(ids);
	}
	
}