package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;


public class RedisUtils {
	
	public enum RedisDB{
		user(1);
		
		private Integer dbno;
		RedisDB(int dbno){
			this.dbno = dbno;
		}
		
		public Integer getDbno(){
			return dbno;
		}
		
	}
	
	
	/**
	 * 设置string类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:10:26
	 * @param redisDB
	 * @param key
	 * @param value
	 */
	public static void setString(RedisDB redisDB, String key,String value){
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
	public static void setString(RedisDB redisDB, String key, String value, int seconde){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		//设置值
		if(seconde>0){
			jedis.setex(key, seconde, value);
		}else{
			jedis.set(key, value);
		}
		//释放连接
		RedisPoolUtils.returnResource(jedis);
	}
	
	/**
	 * 获取string类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:11:29
	 * @param redisDB
	 * @param key
	 * @return
	 */
	public static String getString(RedisDB redisDB, String key){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		//设置值
		String value = jedis.get(key);
		//释放连接
		RedisPoolUtils.returnResource(jedis);
		return value;
	}
	
	/**
	 * 批量获取string类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:11:29
	 * @param redisDB
	 * @param key
	 * @return
	 */
	public static List<String> getStringBatch(RedisDB redisDB, String[] keyArr){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		//设置值
		List<String> valueList = jedis.mget(keyArr);
		//释放连接
		RedisPoolUtils.returnResource(jedis);
		return valueList;
	}
	
	/**
	 * 设置object类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:10:26
	 * @param redisDB
	 * @param key
	 * @param value
	 */
	public static void setObject(RedisDB redisDB, String key, Object value){
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
	public static void setObject(RedisDB redisDB, String key, Object value, int seconde){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		
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
		RedisPoolUtils.returnResource(jedis);
	}
	
	/**
	 * 获取string类型的值
	 * @author likaihao
	 * @date 2016年8月10日 下午2:11:29
	 * @param redisDB
	 * @param key
	 * @return
	 */
	public static Object getObject(RedisDB redisDB, String key){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		
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
		RedisPoolUtils.returnResource(jedis);
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
	public static long getTimeout(RedisDB redisDB, String key){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		//获取超时时间
		long value = jedis.ttl(key);
		//释放连接
		RedisPoolUtils.returnResource(jedis);
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
	public static Set<String> keys(RedisDB redisDB, String pattern){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		//获得key
		Set<String> keySet = jedis.keys(pattern);
		//释放连接
		RedisPoolUtils.returnResource(jedis);
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
	public static void del(RedisDB redisDB, String... keys){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		//删除值
		jedis.del(keys);
		//释放连接
		RedisPoolUtils.returnResource(jedis);
	}
	
	/**
	 * 清除库
	 * @author likaihao
	 * @date 2016年8月10日 下午2:53:19
	 * @param redisDB
	 */
	public static void flushDB(RedisDB redisDB){
		//获得连接
		Jedis jedis = RedisPoolUtils.getJedis();
		//切库
		jedis.select(RedisDB.user.getDbno());
		//清除库
		jedis.flushDB();
		//释放连接
		RedisPoolUtils.returnResource(jedis);
	}
}