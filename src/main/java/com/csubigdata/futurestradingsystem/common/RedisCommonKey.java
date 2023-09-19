package com.csubigdata.futurestradingsystem.common;

public class RedisCommonKey {
    // rocketmq topic
    public static final String CANAL_TOPIC_KEY = "future_trading_topic";
    //消费组 key
    public static final String CANAL_CG_KEY = "future_trading_cg";
    //model表缓存
    public static final String MODEL_KEY_PREFIX = "future_trading:models:";
    //trading_history表缓存
    public static final String TRADING_HISTORY_PREFIX = "future_trading:trading_history:";
}
