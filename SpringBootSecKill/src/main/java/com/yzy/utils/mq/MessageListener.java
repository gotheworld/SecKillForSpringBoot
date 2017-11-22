package com.yzy.utils.mq;
 
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.amqp.core.Binding;  
import org.springframework.amqp.core.BindingBuilder;  
import org.springframework.amqp.core.DirectExchange;  
import org.springframework.amqp.core.Queue;  
import org.springframework.amqp.rabbit.annotation.RabbitHandler;  
import org.springframework.amqp.rabbit.annotation.RabbitListener;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.messaging.handler.annotation.Payload;  


		/**
		 * 当队列中有数据的时候自动执行，不是等秒杀活动结束才执行
		 * 
		 * 生产者确认，消费者确认，持久化三大法宝，保证redis中扣减库存之后订单一定要落地(消息一定要被消费)
		 * @return
		 */
		
		//1.redis中的库存信息更新mysql
		
@Configuration  
@RabbitListener(queues = AmqpConfig.FOO_QUEUE)  
public class MessageListener {  

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);  

  /** 设置交换机类型  */  
  @Bean  
  public DirectExchange defaultExchange() {  
      /** 
       * DirectExchange:按照routingkey分发到指定队列 
       * TopicExchange:多关键字匹配 
       * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念 
       * HeadersExchange ：通过添加属性key-value匹配 
       */  
      return new DirectExchange(AmqpConfig.FOO_EXCHANGE);  
  }  

  @Bean  
  public Queue fooQueue() {  
      return new Queue(AmqpConfig.FOO_QUEUE);  
  }  

  @Bean  
  public Binding binding() {  
      /** 将队列绑定到交换机 */  
      return BindingBuilder
    		  .bind(fooQueue())
    		  .to(defaultExchange())
    		  .with(AmqpConfig.FOO_ROUTINGKEY);  
  }  

  @RabbitHandler  
  public void process(@Payload String foo) {  
      LOGGER.info("Listener: " + foo);  
  }  
}  
