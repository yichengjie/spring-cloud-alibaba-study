package com.yicj.contentcenter;

import com.yicj.contentcenter.auth.RestTemplateTokenRelayInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Collections;
import java.util.List;

@EnableFeignClients
@SpringBootApplication
// 扫描mybatis哪些包里面的接口
@MapperScan(basePackages = "com.yicj.contentcenter.dao")
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args) ;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateTokenRelayInterceptor()));
        return restTemplate ;
    }
}
