package com.yicj.usercenter;

import com.yicj.usercenter.rocketmq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;

@EnableBinding({Sink.class, MySink.class})
@SpringBootApplication
// 扫描mybatis哪些包里面的接口
@MapperScan(basePackages = "com.yicj.usercenter.dao")
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args) ;
    }

}
