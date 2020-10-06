1. 添加依赖依赖
    ```text
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-nacos-config</artifactId>
    </dependency>
    ```
2. 配置bootstrap.yml
    ```text
    spring:
      cloud:
        nacos:
          config:
            server-addr: 192.168.221.128:8848
            file-extension: yaml
      application:
        name: content-center
      profiles:
        active: dev
    ```
3. 项目使用
    ```text
    @Value("${your.configuration}")
        private String yourConfiguration ;
    
        @GetMapping("/test-config")
        public String testNacosConfiguration(){
            return this.yourConfiguration ;
        }
    ```
4. 配置动态刷新,在配置使用类上添加@RefreshScope即可
    ```text
    @RefreshScope
    @RestController
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public class TestController 
    ```