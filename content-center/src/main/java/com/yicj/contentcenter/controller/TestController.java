package com.yicj.contentcenter.controller;


import com.yicj.contentcenter.dao.content.ShareMapper;
import com.yicj.contentcenter.domain.dto.user.UserDTO;
import com.yicj.contentcenter.domain.entity.content.Share;
import com.yicj.contentcenter.feignclient.TestBaiduFeignClient;
import com.yicj.contentcenter.feignclient.TestUserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@RefreshScope
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {
    //@Autowired
    private final ShareMapper shareMapper ;
    @Autowired
    private DiscoveryClient discoveryClient ;
    @Autowired
    private TestUserCenterFeignClient testUserCenterFeignClient ;
    @Autowired
    private TestBaiduFeignClient testBaiduFeignClient ;
    @Autowired
    private RestTemplate restTemplate ;

    @GetMapping("/test")
    public List<Share> testInsert(){
        // 1. 插入
        Share share = new Share() ;
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("xxx");
        share.setCover("xxx");
        share.setAuthor("yicj");
        share.setBuyCount(1);
        shareMapper.insertSelective(share) ;
        // 2. 做查询，查询当前数据库所有share
        List<Share> shares = this.shareMapper.selectAll();
        return shares ;
    }

    /**
     * 测试服务器发现，证明内容中心总能找到用户中心
     * @return 用户中心所有实例的地址信息
     */
    @GetMapping("/test2")
    public List<ServiceInstance> getInstances(){
        // 查询指定服务的所有实例的信息
        return this.discoveryClient.getInstances("user-center");
    }

    @GetMapping("/test-get")
    public UserDTO query(UserDTO user){
        return testUserCenterFeignClient.query(user) ;
    }

    @GetMapping("/baidu")
    public String baiduIndex(){
        return testBaiduFeignClient.index() ;
    }


    @GetMapping("/tokenRelay/{userId}")
    public UserDTO tokenRelay(@PathVariable Integer userId){
        return restTemplate.getForObject("http://user-center/users/{userId}",
                UserDTO.class, userId) ;
    }

    @Value("${your.configuration}")
    private String yourConfiguration ;

    @GetMapping("/test-config")
    public String testNacosConfiguration(){
        return this.yourConfiguration ;
    }
}
