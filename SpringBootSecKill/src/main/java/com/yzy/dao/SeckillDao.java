package com.yzy.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yzy.entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 秒杀库存DAO接口
 */
public interface SeckillDao {

	/**
	 * 减库存
	 * 
	 * @param seckillId
	 * @param killTime
	 * @return 如果影响行数等于>1，表示更新的记录行数
	 */
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

	/**
	 * 根据id查询秒杀对象
	 * 
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);

	/**
	 * 根据偏移量查询秒杀商品列表
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

	/**
	 * 使用存储过程执行秒杀
	 * 
	 * @param paramMap
	 */
	void killByProcedure(Map<String, Object> paramMap);
	
	/**
	 * 
	 * @param seckillId
	 * @param num   最新的库存信息
	 */
	void updateNumber(@Param("seckillId") long seckillId, @Param("num") int num);

}
