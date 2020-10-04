package com.yicj.contentcenter.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
