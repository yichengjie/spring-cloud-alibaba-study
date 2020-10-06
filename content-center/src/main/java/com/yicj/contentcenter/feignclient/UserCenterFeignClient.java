package com.yicj.contentcenter.feignclient;

import com.yicj.contentcenter.config.UserCenterFeignConfiguration;
import com.yicj.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

//@FeignClient(name = "user-center", configuration = UserCenterFeignConfiguration.class)
@FeignClient(name = "user-center")
public interface UserCenterFeignClient {
    //http://user-center/users/{id}
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id) ;
}
