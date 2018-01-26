package com.jack.lottery.buss.plw;

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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Component
public class PLWFetchCurrent {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.plw.switch}")
    private boolean plwSwitch;

    @Value("${task.fetch.plw.urls}")
    private String plwUrls;

    @Autowired
    private LotteryService lotteryService;

    public boolean doPLW() {
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
}
