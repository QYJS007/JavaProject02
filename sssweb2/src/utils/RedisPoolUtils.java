package utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

class RedisPoolUtils {
	
	private static JedisPool jedisPool = null;
	
	/**
	 * 初始化Redis连接池
	 */
//    static {
//		try {
//			//连接信息
//            String host = SystemConf.getResourceValue("redis.host");
//            String port = SystemConf.getResourceValue("redis.port");
//            String pass = SystemConf.getResourceValue("redis.pass");
//            
//            //配置信息
//            String maxIdle = SystemConf.getResourceValue("redis.maxIdle");
//            String maxActive = SystemConf.getResourceValue("redis.maxActive");
//            String maxWait = SystemConf.getResourceValue("redis.maxWait");
//            String testOnBorrow = SystemConf.getResourceValue("redis.testOnBorrow");
//            
//            JedisPoolConfig config = new JedisPoolConfig();
//            config.setMaxActive(new Integer(maxIdle));
//            config.setMaxIdle(new Integer(maxActive));
//            config.setMaxWait(new Integer(maxWait));
//            config.setTestOnBorrow(new Boolean(testOnBorrow));
//            
//            //连接
//            if(pass!=null && pass.length()>0){
//    			jedisPool = new JedisPool(config,host,new Integer(port),3000,pass);
//        	}else{
//    			jedisPool = new JedisPool(config,host,new Integer(port),3000);
//        	}
//    	} catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//    	}
//	}
	
	/**
	 * 获取Jedis实例
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool == null) {
				throw new RuntimeException("获取redis连接失败,连接池为空");
			}
			Jedis resource = jedisPool.getResource();
			return resource;
		} catch (Exception e) {
			throw new RuntimeException("获取redis连接失败",e);
		}
	}
	
	/**
	 * 释放jedis资源
	 * @param jedis
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
}