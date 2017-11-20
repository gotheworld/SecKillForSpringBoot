package com.yzy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import com.yzy.utils.RedisUtil;


@SpringBootApplication
@MapperScan("com.yzy.dao")
@EnableScheduling
public class App {
    public static void main(String[] args) throws Exception {


        SpringApplication.run(App.class, args);

    }
}
