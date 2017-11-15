package com.yzy.utils.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.yzy.service.SeckillService;

public class SuccessSeckillListener implements MessageListener{

	@Autowired
    SeckillService seckillService;
	
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		/**
		 * 当队列中有数据的时候自动执行，不是等秒杀活动结束才执行
		 * 
		 * 生产者确认，消费者确认，持久化三大法宝，保证redis中扣减库存之后订单一定要落地(消息一定要被消费)
		 * @return
		 */
		
		//1.redis中的库存信息更新mysql
		
	}

}
