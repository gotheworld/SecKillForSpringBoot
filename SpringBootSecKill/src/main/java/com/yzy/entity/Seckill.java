package com.yzy.entity;

import java.util.Date;

/**
 * 秒杀库存实体
 */

public class Seckill {

	private long seckill_id;

	private String name;

	private int number;

	private Date start_time;

	private long startTimeStamp;//数据库中并没有该字段,时间戳,毫秒数,为了前端好处理不用处理日期转时间戳

	private Date end_time;

	private long endTimeStamp;

	private Date create_time;

	private long createTimeStamp;//

	public long getEndTimeStamp() {
		return end_time.getTime();
	}

	public void setEndTimeStamp(long endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public long getStartTimeStamp() {
		return start_time.getTime();
	}

	public void setStartTimeStamp(long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}

	public long getCreateTimeStamp() {
		return create_time.getTime();
	}

	public void setCreateTimeStamp(long createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public long getSeckillId() {
		return seckill_id;
	}

	public void setSeckillId(long seckillId) {
		this.seckill_id = seckillId;
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
		return "Seckill [seckillId=" + seckill_id + ", name=" + name + ", number=" + number + ", startTime=" + start_time
				+ ", endTime=" + end_time + ", createTime=" + create_time + "]";
	}

}
