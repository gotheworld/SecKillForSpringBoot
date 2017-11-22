package com.yzy.utils.mq;

import java.io.Serializable;


public class SuccessKilledMessage implements Serializable {
	
	
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
