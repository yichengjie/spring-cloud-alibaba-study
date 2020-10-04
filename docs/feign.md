1. 添加依赖
    ```text
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    ```
2. 添加注解 @EnableFeignClients
3. 编写接口类
    ```text
    @FeignClient(name = "user-center")
    public interface UserCenterFeignClient {
    
        /**
         * http://user-center/users/{id}
         * @param id
         * @return
         */
        @GetMapping("/users/{id}")
        UserDTO findById(@PathVariable Integer id) ;
    }
    ```
 4. 在service中依赖注入声明的FeignClient
 ```text
    @Autowired
    private final UserCenterFeignClient userCenterFeignClient ;
```
5. 细粒度配置自定义-java（打印日志）
    ```text
    编写配置类
    // 注意这里不要添加@Configuration,否则必须放到@ComponentScan能扫描的包以外
    public class UserCenterFeignConfiguration {
        @Bean
        public Logger.Level level(){
            // 让feign打印所有请求的细节
            return Logger.Level.FULL ;
        }
    }
    在feign声明式接口中使用配置类
    @FeignClient(name = "user-center", configuration = UserCenterFeignConfiguration.class)
    public interface UserCenterFeignClient {
        //http://user-center/users/{id}
        @GetMapping("/users/{id}")
        UserDTO findById(@PathVariable Integer id) ;
    }
    在application.yml中配置日志级别为debug
    logging:
      level:
        com.yicj.contentcenter.feignclient.UserCenterFeignClient: debug
    ```
6. 细粒度配置自定义-属性方式
    ```text
    feign.client.config.<feignName>.loggerLevel=full   # 通常feignName即微服务的名称
    ```
7. 全局配置-- java方式,
    ```text
     //在启动类的EnableFeignClients中使用defaultConfiguration指定配置类
     @EnableFeignClients(defaultConfiguration = UserCenterFeignConfiguration.class)
    ```
8. 全局配置-- 属性方式
    ```text
    // 将细粒度属性配置微服务名称改为default即可
    feign.client.config.default.loggerLevel=full   # 通常feignName即微服务的名称
    ```
9. get多参数形式使用@SpringQueryMap
    ```text
     @GetMapping("/q")
     UserDTO query(@SpringQueryMap UserDTO user) ;
    ```
10. Feign构造多参数的请求:http://www.imooc.com/article/289000
11. 性能优化
    ```text
    添加依赖
    <dependency>
        <groupId>io.github.openfeign</groupId>
        <artifactId>feign-httpclient</artifactId>
    </dependency>
    增加配置
    feign.httpclient.enabled = true
    ```
12. 常见问题总结：http://www.imooc.com/article/289005

