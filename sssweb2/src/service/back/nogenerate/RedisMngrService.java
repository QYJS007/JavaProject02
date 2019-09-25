package service.back.nogenerate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import model.generate.RedisInfo;
import model.vo.MessageResult;
import model.vo.RedisKeyVo;
import redis.clients.jedis.Jedis;
import utils.RegexUtils;
import dao.generate.RedisInfoGenDao;

public class RedisMngrService {
	
	private RedisInfo redisInfo = null;
	public RedisMngrService(String redisInfoName){
		List<RedisInfo> list = new RedisInfoGenDao().findCollectionByConditionNoPage(" and name=?", new Object[]{redisInfoName}, null);
		if(list.size()==0){
			throw new RuntimeException("没有查询到指定名称的redis信息:"+redisInfoName);
		}
		redisInfo = list.get(0);
	}
	
	/**
	 * 获得redis的所有库号
	 * @author likaihao
	 * @date 2016年9月11日 上午9:42:05
	 * @return
	 */
	public List<String> getAllDbNo(){
		//获得连接
		Jedis jedis = getJedis();
		//获得所有表名
		List<String> dbNo = RegexUtils.getSubstrByRegexReturnList(jedis.info(), "\r\ndb(\\d+):");
		//释放连接
		jedis.disconnect();
		return dbNo;
	}
	
	/**
	 * 查询所有key
	 * @author likaihao
	 * @date 2016年9月11日 上午9:32:30
	 * @param dbNo
	 * @param pattern
	 * @param showNum 显示条数
	 * @return
	 */
	public List<RedisKeyVo> getKeyValue(Integer dbNo, String pattern, boolean queryTime, boolean queryValue, Integer showNum){
		Jedis jedis = null;
		try {
			List<RedisKeyVo> list = new ArrayList<RedisKeyVo>();
			//获得连接
			jedis = getJedis();
			jedis.select(dbNo);
			//获得key
			if(pattern==null || pattern.length()==0){
				pattern = "*";
			}else{
				pattern = pattern+"*";
			}
			pattern = pattern.replaceAll("([\\[\\]])", "\\\\$1");
			Set<String> keySet = jedis.keys(pattern);
			
			//判断是否达到最大结果数
			List<String> keyList = new ArrayList<String>();
			for(String key : keySet){
				keyList.add(key);
				if(keyList.size()>=showNum){
					break;
				}
			}
			
			//查询过期时间和值
			List<String> valueList = null;
			if(queryValue){
				try {
					valueList = jedis.mget(keyList.toArray(new String[0]));
				} catch (Exception e) {
					throw new RuntimeException("查询值失败");
				}
			}
			for(int i=0; i<keyList.size(); i++){
				String key = keyList.get(i);
				
				RedisKeyVo vo = new RedisKeyVo();
				vo.setKey(key);
				
				//设置过期时间
				if(queryTime){
					Long expire = jedis.ttl(key);
					if(expire!=-1){
						vo.setExpireDate(new Date(new Date().getTime()+expire*1000));
					}
				}
				
				//设置值
				if(valueList!=null){
					vo.setValue(valueList.get(i));
				}
				
				//添加
				list.add(vo);
			}
			return list;
		} finally{
			//释放连接
			if(jedis!=null){
				jedis.disconnect();
			}
		}
	}
	
	/**
	 * 查询指定模式key的个数
	 * @author likaihao
	 * @date 2016年9月11日 上午9:32:30
	 * @param dbNo
	 * @param pattern
	 * @return
	 */
	public int queryKeyCount(Integer dbNo, String pattern){
		//获得连接
		Jedis jedis = getJedis();
		jedis.select(dbNo);
		//获得key
		if(pattern==null || pattern.length()==0){
			pattern = "*";
		}else{
			pattern = pattern+"*";
		}
		pattern = pattern.replaceAll("([\\[\\]])", "\\\\$1");
		Set<String> keySet = jedis.keys(pattern);
		//释放连接
		jedis.disconnect();
		return keySet.size();
	}
	
	/**
	 * 删除指定key
	 * @author likaihao
	 * @date 2016年9月11日 上午9:48:32
	 * @param dbNo
	 * @param keyList
	 * @return
	 */
	public MessageResult delete(Integer dbNo, String[] keyArr){
		//获得连接
		Jedis jedis = getJedis();
		jedis.select(dbNo);
		//删除
		jedis.del(keyArr);
		//释放连接
		jedis.disconnect();
		return new MessageResult(true, "操作成功");
	}
	
	/**
	 * 清库
	 * @author likaihao
	 * @date 2016年9月11日 上午9:48:32
	 * @param dbNo
	 * @param keyList
	 * @return
	 */
	public MessageResult flushDB(Integer dbNo){
		//获得连接
		Jedis jedis = getJedis();
		jedis.select(dbNo);
		//清库
		jedis.flushDB();
		//释放连接
		jedis.disconnect();
		return new MessageResult(true, "操作成功");
	}
	
	/**
	 * 获得redis连接
	 * @author likaihao
	 * @date 2016年9月11日 上午9:49:23
	 * @return
	 */
	private Jedis getJedis(){
		Jedis jedis = new Jedis(redisInfo.getIp(),redisInfo.getPort());
		jedis.auth(redisInfo.getPassword());
		return jedis;
	}
}