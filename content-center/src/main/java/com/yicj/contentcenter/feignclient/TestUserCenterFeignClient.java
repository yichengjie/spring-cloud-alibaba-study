package com.yicj.contentcenter.feignclient;

import com.yicj.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-center")
public interface TestUserCenterFeignClient {

    @GetMapping("/q")
    UserDTO query(@SpringQueryMap UserDTO user) ;
}
