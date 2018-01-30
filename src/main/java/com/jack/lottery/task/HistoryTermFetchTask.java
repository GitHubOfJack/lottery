package com.jack.lottery.task;

import com.jack.lottery.buss.dlt.DLTFetchHistory;
import com.jack.lottery.buss.pls.PLSFetchHistory;
import com.jack.lottery.buss.plw.PLWFetchHistory;
import com.jack.lottery.buss.sd.SDFetchHistory;
import com.jack.lottery.buss.ssq.SSQFetchHistory;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.service.LotteryService;
import com.jack.lottery.vo.GetHistoryTermResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
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
    private SSQFetchHistory ssqFetchHistory;

    @Autowired
    private DLTFetchHistory dltFetchHistory;

    @Autowired
    private PLSFetchHistory plsFetchHistory;

    @Autowired
    private PLWFetchHistory plwFetchHistory;

    @Autowired
    private SDFetchHistory sdFetchHistory;

    //每周二、四、日晚上9点35开奖
    @Scheduled(cron = "0 40 21 ? * 1,3,5")
    public void fetchSSQ() {
        if (!ssqSwitch) {
            logger.info("双色球抓取上期开奖结果开关关闭!");
            return;
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
                success = ssqFetchHistory.fetchSSQHistory(termNo);
            } else {
                return;
            }
        }
    }

    //每周一、三、六晚上8点40开奖
    @Scheduled(cron = "0 45 20 ? * 2,4,7")
    public void fetchDLT() {
        if (!dltSwitch) {
            logger.info("大乐透抓取上期开奖结果开关关闭!");
            return;
        }
        String termNo = null;
        try {
            GetHistoryTermResp historyTerm = lotteryService.getHistoryTerm(LotteryType.DLT.getCode(), 1, 1);
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
                    logger.error("抓取大乐透上期开奖结果线程异常", e);
                    return;
                }
                success = dltFetchHistory.fetchDLTHistory(termNo);
            } else {
                return;
            }
        }
    }

    //每天晚上8点35开奖
    @Scheduled(cron = "0 40 20 * * *")
    public void fetchPLS() {
        if (!plsSwitch) {
            logger.info("排列三抓取上期开奖结果开关关闭!");
            return;
        }
        String termNo = null;
        try {
            GetHistoryTermResp historyTerm = lotteryService.getHistoryTerm(LotteryType.PLS.getCode(), 1, 1);
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
                    logger.error("抓取排列三上期开奖结果线程异常", e);
                    return;
                }
                success = plsFetchHistory.fetchPLSHistory(termNo);
            } else {
                return;
            }
        }
    }

    //每天晚上8点50开奖
    @Scheduled(cron = "0 55 20 * * *")
    public void fetchPLW() {
        if (!plwSwitch) {
            logger.info("排列五抓取上期开奖结果开关关闭!");
            return;
        }
        String termNo = null;
        try {
            GetHistoryTermResp historyTerm = lotteryService.getHistoryTerm(LotteryType.PLW.getCode(), 1, 1);
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
                    logger.error("抓取排列五上期开奖结果线程异常", e);
                    return;
                }
                success = plwFetchHistory.fetchPLWHistory(termNo);
            } else {
                return;
            }
        }
    }

    //每天晚上9点15开奖
    @Scheduled(cron = "0 20 21 * * *")
    public void fetchSD() {
        if (!sdSwitch) {
            logger.info("福彩3D抓取上期开奖结果开关关闭!");
            return;
        }
        String termNo = null;
        try {
            GetHistoryTermResp historyTerm = lotteryService.getHistoryTerm(LotteryType.SD.getCode(), 1, 1);
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
                    logger.error("抓取福彩3D上期开奖结果线程异常", e);
                    return;
                }
                success = sdFetchHistory.fetchSDHistory(termNo);
            } else {
                return;
            }
        }
    }
}
