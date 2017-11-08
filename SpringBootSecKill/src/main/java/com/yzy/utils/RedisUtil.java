package com.yzy.utils;
import com.yzy.dto.SeckillExecution;
import com.yzy.entity.Seckill;
import com.yzy.service.SeckillService;

import freemarker.ext.beans.NumberModel;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService mSeckillService;

	private final JedisPool jedisPool;

	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

	public RedisUtil(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}

	public Seckill getSeckill(long seckillId) {
		// redis操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				//>>>>> get seckill:1003
				String key = "seckill:" + seckillId;
				// 并没有实现内部序列化操作
				// get -> byte[] -> 反序列化 -> object[Seckill]
				// 采用自定义序列化
				// protostuff : pojo.
				byte[] bytes = jedis.get(key.getBytes());
				if (bytes != null) {
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					// seckill被反序列化
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String putSeckill(Seckill seckill) {
		// set Object(Seckill) -> 序列号 -> byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// 超时缓存
				int timeout = 60 * 60;  //一个小时
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 秒杀开始前把 秒杀商品信息和数量从 mysql读取到redis
	 */
	public void syncSeckillListFromMysql2Redis() {
		
		List<Seckill> list = mSeckillService.getSeckillList();
		for(Seckill seckill : list){
			putSeckill(seckill);
			System.out.println("Put Data To Redis, key is " + seckill.getSeckillId());
		}
	}
	
	public  SeckillExecution executeSeckillByRedis(long seckillId, long userPhone, String md5) {

		Seckill seckill = getSeckill(seckillId);
		int num = seckill.getNumber();
	
		if(num -1 >= 0 ){
			num = num - 1;
			seckill.setNumber(num);//减库存
			putSeckill(seckill);
		}
		
		return null;
	}

	
	

}
