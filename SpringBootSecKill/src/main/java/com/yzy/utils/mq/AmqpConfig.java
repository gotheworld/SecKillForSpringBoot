package com.yzy.utils.mq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;  
import org.springframework.amqp.rabbit.connection.ConnectionFactory;  
import org.springframework.amqp.rabbit.core.RabbitTemplate;  
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
        
        System.out.println("+++++++++++++++++"+addresses);
        
        connectionFactory.setUsername(username);  
        connectionFactory.setPassword(password);  
        connectionFactory.setVirtualHost(virtualHost);  
        /** 如果要进行消息回调，则这里必须要设置为true */  
        connectionFactory.setPublisherConfirms(publisherConfirms);  
        return connectionFactory;  
    }  
  
    @Bean  
    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 */  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
    public RabbitTemplate rabbitTemplate() {  
        RabbitTemplate template = new RabbitTemplate(connectionFactory());  
        return template;  
    }  
  
}  
