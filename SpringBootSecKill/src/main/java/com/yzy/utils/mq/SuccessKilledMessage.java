package com.yzy.utils.mq;

import java.io.Serializable;

import org.springframework.amqp.core.Message;


public class SuccessKilledMessage  {

	private static final long serialVersionUID = 1L;
	private long userPhone;
	private long seckillId;
	
	/**
	 * 含义：库存剩余数量
	 * 
	 * 问题：如果消息中存储的是剩余库存数量的话，如果不能保证消息的消费次序的话会有很大问题：
	 * 
	 * SQL:update seckill set number = remainNumber,这个语句的执行次序会产生不同的结果
	 * 如果一个消费者宕机没有确认消费消息，那么之前分发给他的消息会一直停留在消息队列中，也有可能
	 * 分发给其他的消费者，所以消息无法保证有序.
	 * 
	 * 
	 * 所以修改该字段的含义为 某一个订单的扣减的商品数量（默认只能扣减一个商品，
	 * 这种情况下要在消息的消费端对消息进行手动去重，保证不会扣多次。
	 * 
	 * 
	 * 去重的策略就是看mysql中是否已经有了 用户号码和商品ID 的唯一性数据
	 * (userPhone,seckillId)
	 * 
	 */
	private int number;
	
	private String md5;
	


	public SuccessKilledMessage(long userPhone, long seckillId, int number, String md5) {
    
		this.userPhone = userPhone;
		this.seckillId = seckillId;
		this.number = number;
		this.md5 = md5;
	}

	public long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
}
