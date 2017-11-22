package com.spring.artifactid;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yzy.App;
import com.yzy.utils.mq.MessageSender;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class RedisTest {

	    @Autowired
	    MessageSender messageSender;
	    @Test
	    public void testRabbitmq() throws Exception {
	        messageSender.send("hello yzy");
	    }
	    
	    @Test
	    public void testRedis() throws Exception{
	    	
	    }
}
