package com.yzy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.yzy.dao.SeckillDao;
import com.yzy.dao.SuccessKilledDao;
import com.yzy.dto.Exposer;
import com.yzy.dto.SeckillExecution;
import com.yzy.entity.Seckill;
import com.yzy.entity.SuccessKilled;
import com.yzy.enums.SeckillStateEnum;
import com.yzy.exception.RepeatKillException;
import com.yzy.exception.SeckillCloseException;
import com.yzy.exception.SeckillException;
import com.yzy.service.SeckillService;
import com.yzy.utils.RedisUtil;
import com.yzy.utils.SuccessKilledMessage;
import com.yzy.utils.ZookeeperLock;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

//@Componet @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入Service依赖
	@Autowired
	private SeckillDao seckillDao;

	@Autowired
	private SuccessKilledDao successKilledDao;

//	@Autowired
	private RedisUtil redisDao = new RedisUtil("127.0.0.1",6379);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	// md5盐值字符串，用于混淆MD5
	private final String slat = "skdfjksjdf7787%^%^%^FSKJFK*(&&%^%&^8DF8^%^^*7hFJDHFJ";

	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 10);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	


	@Override
	public void updateNumber(long seckillId,int num) {
		seckillDao.updateNumber(seckillId, num);
	}
	

	/**
	 * 输出 : 每一个秒杀商品在前台页面都会有一个秒杀的链接
	 *
	 * 在秒杀开始之前这个链接显示距离秒杀的时间剩余,当秒杀开始以后显示秒杀的URL
	 *
	 * 该URL需要做  加密(防止URL在秒杀之前被猜到) , 防重放 ,
	 *
	 * @param seckillId
	 * @return
     */
	@Override
	public Exposer exportSeckillUrl(long seckillId) {

		// 优化点：缓存优化：超时的基础上维护一致性
		// 1.访问redis
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (seckill == null) {
			// 2.访问数据库
			seckill = seckillDao.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				// 3.访问redis
				redisDao.putSeckill(seckill);
			}
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// 系统当前时间
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			//秒杀还没有开始
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		// 转化特定字符串的过程，不可逆
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	/**
	 * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
	 * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
	 * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
	 */
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// 执行秒杀逻辑：减库存 + 记录购买行为
		Date now = new Date();
		try {
			// 记录购买行为
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			// 唯一：seckillId,userPhone
			if (insertCount <= 0) {
				// 重复秒杀
				throw new RepeatKillException("seckill repeated");
			} else {
				// 减库存，热点商品竞争
				int updateCount = seckillDao.reduceNumber(seckillId, now);
				if (updateCount <= 0) {
					// 没有更新到记录 rollback
					throw new SeckillCloseException("seckill is closed");
				} else {
					// 秒杀成功 commit
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所有编译期异常转换为运行期异常
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		// 执行存储过程，result被赋值
		try {
			seckillDao.killByProcedure(map);
			// 获取result
			int result = MapUtils.getInteger(map, "result", -2);

			if (result == 1) {
				SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
			} else {
				return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
		}
	}

	/**
	 * 
	 * 架构流程：
	 *  0.定时任务,秒杀开始之前同步mysql库存信息到redis
	 *  1.redis中减库存
		2.把秒杀的结果写入mq 
		3.消费者线程去消费mq中的数据，更新到mysql的秒杀结果表中
		4.redis中的库存信息写会mysql
		 
	 * 可能遇到的问题：
	 * 1.redis减库存的分布式锁【zk分布式锁】
	 * 2.mq记录购买行为，如何保证生产者投递消息成功（AutoAck）
	 * 3.事务问题：redis减库存成功了，但是mq记录购买行为失败怎么办？【回滚redis的库存】
	 * 4.reids库存写会mysql成功了，但是消费者没有消费mq的购买记录怎么办？【消息如何保证被消费者消费？】
	 * 
	 * 
	 */
   // 其实redis本事是不会存在并发问题的，因为他是单进程的，
//	再多的command都是one by one执行的。我们使用的时候，可能会出现并发问题，比如get和set这一对。
	//解决方案：
	//1.使用incr、decr单个命令取代get set两个命令
	//2.使用jdk5 lock，syncnized
	//3.SETNX 分布式锁(有多个机器同时运行 抢购服务的时候)
	
	private Lock lock = new ZookeeperLock();
	
	@Override
	public SeckillExecution executeSeckillByRedis(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		
		
		lock.lock();
		try {
			//重复秒杀怎么判断？？？？？？
			//redis减库存成功，发送消息失败怎么办？redis回滚吗？
			//这个地方要加上分布式锁,redis减库存有可能失败，可能库存已经为0了，可能某一个userPhone重复秒杀等
			redisDao.executeSeckillByRedis(seckillId, userPhone, md5);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
		
		int number = redisDao.getSeckill(seckillId).getNumber();//最新的库存信息
		
	//	rabbitTemplate.send(new SuccessKilledMessage(body, messageProperties));
		return null;
	}

}
