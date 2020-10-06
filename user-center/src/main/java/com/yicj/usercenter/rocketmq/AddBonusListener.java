package com.yicj.usercenter.rocketmq;

import com.yicj.usercenter.dao.bonus.BonusEventLogMapper;
import com.yicj.usercenter.dao.user.UserMapper;
import com.yicj.usercenter.domain.dto.message.UserAddBonusMsgDTO;
import com.yicj.usercenter.domain.entity.bonus.BonusEventLog;
import com.yicj.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "consumer-group",topic = "add-bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusListener implements RocketMQListener<UserAddBonusMsgDTO> {
    private final UserMapper userMapper ;
    private final BonusEventLogMapper bonusEventLogMapper ;

    @Override
    public void onMessage(UserAddBonusMsgDTO message) {
        //当收到消息时执行的业务
        //1. 为用户添加积分
        Integer userId = message.getUserId();
        Integer bonus = message.getBonus();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateByPrimaryKey(user) ;
        //2. 记录日志到bonus_event_log表
        BonusEventLog bonusEventLog = BonusEventLog.builder()
                .userId(userId)
                .value(bonus)
                .event("CONTRIBUTE")
                .createTime(new Date())
                .description("投稿加积分")
                .build();
        bonusEventLogMapper.insert(bonusEventLog) ;
        log.info("积分添加完毕....");
    }
}
