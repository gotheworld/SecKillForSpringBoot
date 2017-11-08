package com.yzy.utils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

public class SuccessKilledMessage extends Message{
	public SuccessKilledMessage(byte[] body, MessageProperties messageProperties) {
		super(body, messageProperties);
		// TODO Auto-generated constructor stub
	}
	
	public SuccessKilledMessage(byte[] body, MessageProperties messageProperties, String userPhone, int seckillId,
			int number) {
		super(body, messageProperties);
		this.userPhone = userPhone;
		this.seckillId = seckillId;
		this.number = number;
	}

	private static final long serialVersionUID = 1L;
	private String userPhone;
	private int seckillId;
	private int number;//redis中的剩余库存数量
}
