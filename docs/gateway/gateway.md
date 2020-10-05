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
    # 访问${gateway_url}/{微服务X}/**  会转发到微服务X的/**路径
    spring.cloud.gateway.discovery.locator.enabled=true
    ```
3. 路由谓词工厂：http://www.imooc.com/article/290804
4. 自定义predicate
    ```text
    添加配置
    predicates:
       - TimeBetween=上午9:00,下午17:00
    编写predicate工厂类
    @Component
    public class TimeBetweenRoutePredicateFactory extends
            AbstractRoutePredicateFactory<TimeBetweenRoutePredicateFactory.TimeBetweenConfig> {
        public TimeBetweenRoutePredicateFactory() {
            super(TimeBetweenConfig.class);
        }
        @Override
        public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
            LocalTime start = config.getStart();
            LocalTime end = config.getEnd();
            return exchange -> {
                LocalTime now = LocalTime.now() ;
                return now.isAfter(start) && now.isBefore(end) ;
            } ;
        }
        //配置类-TimeBetweenConfig与配置文件的映射关系的配置
        // 返回配置类field的名称
        @Override
        public List<String> shortcutFieldOrder() {
            return Arrays.asList("start","end");
        }
        @Data
        static class TimeBetweenConfig{
            private LocalTime start ;
            private LocalTime end ;
        }
        public static void main(String[] args) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
            System.out.println(dateTimeFormatter.format(LocalTime.now()));
        }
    }
    ```
5. 过滤器工厂详解：http://www.imooc.com/article/290816
6. spring cloud内置全局过滤器：Spring Cloud Gateway-全局过滤器 （重点需要看）
7. spring cloud整合sentinel
8. Spring Cloud Gateway监控：http://www.imooc.com/article/290822
    ```text
    http://localhost:8040/actuator/gateway/globalfilters
    ```
9. 调试排除总结：http://www.imooc.com/article/290824
10 过滤器执行顺序
11 限流详解：http://www.imooc.com/article/290828

 
