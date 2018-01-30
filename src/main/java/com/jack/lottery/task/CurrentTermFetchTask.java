package com.jack.lottery.task;

import com.jack.lottery.buss.dlt.DLTFetchCurrent;
import com.jack.lottery.buss.pls.PLSFetchCurrent;
import com.jack.lottery.buss.plw.PLWFetchCurrent;
import com.jack.lottery.buss.sd.SDFetchCurrent;
import com.jack.lottery.buss.ssq.SSQFetchCurrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class CurrentTermFetchTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.ssq.switch}")
    private boolean ssqSwitch;

    @Value("${task.fetch.dlt.switch}")
    private boolean dltSwitch;

    @Value("${task.fetch.pls.switch}")
    private boolean plsSwitch;

    @Value("${task.fetch.plw.switch}")
    private boolean plwSwitch;

    @Value("${task.fetch.sd.switch}")
    private boolean sdSwitch;

    @Autowired
    private SSQFetchCurrent ssqFetchCurrent;

    @Autowired
    private DLTFetchCurrent dltFetchCurrent;

    @Autowired
    private PLSFetchCurrent plsFetchCurrent;

    @Autowired
    private PLWFetchCurrent plwFetchCurrent;

    @Autowired
    private SDFetchCurrent sdFetchCurrent;

    //每周二、四、日晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 ? * 1,3,5")
    public void ssq() {
        if (!ssqSwitch) {
            logger.info("双色球抓取当前期开关关闭!");
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
                    logger.error("抓取双色球结果线程异常", e);
                    return;
                }
                success = ssqFetchCurrent.doSSQ();
            } else {
                return;
            }
        }
    }

    //每周一、三、六晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 ? * 2,4,7")
    public void dlt() {
        if (!dltSwitch) {
            logger.info("大乐透抓取当前期开关关闭!");
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
                    logger.error("抓取大乐透结果线程异常", e);
                    return;
                }
                success = dltFetchCurrent.doDLT();
            } else {
                return;
            }
        }
    }

    //每天晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 * * *")
    public void pls() {
        if (!plsSwitch) {
            logger.info("排列三抓取当前期开关关闭!");
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
                    logger.error("抓取排列三结果线程异常", e);
                    return;
                }
                success = plsFetchCurrent.doPLS();
            } else {
                return;
            }
        }
    }

    //每天晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 * * *")
    public void plw() {
        if (!plwSwitch) {
            logger.info("排列五抓取当前期开关关闭!");
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
                    logger.error("抓取排列五结果线程异常", e);
                    return;
                }
                success = plwFetchCurrent.doPLW();
            } else {
                return;
            }
        }
    }

    //每天晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 * * *")
    public void sd() {
        if (!sdSwitch) {
            logger.info("3D抓取当前期开关关闭!");
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
                    logger.error("抓取3D结果线程异常", e);
                    return;
                }
                success = sdFetchCurrent.doSD();
            } else {
                return;
            }
        }
    }
}
