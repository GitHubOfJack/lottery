package com.jack.lottery.buss;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.po.LotteryHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class LotteryCacheBuss {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LotteryBuss lotteryBuss;

    private LoadingCache<String, LotteryHistory> lotteryHistoryCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(5, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build(new CacheLoader<String, LotteryHistory>() {
                @Override
                public LotteryHistory load(String s) throws Exception {
                    String[] strings = deGenerateCacheKey(s);
                    return lotteryBuss.getHistory(LotteryType.getTypeByCode(strings[0]), strings[1]);
                }
            });

    private String generateCacheKey(LotteryType type, String termNo) {
        return type.getCode()+"|"+termNo;
    }

    private String[] deGenerateCacheKey(String key) {
        return key.split("\\|");
    }

    public LotteryHistory getCache(LotteryType type, String termNo) {
        LotteryHistory lotteryHistory = null;
        String key = generateCacheKey(type, termNo);
        try {
            lotteryHistory = lotteryHistoryCache.get(key);
        } catch (ExecutionException e) {
            logger.error("获取历史记录报错,{}, {}", type, termNo, e);
        }
        return lotteryHistory;
    }
}
