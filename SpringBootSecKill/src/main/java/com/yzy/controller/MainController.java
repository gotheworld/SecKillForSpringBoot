package com.yzy.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
public class MainController {

    @RequestMapping("/")
    public String hello(Map<String,Object> map){
        map.put("name", "[yangzhongyu]");
        return "index";//跳转到index.ftl页面
    }

}
