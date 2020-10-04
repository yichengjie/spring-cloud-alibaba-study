package com.yicj.contentcenter.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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



