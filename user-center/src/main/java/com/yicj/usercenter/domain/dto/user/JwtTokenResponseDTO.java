package com.yicj.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponseDTO {
    // token
    private String token ;
    // 过期时间
    private Long expirationTime ;
}
