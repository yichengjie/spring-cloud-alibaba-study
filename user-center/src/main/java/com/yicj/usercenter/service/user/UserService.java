package com.yicj.usercenter.service.user;

import com.yicj.usercenter.dao.user.UserMapper;
import com.yicj.usercenter.domain.dto.user.UserLoginDTO;
import com.yicj.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper userMapper ;

    public User findById(Integer id){
        // select * FROM USER where id = #{id}
        return userMapper.selectByPrimaryKey(id);
    }

    public User login(UserLoginDTO loginDTO, String openId){
        User paramUser = User.builder().wxId(openId).build();
        User user = this.userMapper.selectOne(paramUser);
        if (user == null){
            User userToSave = User.builder().wxId(openId)
                    .bonus(300)
                    .wxNickname(loginDTO.getWxNickname())
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .roles("user")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            this.userMapper.insertSelective(userToSave) ;
            return userToSave ;
        }
        return user ;
    }
}
