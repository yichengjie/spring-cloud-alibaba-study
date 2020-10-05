package com.yicj.usercenter.configuration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfiguration {

    @Bean
    public WxMaConfig wxMaConfig(){
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl() ;
        config.setAppid("wxe0ce9e77e6c91514");
        config.setSecret("xxx");
        return config ;
    }

    @Bean
    public WxMaService wxMaService(){
        WxMaServiceImpl service = new WxMaServiceImpl() ;
        service.setWxMaConfig(wxMaConfig());
        return service ;
    }
}
