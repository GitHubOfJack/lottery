package com.jack.lottery.task;

import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.enums.WEBSITE;
import com.jack.lottery.service.LotteryService;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class CurrentTermFetchTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.ssq.switch}")
    private boolean ssqSwitch;

    @Value("${task.fetch.ssq.urls}")
    private String ssqUrls;

    @Value("${task.fetch.dlt.switch}")
    private boolean dltSwitch;

    @Value("${task.fetch.dlt.urls}")
    private String dltUrls;

    @Value("${task.fetch.pls.switch}")
    private boolean plsSwitch;

    @Value("${task.fetch.pls.urls}")
    private String plsUrls;

    @Value("${task.fetch.plw.switch}")
    private boolean plwSwitch;

    @Value("${task.fetch.plw.urls}")
    private String plwUrls;

    @Value("${task.fetch.sd.switch}")
    private boolean sdSwitch;

    @Value("${task.fetch.sd.urls}")
    private String sdUrls;

    @Autowired
    private LotteryService lotteryService;

    //每周二、四、日晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 0 0 1,3,5 *")
    public void ssq() {
        if (!ssqSwitch) {
            logger.info("双色球抓取当前期开关关闭!");
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
                success = doSSQ();
            } else {
                return;
            }
        }
    }

    private boolean doSSQ() {
        String[] urls = ssqUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            Document document = null;
            try {
                document = Jsoup.connect(realUrl).get();
            } catch (IOException e) {
                logger.info("抓取双色球数据出错,url:{},切换到下一个地址抓取", url);
                continue;
            }
            String lotteryNo = "";
            Date endTime = null;
            try {
                if (type.equals(WEBSITE.OK.getCode())) {
                    lotteryNo = document.getElementById("LotteryNo").attr("value");
                    //返回的格式是01-23 20:00
                    String endStr = "2018-"+document.getElementsByClass("numqihao").get(0).getElementsByTag("em").get(1).text();
                    endTime = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm");
                } else if (type.equals(WEBSITE.TAOBAO.getCode())) {
                    lotteryNo = document.getElementById("issue").attr("value");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    int week = calendar.get(Calendar.DAY_OF_WEEK);
                    if (week == 5) {
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+3);
                    } else {
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+2);
                    }
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                }
                lotteryService.insertTerm(LotteryType.SSQ, lotteryNo, endTime);
                return true;
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    //每周一、三、六晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 0 0 2,4,7 *")
    public void dlt() {
        if (!dltSwitch) {
            logger.info("大乐透抓取当前期开关关闭!");
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
                success = doDLT();
            } else {
                return;
            }
        }
    }

    private boolean doDLT() {
        String[] urls = dltUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            Document document = null;
            try {
                document = Jsoup.connect(realUrl).get();
            } catch (IOException e) {
                logger.info("抓取大乐透数据出错,url:{},切换到下一个地址抓取", url);
                continue;
            }
            String lotteryNo = "";
            Date endTime = null;
            try {
                if (type.equals(WEBSITE.OK.getCode())) {
                    lotteryNo = "20" + document.getElementById("LotteryNo").attr("value");
                    //返回的格式是01-23 20:00
                    String endStr = "2018-"+document.getElementsByClass("numqihao").get(0).getElementsByTag("em").get(1).text();
                    endTime = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm");
                } else if (type.equals(WEBSITE.TAOBAO.getCode())) {
                    lotteryNo = "20" +  document.getElementById("issue").attr("value");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    int week = calendar.get(Calendar.DAY_OF_WEEK);
                    if (week == 5) {
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+3);
                    } else {
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+2);
                    }
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                }
                lotteryService.insertTerm(LotteryType.DLT, lotteryNo, endTime);
                return true;
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    //每天晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 * * * *")
    public void pls() {
        if (!plsSwitch) {
            logger.info("排列三抓取当前期开关关闭!");
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
                success = doPLS();
            } else {
                return;
            }
        }
    }

    private boolean doPLS() {
        String[] urls = plsUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            Document document = null;
            try {
                document = Jsoup.connect(realUrl).get();
            } catch (IOException e) {
                logger.info("抓取排列三数据出错,url:{},切换到下一个地址抓取", url);
                continue;
            }
            String lotteryNo = "";
            Date endTime = null;
            try {
                if (type.equals(WEBSITE.OK.getCode())) {
                    lotteryNo = "20" + document.getElementById("LotteryNo").attr("value");
                    //返回的格式是01-23 20:00
                    String endStr = "2018-"+document.getElementsByClass("numqihao").get(0).getElementsByTag("em").get(1).text();
                    endTime = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm");
                } else if (type.equals(WEBSITE.TAOBAO.getCode())) {
                    lotteryNo = "20" +  document.getElementById("issue").attr("value");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1);
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                }
                lotteryService.insertTerm(LotteryType.PLS, lotteryNo, endTime);
                return true;
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    //每天晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 * * * *")
    public void plw() {
        if (!plwSwitch) {
            logger.info("排列五抓取当前期开关关闭!");
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
                success = doPLW();
            } else {
                return;
            }
        }
    }

    private boolean doPLW() {
        String[] urls = plwUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            Document document = null;
            try {
                document = Jsoup.connect(realUrl).get();
            } catch (IOException e) {
                logger.info("抓取排列五数据出错,url:{},切换到下一个地址抓取", url);
                continue;
            }
            String lotteryNo = "";
            Date endTime = null;
            try {
                if (type.equals(WEBSITE.OK.getCode())) {
                    lotteryNo = "20" + document.getElementById("LotteryNo").attr("value");
                    //返回的格式是01-23 20:00
                    String endStr = "2018-"+document.getElementsByClass("numqihao").get(0).getElementsByTag("em").get(1).text();
                    endTime = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm");
                } else if (type.equals(WEBSITE.TAOBAO.getCode())) {
                    lotteryNo = "20" +  document.getElementById("issue").attr("value");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1);
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                }
                lotteryService.insertTerm(LotteryType.PLW, lotteryNo, endTime);
                return true;
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    //每天晚上8点开始抓取新一期期号
    @Scheduled(cron = "0 0 20 * * * *")
    public void sd() {
        if (!sdSwitch) {
            logger.info("双色球抓取当前期开关关闭!");
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
                success = doSD();
            } else {
                return;
            }
        }
    }

    private boolean doSD() {
        String[] urls = sdUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            Document document = null;
            try {
                document = Jsoup.connect(realUrl).get();
            } catch (IOException e) {
                logger.info("抓取3D数据出错,url:{},切换到下一个地址抓取", url);
                continue;
            }
            String lotteryNo = "";
            Date endTime = null;
            try {
                if (type.equals(WEBSITE.OK.getCode())) {
                    lotteryNo = document.getElementById("LotteryNo").attr("value");
                    //返回的格式是01-23 20:00
                    String endStr = "2018-"+document.getElementsByClass("numqihao").get(0).getElementsByTag("em").get(1).text();
                    endTime = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm");
                } else if (type.equals(WEBSITE.TAOBAO.getCode())) {
                    lotteryNo = document.getElementById("issue").attr("value");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1);
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                }
                lotteryService.insertTerm(LotteryType.SD, lotteryNo, endTime);
                return true;
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String[] urls = "ok|http://www.okooo.com/shuangseqiu/;taobao|https://caipiao.taobao.com/lottery/order/lottery_ssq.htm;".split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            Document document = null;
            try {
                document = Jsoup.connect(realUrl).get();
            } catch (IOException e) {
                continue;
            }
            String lotteryNo = "";
            Date endTime = null;
            try {
                if (type.equals(WEBSITE.OK.getCode())) {
                    lotteryNo = document.getElementById("LotteryNo").attr("value");
                    //返回的格式是01-23 20:00
                    String endStr = "2018-"+document.getElementsByClass("numqihao").get(0).getElementsByTag("em") .get(1).text();
                    endTime = DateUtils.parseDate(endStr, "yyyy-MM-dd HH:mm");
                } else if (type.equals(WEBSITE.TAOBAO.getCode())) {
                    lotteryNo = document.getElementById("issue").attr("value");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    int week = calendar.get(Calendar.DAY_OF_WEEK);
                    if (week == 5) {
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+3);
                    } else {
                        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+2);
                    }
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                }
            } catch (Exception e) {
                continue;
            }
            System.out.println(realUrl);
            System.out.println(lotteryNo);
            System.out.println(endTime);
        }
    }
}
