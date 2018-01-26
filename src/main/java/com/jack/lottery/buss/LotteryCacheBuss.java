package com.jack.lottery.buss;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.po.LotteryHistory;
import com.jack.lottery.service.LotteryService;
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
    private LotteryService lotteryService;

    private LoadingCache<String, LotteryHistory> lotteryHistoryCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(1, TimeUnit.DAYS)
            .expireAfterAccess(1, TimeUnit.DAYS)
            .maximumSize(1000)
            .build(new CacheLoader<String, LotteryHistory>() {
                @Override
                public LotteryHistory load(String s) throws Exception {
                    String[] strings = deGenerateCacheKey(s);
                    return lotteryService.getLotteryHistory(LotteryType.getTypeByCode(strings[0]), strings[1]);
                }
            });

    private LoadingCache<LotteryType, LotteryTerm> latestLotteryCache = CacheBuilder.newBuilder()
            .refreshAfterWrite(15, TimeUnit.MINUTES)
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .maximumSize(10)
            .build(new CacheLoader<LotteryType, LotteryTerm>() {
                       @Override
                       public LotteryTerm load(LotteryType type) throws Exception {
                           return lotteryService.getHistoryTerm(type.getCode(), 1, 1).getTerms().get(0);
                       }
                   }
            );

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

    public LotteryTerm getLatestTerm(LotteryType type) {
        LotteryTerm term = null;
        try {
            term = latestLotteryCache.get(type);
        } catch (ExecutionException e) {
            logger.error("获取最近期的数据报错,{}", type, e);
        }
        return term;
    }
}
