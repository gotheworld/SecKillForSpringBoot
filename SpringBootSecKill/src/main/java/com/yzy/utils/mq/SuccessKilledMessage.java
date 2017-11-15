package com.yzy.utils.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

public class SuccessKilledMessage {
	public SuccessKilledMessage(long userPhone, long seckillId,
			int number) {
		this.userPhone = userPhone;
		this.seckillId = seckillId;
		this.number = number;
	}

	private static final long serialVersionUID = 1L;
	private long userPhone;
	private long seckillId;
	private int number;//redis中的剩余库存数量
}
