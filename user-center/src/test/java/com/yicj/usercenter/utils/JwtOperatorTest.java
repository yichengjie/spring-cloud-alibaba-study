package com.yicj.usercenter.utils;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class JwtOperatorTest {
    // 1. 初始化
    private JwtOperator jwtOperator ;

    @Before
    public void before(){
        jwtOperator = new JwtOperator();
        jwtOperator.setExpirationTimeInSecond(1209600L);
        jwtOperator.setSecret("aaabbbcccdddeeefffggghhhiiijjjkkklllmmmnnnooopppqqqrrrsssttt");
    }

    @Test
    public void genToken(){
        // 2.设置用户信息
        HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
        objectObjectHashMap.put("id", "1");
        // 测试1: 生成token
        String token = jwtOperator.generateToken(objectObjectHashMap);
        // 会生成类似该字符串的内容: eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJpYXQiOjE1NjU1ODk4MTcsImV4cCI6MTU2Njc5OTQxN30.27_QgdtTg4SUgxidW6ALHFsZPgMtjCQ4ZYTRmZroKCQ
        System.out.println(token);
    }

    @Test
    public void validate(){
        // 将我改成上面生成的token!!!
        String someToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJpYXQiOjE2MDE4OTgxOTYsImV4cCI6MTYwMzEwNzc5Nn0.alcM0doFPcHm3q1SoRdWtnN_v0rZd8biNNzo4682IrM";
        // 测试2: 如果能token合法且未过期，返回true
        Boolean validateToken = jwtOperator.validateToken(someToken);
        System.out.println(validateToken);
    }

    @Test
    public void validateAndGetUserInfo(){
        // 将我改成上面生成的token!!!
        String someToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJpYXQiOjE2MDE4OTgxOTYsImV4cCI6MTYwMzEwNzc5Nn0.alcM0doFPcHm3q1SoRdWtnN_v0rZd8biNNzo4682IrM";
        // 测试3: 获取用户信息
        Claims claims = jwtOperator.getClaimsFromToken(someToken);
        System.out.println(claims);
    }

    @Test
    public void decodeUserInfo(){
        // 将我改成你生成的token的第一段（以.为边界）
        String encodedHeader = "eyJhbGciOiJIUzI1NiJ9";
        // 测试4: 解密Header
        byte[] header = Base64.decodeBase64(encodedHeader.getBytes());
        System.out.println(new String(header));
        // 将我改成你生成的token的第二段（以.为边界）
        String encodedPayload = "eyJpZCI6IjEiLCJpYXQiOjE2MDE4OTgxOTYsImV4cCI6MTYwMzEwNzc5Nn0";
        // 测试5: 解密Payload
        byte[] payload = Base64.decodeBase64(encodedPayload.getBytes());
        System.out.println(new String(payload));
    }

    @Test
    public void validate3(){
        // 测试6: 这是一个被篡改的token，因此会报异常，说明JWT是安全的
        jwtOperator.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJpYXQiOjE1NjU1ODk3MzIsImV4cCI6MTU2Njc5OTMzMn0.nDv25ex7XuTlmXgNzGX46LqMZItVFyNHQpmL9UQf-aUx");
    }

}
