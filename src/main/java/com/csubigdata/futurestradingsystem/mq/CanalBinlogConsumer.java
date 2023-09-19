package com.csubigdata.futurestradingsystem.mq;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.csubigdata.futurestradingsystem.common.RedisCommonKey;
import com.csubigdata.futurestradingsystem.dto.CanalBinLogDTO;
import com.csubigdata.futurestradingsystem.patterns.AbstractStrategyChoose;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = RedisCommonKey.CANAL_TOPIC_KEY,
        consumerGroup = RedisCommonKey.CANAL_CG_KEY
)
public class CanalBinlogConsumer implements RocketMQListener<CanalBinLogDTO> {

    private final AbstractStrategyChoose abstractStrategyChoose;

    @Value("${ticket.availability.cache-update.type:}")
    private String ticketAvailabilityCacheUpdateType;

    @Override
    public void onMessage(CanalBinLogDTO message) {
        // 如果是 DDL 返回
        // 如果不是 UPDATE 类型数据变更返回
        // 如果没有开启 binlog 数据同步模型返回
        if (message.getIsDdl()
                || CollUtil.isEmpty(message.getOld())
                || !Objects.equals("UPDATE", message.getType())
                || !StrUtil.equals(message.getTable(), "models")
                || !StrUtil.equals(message.getTable(), "trading_history")){
            return;
        }
        // 通过策略模式进行不同 Binlog 变更类型的监听，比如说订单和座位两个表就分别有两个处理类
        abstractStrategyChoose.chooseAndExecute(
                message.getTable(),
                message
        );
    }
}