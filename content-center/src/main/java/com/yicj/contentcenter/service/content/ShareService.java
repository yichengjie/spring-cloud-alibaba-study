package com.yicj.contentcenter.service.content;

import com.yicj.contentcenter.dao.content.ShareMapper;
import com.yicj.contentcenter.domain.dto.content.ShareDTO;
import com.yicj.contentcenter.domain.dto.user.UserDTO;
import com.yicj.contentcenter.domain.entity.content.Share;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {
    private final ShareMapper shareMapper ;
    private final   RestTemplate restTemplate;

    public ShareDTO findById(Integer id){
        // 1. 查询share
        Share share = shareMapper.selectByPrimaryKey(id);
        // 发布人id
        Integer userId = share.getUserId();
        //2. 调用用户微服务的/users/{userId}
        UserDTO userDTO = restTemplate.getForObject("http://localhost:8080/users/{id}", UserDTO.class, 1);
        //3. 组装信息返回
        ShareDTO shareDTO = new ShareDTO() ;
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO ;
    }


}
