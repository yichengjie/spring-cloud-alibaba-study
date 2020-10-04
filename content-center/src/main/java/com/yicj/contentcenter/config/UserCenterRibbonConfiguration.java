package com.yicj.contentcenter.config;

import com.yicj.ribbonconfiguration.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

//@Configuration
// name表明是对user-center进行的配置
//@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
// 全局ribbon配置
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {

}
