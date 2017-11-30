package com.yzy.utils.mq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;  
import org.springframework.amqp.rabbit.connection.ConnectionFactory;  
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;  
import org.springframework.beans.factory.config.ConfigurableBeanFactory;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.context.annotation.Scope;  
  
@Configuration  
public class AmqpConfig {  
  //http://localhost:15672/  后台管理界面
	
	/*
	 *  ./src/redis-server 
	 *  
	 *  ./rabbitmq-server start
	 * 
	 * sh zkServer.sh start ../conf/zoo1.cfg 
	 * sh zkServer.sh start ../conf/zoo2.cfg 
	 * sh zkServer.sh start ../conf/zoo3.cfg 
	 * 
	 * 
	 * http://127.0.0.1:9090/login    zkui
	 * 15672  rabbitmq  guest  guest
	 * redisui
	 * 
	 *
	 */
	
    public static final String FOO_EXCHANGE   = "callback.exchange.foo";  
    public static final String FOO_ROUTINGKEY = "callback.routingkey.foo";  
    public static final String FOO_QUEUE      = "callback.queue.foo";  
  
    @Value("${spring.rabbitmq.addresses}")  
    private String addresses;  
    @Value("${spring.rabbitmq.username}")  
    private String username;  
    @Value("${spring.rabbitmq.password}")  
    private String password;  
    @Value("${spring.rabbitmq.virtual-host}")  
    private String virtualHost;  
    @Value("${spring.rabbitmq.publisher-confirms}")  
    private boolean publisherConfirms;  
  
    @Bean  
    public ConnectionFactory connectionFactory() {  
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();  
        connectionFactory.setAddresses(addresses);  
        
        connectionFactory.setUsername(username);  
        connectionFactory.setPassword(password);  
        connectionFactory.setVirtualHost(virtualHost);  
        /** 如果要进行消息回调，则这里必须要设置为true */  
        connectionFactory.setPublisherConfirms(publisherConfirms); 
        
        //防止消息丢失
        connectionFactory.setChannelCacheSize(100);
        return connectionFactory;  
    }  
    
    @Bean
    public SimpleMessageListenerContainer 
    messageListenerContainer(ConnectionFactory connectionFactory,MessageListener messageListener){
    	SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
    	container.setQueues(fooQueue());
    	container.setExposeListenerChannel(true);
    	container.setMaxConcurrentConsumers(1);
    	container.setConcurrentConsumers(1);
    	container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    	container.setMessageListener(messageListener);
    	
    	return container;
    }
    

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
        return new Queue(AmqpConfig.FOO_QUEUE /*是否持久化*/ /* ,true*/);  
    }  

    @Bean  
    public Binding binding() {  
        /** 将队列绑定到交换机 */  
        return BindingBuilder
      		  .bind(fooQueue())
      		  .to(defaultExchange())
      		  .with(AmqpConfig.FOO_ROUTINGKEY);  
    }  

  
    @Bean  
    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 */  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
    public RabbitTemplate rabbitTemplate() {  
        RabbitTemplate template = new RabbitTemplate(connectionFactory());  
        
        template.setMandatory(true);
        
        return template;  
    }  
  
}  
