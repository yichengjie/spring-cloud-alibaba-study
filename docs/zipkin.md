1. Zipkin Server下载与搭建:http://www.imooc.com/article/291572
2. 整合Zipkin之后的报错问题后的nacos报错：http://www.imooc.com/article/291578
#### 使用sleuth
1. 添加依赖
    ```text
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
    ```
 2. 添加配置
     ```text
    # 抽样率，默认是1.0
    spring.sleuth.sampler.probability=1.0
    ```
#### 使用zipkin
1. 添加依赖
    ```text
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zipkin</artifactId>
    </dependency>
    ```
2. 添加配置
    ```text
    # zipkin server address
    spring.zipkin.base-url=http://192.168.221.128:9411
    # 将192.168.221.128:9411作为一个纯地址，而不是微服务应用
    spring.zipkin.discovery-client-enabled=false
    ```