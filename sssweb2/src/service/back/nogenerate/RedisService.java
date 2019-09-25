package service.back.nogenerate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import model.generate.RedisInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;


public class RedisService {
	
	private RedisInfo redisInfo = null;
	public RedisService(RedisInfo redisInfo){
		this.redisInfo = redisInfo;
	}
	
	/**
	 * 设置string类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:10:26
	 * @param redisDB
	 * @param key
	 * @param value
	 */
	public void setString(Integer redisDB, String key,String value){
		setString(redisDB, key, value, 0);
	}
	
	/**
	 * 设置string类型的值,同时设置过期时间(秒)
	 * @author likaihao
	 * @date 2016年8月10日 下午2:33:17
	 * @param redisDB
	 * @param key
	 * @param value
	 * @param seconde
	 */
	public void setString(Integer redisDB, String key, String value, int seconde){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		//设置值
		if(seconde>0){
			jedis.setex(key, seconde, value);
		}else{
			jedis.set(key, value);
		}
		//释放连接
		closeJedis(jedis);
	}
	
	/**
	 * 获取string类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:11:29
	 * @param redisDB
	 * @param key
	 * @return
	 */
	public String getString(Integer redisDB, String key){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		//设置值
		String value = jedis.get(key);
		//释放连接
		closeJedis(jedis);
		return value;
	}
	
	/**
	 * 设置object类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:10:26
	 * @param redisDB
	 * @param key
	 * @param value
	 */
	public void setObject(Integer redisDB, String key, Object value){
		setObject(redisDB, key, value, -1);
	}
	
	/**
	 * 设置object类型的值, 同时设置过期时间(秒)
	 * @author likaihao
	 * @date 2016年8月10日 下午2:10:26
	 * @param redisDB
	 * @param key
	 * @param value
	 * @param seconde
	 */
	public void setObject(Integer redisDB, String key, Object value, int seconde){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		
		//对象序列化保存
		try {
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(value);
			oos.close();
			
			byte[] byteArr=out.toByteArray();
			if(seconde>0){
				jedis.setex(key.getBytes(), seconde, byteArr);
			}else{
				jedis.set(key.getBytes(), byteArr);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		//释放连接
		closeJedis(jedis);
	}
	
	/**
	 * 获取string类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:11:29
	 * @param redisDB
	 * @param key
	 * @return
	 */
	public Object getObject(Integer redisDB, String key){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		
		//获取值 并进行对象反序列化
		Object obj = null;
		try {
			byte[] byteArr = jedis.get(key.getBytes());
			if(byteArr!=null){
				ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
				ObjectInputStream ois = new ObjectInputStream(bais);
				obj = ois.readObject();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		//释放连接
		closeJedis(jedis);
		return obj;
	}
	
	/**
	 * 获取值的超时时间
	 * @author likaihao
	 * @date 2016年8月10日 下午2:11:29
	 * @param redisDB
	 * @param key
	 * @return
	 */
	public long getTimeout(Integer redisDB, String key){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		//获取超时时间
		long value = jedis.ttl(key);
		//释放连接
		closeJedis(jedis);
		return value;
	}
	
	
	/**
	 * 根据规则获得key
	 * @author likaihao
	 * @date 2016年8月10日 下午2:51:55
	 * @param redisDB
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(Integer redisDB, String pattern){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		//获得key
		Set<String> keySet = jedis.keys(pattern);
		//释放连接
		closeJedis(jedis);
		return keySet;
	}
	
	/**
	 * 删除值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:11:29
	 * @param redisDB
	 * @param key
	 * @return
	 */
	public void del(Integer redisDB, String... keys){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		//删除值
		jedis.del(keys);
		//释放连接
		closeJedis(jedis);
	}
	
	/**
	 * 清除库
	 * @author likaihao
	 * @date 2016年8月10日 下午2:53:19
	 * @param redisDB
	 */
	public void flushDB(Integer redisDB){
		//获得连接
		Jedis jedis = getJedis();
		//切库
		jedis.select(redisDB);
		//清除库
		jedis.flushDB();
		//释放连接
		closeJedis(jedis);
	}
	
	public Jedis getJedis(){
		Jedis jedis = new Jedis(new JedisShardInfo(redisInfo.getIp(),redisInfo.getPort(),redisInfo.getPassword()));
		return jedis;
	}
	
	public void closeJedis(Jedis jedis){
		jedis.disconnect();
	}
	
}