package com.yicj.usercenter.service.user;

import com.yicj.usercenter.dao.user.UserMapper;
import com.yicj.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper userMapper ;

    public User findById(Integer id){
        // select * FROM USER where id = #{id}
        return userMapper.selectByPrimaryKey(id);
    }
}
