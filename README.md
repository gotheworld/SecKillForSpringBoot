# SecKillForSpringBoot
电商抢购系统[springboot]


技术点：
0.springboot定时任务抢购之前库存信息写入redis
1.商品列表和商品详情页的静态化处理和CDN的使用
2.redis缓存商品抢购时候请求的URL
3.zookeeper分布式锁实现部署多个抢购服务实例时候的redis减库存操作
4.rabbitmq记录抢购订单记录，抢购结束后消费队列数据写入mysql

