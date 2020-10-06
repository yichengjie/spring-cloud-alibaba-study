package com.yicj.contentcenter.service.content;

import com.yicj.contentcenter.dao.content.ShareMapper;
import com.yicj.contentcenter.dao.messaging.RocketmqTransactionLogMapper;
import com.yicj.contentcenter.domain.dto.content.ShareAuditDTO;
import com.yicj.contentcenter.domain.dto.content.ShareDTO;
import com.yicj.contentcenter.domain.dto.enums.AuditStatusEnum;
import com.yicj.contentcenter.domain.dto.message.UserAddBonusMsgDTO;
import com.yicj.contentcenter.domain.dto.user.UserDTO;
import com.yicj.contentcenter.domain.entity.content.Share;
import com.yicj.contentcenter.domain.entity.messaging.RocketmqTransactionLog;
import com.yicj.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {
    private final ShareMapper shareMapper ;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper ;
    private final  UserCenterFeignClient userCenterFeignClient ;
    private final RocketMQTemplate rocketMQTemplate ;

    public ShareDTO findById(Integer id){
        // 1. 查询share
        Share share = shareMapper.selectByPrimaryKey(id);
        // 发布人id
        Integer userId = share.getUserId();
        //2. 调用用户微服务的/users/{userId}
        // 用户中心的所有实例信息
        //http://localhost:8080/users/{id}
        UserDTO userDTO = userCenterFeignClient.findById(id) ;
        //3. 组装信息返回
        ShareDTO shareDTO = new ShareDTO() ;
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO ;
    }

    public Share auditById(Integer id, ShareAuditDTO auditDTO) {
        // 1. 查询share是否存在，不存在或者当前的audit_status != NOT_YET，那么抛异常
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法！该分享不存在！");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法！该分享已审核通过或审核不通过！");
        }
        auditByIdInDB(id, auditDTO);
        //3. 如果是pass，那么发送消息给rocketmq，让用户中心消费，并为发布人添加积分
        if (AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())){
            // 发送半消息
            UserAddBonusMsgDTO bonusMsgDTO =UserAddBonusMsgDTO.builder().userId(share.getUserId()).bonus(50).build() ;
            String destination = "add-bonus" ;
            String txProducerGroup = "tx-add-bonus-group" ;
            String transactionId = UUID.randomUUID().toString();
            Message<UserAddBonusMsgDTO> message =
                    MessageBuilder.withPayload(bonusMsgDTO)
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                            .setHeader("share_id", id)
                            .build();
            // arg有大用处
            rocketMQTemplate.sendMessageInTransaction(txProducerGroup , destination, message,auditDTO) ;
        }
        return share;
    }

    private void auditByIdInDB(Integer id, ShareAuditDTO auditDTO) {
        //2. 审核资源，将状态设为pass/reject
        Share share = Share.builder()
                .id(id)
                .auditStatus(auditDTO.getAuditStatusEnum().toString())
                .reason(auditDTO.getReason())
                .build();
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        this.shareMapper.updateByPrimaryKeySelective(share);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithRocketMqLog(Integer id, ShareAuditDTO auditDTO, String transactionId) {
        this.auditByIdInDB(id, auditDTO);
        this.rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("审核分享...")
                        .build()
        );
    }

}
