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
