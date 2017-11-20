package com.yzy.utils;
import com.yzy.dto.SeckillExecution;
import com.yzy.entity.Seckill;
import com.yzy.entity.SuccessKilled;
import com.yzy.enums.SeckillStateEnum;
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

	private RuntimeSchema<SuccessKilled> schema2 = RuntimeSchema.createFrom(SuccessKilled.class);

	
	
	public RedisUtil(){
		jedisPool = new JedisPool("127.0.0.1",6379);
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
	
	/**
	 * 缓存秒杀成功的信息到redis
	 * @param userPhone
	 * @param seckill
	 */
	public String putSuccessSeckill(SuccessKilled successKilled){
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "successSeckill:" + successKilled.getUserPhone()
				              + "-" + successKilled.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(successKilled, schema2,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// 1.订单记录就不能设置超时时间了，如果redis缓存还没有被落地到MySQL，过期了怎么办？
				// 2.如何保证生产者产生的订单记录成功发送到mq，并且消费者把消息落地到mysql.
				int timeout = 60 * 60;  //一个小时,
				//要保证一个小时内消息发送到mq(即发送消息要有重试机制),如果确认mq挂了，那么直接插入mysql
				
				//发送者手动确认模式，保证消息发送到mq以后才能，
				//消费者
				
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
	
	public SuccessKilled getSuccessSeckill(long userPhone,long seckillId) {
		// redis操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "successSeckill:" + userPhone + "-" + seckillId;
				byte[] bytes = jedis.get(key.getBytes());
				if (bytes != null) {
					SuccessKilled seckill = schema2.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema2);
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

	
	

}
