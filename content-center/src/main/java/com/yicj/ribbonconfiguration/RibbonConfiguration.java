package com.yicj.ribbonconfiguration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.yicj.contentcenter.config.NacosSameClusterWeightedRule;
import com.yicj.contentcenter.config.NacosWeightedRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule(){
       // return new RandomRule() ;
       // return new NacosWeightedRule() ;
        return new NacosSameClusterWeightedRule() ;
    }
}
