1. 添加依赖
```text
 <dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
 </dependency>
```
2. 添加配置

```text
# spring.application.name 必须，否则nacos中无法注册    
spring:
  cloud:
    nacos:
      discovery:
        # 指定nacos server的地址，注意前面不要加协议http
        server-addr: 192.168.221.128:8848
  application:
    # 服务名称尽量用中-，不要用_，更不要用特殊字符串
    name: user-center
```