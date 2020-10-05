package com.yicj.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    // id
    private Integer id ;
    // 头像地址
    private String avatarUrl ;
    // 积分
    private Integer bonus ;
    // 微信昵称
    private String wxNickname ;
}
