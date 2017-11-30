package com.yzy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yzy.service.SeckillService;

@Component
public class Jobs {
	
	@Autowired
	RedisUtil mRedisUtil;
   /* 
	Spring提供如下几个Annotation来标注Spring Bean:
		@Component标注一个普通的Spring Bean；
		@Controller：标注一个控制器组件类；
		@Service：标注一个业务逻辑组件类；
		@Repository：标注一个Dao组件；*/
   public final static long ONE_Minute =  60 * 1000;

/*  @Scheduled(fixedDelay=ONE_Minute)
    public void fixedDelayJob(){
        System.out.println(" >>fixedDelay执行....");
    }*/

    //@Scheduled(fixedRate=ONE_Minute)
   // public void fixedRateJob(){
      //  System.out.println(" >>fixedRate执行....秒杀开始之前把数据库中的商品库存信息写入redis");
        
        
    //   mRedisUtil.syncSeckillListFromMysql2Redis();
  //  }
	//在秒杀开始前定时任务把db中的库存信息写入redis
//  0 5 3 * * ?     每天16点25分执行
 //   @Scheduled(cron="0 25 16 * * ?")
  //  public void cronJob(){
      //  System.out.println(" >>cron执行....");
   // }
}
