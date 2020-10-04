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
6. ribbon全局配置，与细粒度配置类似使用@RibbonClients
    ```text
    // 全局ribbon配置
    @RibbonClients(defaultConfiguration = RibbonConfiguration.class)
    public class UserCenterRibbonConfiguration {}
    ```
7. ribbon饥饿加载
    ```text
    # ribbon 饥饿加载
    ribbon:
      eager-load:
        enabled: true
        # 这里可以细粒度配置哪些微服务需要饥饿加载，多个用逗号分隔
        clients: user-center
    ```
8. 自定义负载均衡（基于权重）
    ```text
    // 支持权重的负载均衡
    @Slf4j
    public class NacosWeightedRule extends AbstractLoadBalancerRule {
        @Autowired
        private NacosDiscoveryProperties nacosDiscoveryProperties ;
        @Override
        public void initWithNiwsConfig(IClientConfig clientConfig) {
            // 读取配置文件，并初始化NacosWeightedRule
            log.info("===> init..");
        }
        @Override
        public Server choose(Object key) {
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer)this.getLoadBalancer();
            // 微服务的名称
            String name = loadBalancer.getName();
            //服务发现相关的api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            try {
                // nacos client 自动通过基于权重的负载均衡算法，给我们选择一个实例
                Instance instance = namingService.selectOneHealthyInstance(name);
                log.info("选择的实例是 port = {}, instance = {}", instance.getPort(), instance);
                return new NacosServer(instance);
            }catch (NacosException e){
                return null ;
            }
        }
    }
    
    在配置类中使用负载均衡算法
    @Configuration
    public class RibbonConfiguration {
        @Bean
        public IRule ribbonRule(){
            return new NacosWeightedRule() ;
        }
    }
    ```
9. 扩展Ribbon支持Nacos权重的三种方式：http://www.imooc.com/article/288660
10 相同集群的实例优先调用
```text
// 同一个集群优先调用
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties ;
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) { }

    @Override
    public Server choose(Object key) {
        // 获取配置文件中的集群名称BJ
        String clusterName = nacosDiscoveryProperties.getClusterName();
        BaseLoadBalancer loadBalancer = (BaseLoadBalancer)this.getLoadBalancer();
        // 微服务的名称
        String name = loadBalancer.getName();
        //服务发现相关的api
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        try {
            //1. 找到指定服务的所有实例A
            List<Instance> instances = namingService.selectInstances(name, true);
            //2. 过滤出相同集群下的所有实例B
            List<Instance> sameClusterInstances = instances.stream()
                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
                    .collect(Collectors.toList());
            //3. 如果B是空，就用A
            List<Instance> instancesToBeanChosen = new ArrayList<>() ;
            if (CollectionUtils.isEmpty(sameClusterInstances)){
                instancesToBeanChosen = instances ;
                log.info("发生跨集群的调用，name = {}, clusterName = {}, instances = {}",name, clusterName, instances);
            }else {
                instancesToBeanChosen = sameClusterInstances ;
            }
            //4. 基于权重的负载均衡算法，返回1个实例
            Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToBeanChosen);
            log.info("选择的实例是： port ={}, instance ={}", instance.getPort(), instance);
            return new NacosServer(instance);
        }catch (NacosException e){
            log.error("发生异常：",e);
            return null;
        }
    }

    static class ExtendBalancer extends Balancer{
        protected static Instance getHostByRandomWeight2(List<Instance> hosts) {
            return getHostByRandomWeight(hosts) ;
        }
    }
}
```
