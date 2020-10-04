package com.yicj.contentcenter.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

// 注意这里不要添加@Configuration,否则必须放到@ComponentScan能扫描的包以外
public class UserCenterFeignConfiguration {
    @Bean
    public Logger.Level level(){
        // 让feign打印所有请求的细节
        return Logger.Level.FULL ;
    }
}
