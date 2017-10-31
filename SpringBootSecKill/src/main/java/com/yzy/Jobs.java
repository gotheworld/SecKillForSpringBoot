package com.yzy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yzy.service.SeckillService;

@Component
public class Jobs {
	
	@Autowired
    SeckillService seckillService;
	
/*    public final static long ONE_Minute =  60 * 1000;

    @Scheduled(fixedDelay=ONE_Minute)
    public void fixedDelayJob(){
        System.out.println(" >>fixedDelay执行....");
    }

    @Scheduled(fixedRate=ONE_Minute)
    public void fixedRateJob(){
        System.out.println(" >>fixedRate执行....");
    }*/
	//在秒杀开始前定时任务把db中的库存信息写入redis
//  0 5 3 * * ?     每天16点25分执行
    @Scheduled(cron="0 25 16 * * ?")
    public void cronJob(){
      //  System.out.println(" >>cron执行....");
    	seckillService.syncRedisFromDB();
    }
}
