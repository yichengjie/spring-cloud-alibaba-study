1. 添加ribbon依赖
2. 为RestTemplate添加注解@LoadBalanced
```text
@Bean
@LoadBalanced
public RestTemplate restTemplate(){
    return  new RestTemplate() ;
}
```
3. 用法
```text
restTemplate.getForObject("http://user-center/users/{id}", UserDTO.class, 1);
```
4. java方式细粒度配置ribbon
```text
在消费方添加@RibbonClient配置
@Configuration
// name表明是对user-center进行的配置
@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {}

编写配置类 注意包名称一定要启动类子包外，否则全局就都是用这个ribbon规则不能单独对user-center生效
@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule(){
        return new RandomRule() ;
    }
}
```
5. 配置属性方式细粒度配置ribbon,在application.yml中添加配置
```text
user-center:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```