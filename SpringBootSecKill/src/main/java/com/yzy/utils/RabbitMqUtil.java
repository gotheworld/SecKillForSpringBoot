package com.yzy.utils;

import org.springframework.stereotype.Component;

@Component
public class RabbitMqUtil {

	public boolean produceMessage(){
		return false;
	}
	
	public boolean consumeMessage(){
		return false;
	}
	
	public static class SuccessKilledMessage{
		private String userPhone;
		private int seckillId;
	}
	
	
}
