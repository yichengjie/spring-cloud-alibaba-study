####  sentinel的使用
1. 添加依赖即可，无需注解，无需配置
    ```text
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    </dependency>
    ```
####  sentinel控制台的使用  
1. sentinel有控制台，可以查看项目依赖sentinel下载对应的控制台版本
2. 微服务中application.properties中添加配置
    ```text
    # 指定sentinel 控制台的地址
    spring.cloud.sentinel.transport.dashboard=192.168.221.128:8080
    ```
3. sentinel 规则参数总结: http://www.imooc.com/article/289345
4. sentinel api: http://www.imooc.com/article/289384
5. SentinelBeanPostProcessor源码学习