package com.yzy.controller;

import com.yzy.dao.SeckillDao;
import com.yzy.entity.Seckill;
import com.yzy.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class DemoInfoController {

    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/test/{id}")
    @ResponseBody
    Seckill queryById(@PathVariable Integer id){
        System.out.println("controller....id="+id);
        Seckill demoInfo = seckillService.getById(id);
        return demoInfo;
    }
}
