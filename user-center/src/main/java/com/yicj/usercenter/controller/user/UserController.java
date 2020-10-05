package com.yicj.usercenter.controller.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.yicj.usercenter.domain.dto.user.JwtTokenResponseDTO;
import com.yicj.usercenter.domain.dto.user.LoginRespDTO;
import com.yicj.usercenter.domain.dto.user.UserLoginDTO;
import com.yicj.usercenter.domain.dto.user.UserResponseDTO;
import com.yicj.usercenter.domain.entity.user.User;
import com.yicj.usercenter.service.user.UserService;
import com.yicj.usercenter.utils.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService ;
    private final WxMaService wxMaService ;
    private final JwtOperator jwtOperator ;

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id){
        log.info("我被调用了...");
        return userService.findById(id) ;
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO loginDTO) throws WxErrorException {
        // 微信小程序服务端校验是否已经登录的结果
//        WxMaJscode2SessionResult result =
//                this.wxMaService.getUserService().getSessionInfo(loginDTO.getCode());
//        // 微信的openId，用户在微信这边的唯一标识
//        String openid = result.getOpenid();
        // 查看用户是否注册，如果没有注册就（插入）
        // 如果已经注册，就直接颁发token
        // 这里模拟以code代替openId
        User user = this.userService.login(loginDTO, loginDTO.getCode());
        // 颁发token
        Map<String,Object> userInfo = new HashMap<>(3) ;
        userInfo.put("id", user.getId()) ;
        userInfo.put("wxNickname", user.getWxNickname()) ;
        userInfo.put("role", user.getRoles()) ;
        String token = jwtOperator.generateToken(userInfo);
        log.info("用户{}登录成功，生成的token ：{}， 有效期到：{}", user.getWxNickname(), token, jwtOperator.getExpirationDateFromToken(token));
        // 构建响应
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(user.getId())
                .avatarUrl(user.getAvatarUrl())
                .bonus(user.getBonus())
                .wxNickname(user.getWxNickname())
                .build();
        JwtTokenResponseDTO jwtTokenResponseDTO = JwtTokenResponseDTO.builder()
                .expirationTime(jwtOperator.getExpirationDateFromToken(token).getTime())
                .token(token).build();

        LoginRespDTO loginRespDTO = LoginRespDTO.builder()
                .user(userResponseDTO)
                .token(jwtTokenResponseDTO)
                .build();

        return loginRespDTO ;
    }
}
