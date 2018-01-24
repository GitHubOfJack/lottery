package com.jack.lottery.task;

import com.jack.lottery.buss.LotteryBuss;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.service.LotteryService;
import com.jack.lottery.vo.GetHistoryTermResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class HistoryTermFetchTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.history.ssq.switch}")
    private boolean ssqSwitch;
    @Value("${task.fetch.history.ssq.urls}")
    private String ssqUrls;

    @Value("${task.fetch.history.dlt.switch}")
    private boolean dltSwitch;
    @Value("${task.fetch.history.dlt.urls}")
    private String dltUrls;

    @Value("${task.fetch.history.pls.switch}")
    private boolean plsSwitch;
    @Value("${task.fetch.history.pls.urls}")
    private String plsUrls;

    @Value("${task.fetch.history.plw.switch}")
    private boolean plwSwitch;
    @Value("${task.fetch.history.plw.urls}")
    private String plwUrls;

    @Value("${task.fetch.history.sd.switch}")
    private boolean sdSwitch;
    @Value("${task.fetch.history.sd.urls}")
    private String sdUrls;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryBuss lotteryBuss;

    //每周二、四、日晚上9点35开奖
    @Scheduled(cron = "0 40 21 0 0 1,3,5 *")
    public void fetchSSQ() {
        if (!ssqSwitch) {
            logger.info("双色球抓取上期开奖结果开关关闭!");
        }
        String termNo = null;
        try {
            GetHistoryTermResp historyTerm = lotteryService.getHistoryTerm(LotteryType.SSQ.getCode(), 1, 1);
            LotteryTerm lotteryTerm = historyTerm.getTerms().get(0);
            termNo = lotteryTerm.getTerm();
        } catch (Exception e) {
            logger.error("获取历史期数异常", e);
            return;
        }
        boolean success = false;
        int num = 0;
        while (!success) {
            num++;
            if (6 > num) {
                try {
                    TimeUnit.MINUTES.sleep(num-1);
                } catch (InterruptedException e) {
                    logger.error("抓取双色球上期开奖结果线程异常", e);
                    return;
                }
                success = lotteryBuss.fetchSSQHistory(termNo);
            } else {
                return;
            }
        }
    }

    //每周一、三、六晚上8点40开奖
    @Scheduled(cron = "0 45 20 0 0 2,4,7 *")
    public void fetchDLT() {

    }

    //每天晚上8点35开奖
    @Scheduled(cron = "0 40 20 * * * *")
    public void fetchPLS() {

    }

    //每天晚上8点50开奖
    @Scheduled(cron = "0 55 20 * * * *")
    public void fetchPLW() {

    }

    //每天晚上9点15开奖
    @Scheduled(cron = "0 20 21 * * * *")
    public void fetchSD() {

    }
}
