package com.yzy.utils.mq;
 
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;  
import org.springframework.amqp.rabbit.support.CorrelationData;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Component;  
  
import java.util.UUID;  
  
@Component  
public class MessageSender implements RabbitTemplate.ConfirmCallback , RabbitTemplate.ReturnCallback {  
  
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);  
  
    private RabbitTemplate rabbitTemplate;  
  
    @Autowired  
    public MessageSender(RabbitTemplate rabbitTemplate) {  
        this.rabbitTemplate = rabbitTemplate;  
        this.rabbitTemplate.setConfirmCallback(this);  
    }  
  
    public void send(String msg) {  
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());  
        LOGGER.info("send: " + correlationData.getId());  
        this.rabbitTemplate.convertAndSend(AmqpConfig.FOO_EXCHANGE, AmqpConfig.FOO_ROUTINGKEY, msg, correlationData);  
    }  
    
    public void send(byte[] msg) {  
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());  
        LOGGER.info("send: " + correlationData.getId());  
        this.rabbitTemplate.convertAndSend(AmqpConfig.FOO_EXCHANGE, AmqpConfig.FOO_ROUTINGKEY, msg, correlationData);  
    }  
    
    
    /** 发送者确认回调方法 */  
    @Override  
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {  
        LOGGER.info("confirm: " + correlationData.getId() + " ack="+ack);
       // http://www.jianshu.com/p/6579e48d18ae
        if (!ack) {
        	//投递失败，可以是rabbitmq宕机
         //   log.info("send message failed: " + cause + correlationData.toString());
        } else {
        //    retryCache.del(correlationData.getId());
        }

    }

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		// TODO Auto-generated method stub
		
	}
 
}  