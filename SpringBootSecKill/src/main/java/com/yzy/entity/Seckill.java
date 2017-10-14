package com.yzy.entity;

import java.util.Date;

/**
 * 秒杀库存实体
 */

public class Seckill {

	private long seckillId;

	private String name;

	private int number;

	private Date start_time;

	private Date end_time;

	private Date create_time;

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getStartTime() {
		return start_time;
	}

	public void setStartTime(Date startTime) {
		this.start_time = start_time;
	}

	public Date getEndTime() {
		return end_time;
	}

	public void setEndTime(Date endTime) {
		this.end_time = endTime;
	}

	public Date getCreateTime() {
		return create_time;
	}

	public void setCreateTime(Date createTime) {
		this.create_time = createTime;
	}

	@Override
	public String toString() {
		return "Seckill [seckillId=" + seckillId + ", name=" + name + ", number=" + number + ", startTime=" + start_time
				+ ", endTime=" + end_time + ", createTime=" + create_time + "]";
	}

}
