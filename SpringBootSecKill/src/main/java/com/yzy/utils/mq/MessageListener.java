package com.yzy.utils.mq;
 
import org.apache.commons.collections.functors.TruePredicate;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.amqp.core.Binding;  
import org.springframework.amqp.core.BindingBuilder;  
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;  
import org.springframework.amqp.rabbit.annotation.RabbitHandler;  
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.rabbitmq.client.AMQP.Channel;
import com.yzy.entity.Seckill;  


		/**
		 * 当队列中有数据的时候自动执行，不是等秒杀活动结束才执行
		 * 
		 * 生产者确认，消费者确认，持久化三大法宝，保证redis中扣减库存之后订单一定要落地(消息一定要被消费)
		 * @return
		 */
		
		//1.redis中的库存信息更新mysql
		

//http://blog.csdn.net/zh350229319/article/details/

@Component
public class MessageListener implements ChannelAwareMessageListener{  

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);  

  /*@RabbitHandler  
  public void process(@Payload SuccessKilledMessage foo) {  
      LOGGER.info("Listener: " + foo.getUserPhone());  
     //如何进行消费的应答，保证没有被应答的消息可以投递给其他的消费者
  }*/

	@Override
	public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
		// TODO Auto-generated method stub
      
		//如果不进行ack的话，消息会一直持久化在队列里面
		//如果该消费者拒绝了某几个消息，rabbitmq会把他投递给其他消费者
		//多个消费者之间默认是轮询的方式分配给多个消费者
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		
		
		byte[] msgBytes = message.getBody();
		RuntimeSchema<SuccessKilledMessage> schema = RuntimeSchema.createFrom(SuccessKilledMessage.class);
		  
		SuccessKilledMessage sm = schema.newMessage();
		ProtostuffIOUtil.mergeFrom(msgBytes, sm, schema);
		
		LOGGER.info("onMessage==============+++++++=: "  + sm.getNumber());
	}  
}  
