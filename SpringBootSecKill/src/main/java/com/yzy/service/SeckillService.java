package com.yzy.service;

import com.yzy.dto.Exposer;
import com.yzy.dto.SeckillExecution;
import com.yzy.entity.Seckill;
import com.yzy.exception.RepeatKillException;
import com.yzy.exception.SeckillCloseException;
import com.yzy.exception.SeckillException;

import java.util.List;


/**
 * 业务接口：站在"使用者"角度设计接口 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 * 
 * @author 李奕锋
 */
public interface SeckillService {

	/**
	 * 查询所有秒杀记录
	 * 
	 * @return
	 */
	List<Seckill> getSeckillList();

	/**
	 * 查询单个秒杀记录
	 * 
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);

	/**
	 * 秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀时间
	 * 
	 * @param seckillId
	 * @return
	 */
	Exposer exportSeckillUrl(long seckillId);

	/**
	 * 执行秒杀操作
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillException
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException;

	/**
	 * 执行秒杀操作by存储过程
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillException
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 */
	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException;
	
	/**
	 * 秒杀开始之前把数据库中的库存信息写入redis
	 */
	void syncRedisFromDB();
	
	/**
	 * 秒杀的时候通过redis的原子操作把库存信息减1，把秒杀的结果写入rabbitmq
	 * 
	 * 秒杀结束以后把redis的库存信息和mq的订单信息写入mysql
	 * 
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
    SeckillExecution executeSeckillByRedis(long seckillId, long userPhone, String md5);

	void syncDBFromRedis();
}
