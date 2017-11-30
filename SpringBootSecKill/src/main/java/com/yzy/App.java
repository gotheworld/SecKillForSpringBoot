package com.yzy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import com.yzy.utils.RedisUtil;
import com.yzy.utils.mq.MessageSender;


@SpringBootApplication
@MapperScan("com.yzy.dao")
@EnableScheduling
public class App {
	
	@Autowired
	private static RedisUtil mRedisUtil;
	
    public static void main(String[] args) throws Exception {

    	mRedisUtil.syncSeckillListFromMysql2Redis();
    	
        SpringApplication.run(App.class, args);

    }
}
