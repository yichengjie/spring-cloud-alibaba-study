package com.yicj.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRespDTO {
    // token
    private JwtTokenResponseDTO token ;
    // 用户信息
    private UserResponseDTO user ;
}
