package com.csubigdata.futurestradingsystem.patterns.handler;

import cn.hutool.core.collection.CollUtil;
import com.csubigdata.futurestradingsystem.dto.CanalBinLogDTO;
import com.csubigdata.futurestradingsystem.mq.CanalStrategyEnum;
import com.csubigdata.futurestradingsystem.patterns.AbstractExecuteStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.csubigdata.futurestradingsystem.common.RedisCommonKey.TRADING_HISTORY_PREFIX;


/**
 * 更新trading_history表缓存
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TradingHistoryCacheUpdateHandler implements AbstractExecuteStrategy<CanalBinLogDTO, Void> {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void execute(CanalBinLogDTO message) {
        if (CollUtil.isEmpty(message.getData())) {
            return;
        }
        for (Map<String, Object> each : message.getData()) {
            stringRedisTemplate.opsForHash().put(TRADING_HISTORY_PREFIX + each.get("uid"), each.get("record_id"), each);
        }
    }

    @Override
    public String mark() {
        return CanalStrategyEnum.T_TRADING.getTabName();
    }

}
