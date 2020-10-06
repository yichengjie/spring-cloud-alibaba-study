1. 常用MQ产品的对比: https://www.imooc.com/article/290040
2. RocketMQ 4.5.1安装教程：https://www.imooc.com/article/290089
3. RocketMQ控制台安装教程：https://www.imooc.com/article/290092
### spring boot整合rocket mq
1. 添加依赖，注意与服务端版本兼容性
    ```text
    <dependency>
        <groupId>org.apache.rocketmq</groupId>
        <artifactId>rocketmq-spring-boot-starter</artifactId>
    </dependency>
    ```
2. 添加配置
    ```text
    # rocket mq配置
    rocketmq.name-server=192.168.221.128:9876
    # 小坑：必须指定group，如果不指定springboot项目启动报错
    rocketmq.producer.group=test-group
    ```
3. java代码使用
    ```text
    @Autowired
    private RocketMQTemplate rocketMQTemplate ;
    
    // busi code
    UserAddBonusMsgDTO dto = UserAddBonusMsgDTO.builder().userId(share.getUserId()) .bonus(50).build() ;
    rocketMQTemplate.convertAndSend("add-bonus",dto);
    ```