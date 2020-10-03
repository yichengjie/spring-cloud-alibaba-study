package com.yicj.contentcenter.controller;


import com.yicj.contentcenter.dao.content.ShareMapper;
import com.yicj.contentcenter.domain.entity.content.Share;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private ShareMapper shareMapper ;

    @GetMapping("/test")
    public List<Share> testInsert(){
        // 1. 插入
        Share share = new Share() ;
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("xxx");
        share.setCover("xxx");
        share.setAuthor("yicj");
        share.setBuyCount(1);
        shareMapper.insertSelective(share) ;
        // 2. 做查询，查询当前数据库所有share
        List<Share> shares = this.shareMapper.selectAll();
        return shares ;
    }
}
