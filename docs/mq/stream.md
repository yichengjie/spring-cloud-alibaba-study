1. 添加依赖
    ```text
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-stream-rocketmq</artifactId>
    </dependency>
    <!--spring-cloud-starter-stream-rocketmq 强制依赖hibernate-validator，
    而hibernate-validator又强制依赖validation-api，jboss-logging，classmate-->
    <dependency>
        <groupId>javax.validation</groupId>
        <artifactId>validation-api</artifactId>
    </dependency>
    <dependency>
        <groupId>org.jboss.logging</groupId>
        <artifactId>jboss-logging</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml</groupId>
        <artifactId>classmate</artifactId>
    </dependency> 
    ```
#### 生产者相关  
1. 启动类上添加注解
   ```text
   @EnableBinding(Source.class)
   ```
2. 添加配置
    ```text
    # spring cloud stream config
    spring.cloud.stream.rocketmq.binder.name-server=192.168.221.128:9876
    # 用来指定topic
    spring.cloud.stream.bindings.output.destination = stream-test-topic
    ```
3. 业务代码
    ```text
    @Autowired
    private Source source ;
    
    @GetMapping("/test-stream")
    public String testStream(){
        Message<String> message = MessageBuilder.withPayload("消息体测试").build();
        source.output().send(message) ;
        return "success" ;
    }
    ```
4. 自定义接口-发送消息
    ```text
    1. 编写接口类
       public interface MySource {
           String MY_OUTPUT = "my-output" ;
           @Output(MY_OUTPUT)
           MessageChannel output();
       }
    2. 在EnableBinding中添加MySource
       @EnableBinding({Source.class, MySource.class})
    3. 添加配置
       # 配置定义out-put
       spring.cloud.stream.bindings.my-output.destination= stream-my-topic
    4. 业务代码
     @Autowired
     private MySource mySource ;
  
     @GetMapping("/test-my-stream")
     public String testMyStream(){
         Message<String> message = MessageBuilder.withPayload("自定义消息体测试").build();
         mySource.output().send(message) ;
         return "success" ;
     }
    ```
#### 消费者相关
1. 启动类上添加注解
   @EnableBinding(Sink.class)
   ```
3. 添加配置
    ```text
    # stream config
    # spring cloud stream config
    spring.cloud.stream.rocketmq.binder.name-server=192.168.221.128:9876
    # 用来指定topic,这里的配置要与消费者的一致
    spring.cloud.stream.bindings.input.destination=stream-test-topic
    # 如果用的是RocketMQ，group一定要配置，否则应用无法启动，
    # 如果使用的是其他MQ，这里的group可以不设置
    spring.cloud.stream.bindings.input.group=binder-group
    ```
3. 业务代码
    ```text
   @Service
   public class TestStreamConsumer {
       @StreamListener(Sink.INPUT)
       public void receive(String messageBody){
           log.info("===> 通过stream收到了消息：messageBody ={}", messageBody);
       }
   }
    ```
4. 自定义接口-消息接收
    ```text
    1. 编写接口类
      public interface MySink {
          String MY_INPUT = "my-input" ;
          @Input(MY_INPUT)
          SubscribableChannel input() ;
      }
    2. 在EnableBinding中添加MySink
       @EnableBinding({Sink.class, MySink.class})
    3. 添加配置
       # 自定义接口接收消息
       spring.cloud.stream.bindings.my-input.destination=stream-my-topic
       spring.cloud.stream.bindings.my-input.group=binder-my-group
    4. 业务代码
     @Service
     public class MyTestStreamConsumer {
         @StreamListener(MySink.MY_INPUT)
         public void receive(String messageBody){
             log.info("===> 自定义接口消费：通过stream收到了消息：messageBody ={}", messageBody);
         }
     }
    ```
 5. 消息过滤：http://www.imooc.com/article/290424
 6. 异常处理：http://www.imooc.com/article/290435
 7. Spring Cloud Stream知识点盘点（重点必看）: http://www.imooc.com/article/290489