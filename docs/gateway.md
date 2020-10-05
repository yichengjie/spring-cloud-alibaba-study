1. 添加依赖
    ```text
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    ```
2. 添加配置
    ```text
    spring.application.name=gateway-center
    spring.cloud.nacos.discovery.server-addr=192.168.221.128:8848
    # gateway通过服务发现组件找到其他的微服务
    spring.cloud.gateway.discovery.locator.enabled=true
    ```
