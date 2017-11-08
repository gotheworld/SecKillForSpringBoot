# SecKillForSpringBoot
电商抢购系统[springboot]


技术点：
0.springboot定时任务抢购之前mysql库存信息写入redis

1.商品列表和商品详情页的静态化处理和CDN的使用

2.redis缓存商品抢购时候请求的URL

3.zookeeper分布式锁实现部署多个抢购服务实例并发的redis减库存操作,并发送生成订单消息到rabbitmq

4.消费者消费rabbitmq记录抢购订单队列，消费订单数据写入mysql，redis库存信息写入mysql

需要主要的问题：

1.发送者确认模式和rabbitmq持久化，保证订单消息投递到rabbitmq

2.rabbitmq的消息有序性

