package com.jack.lottery.buss.plw;

import com.alibaba.fastjson.JSONObject;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.enums.WEBSITE;
import com.jack.lottery.service.LotteryService;
import com.jack.lottery.utils.URLConnectionUtil;
import com.jack.lottery.vo.LotteryHistory;
import com.jack.lottery.vo.PrizeDetail;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PLWFetchHistory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.history.plw.urls}")
    private String plwUrls;

    @Autowired
    private LotteryService lotteryService;

    public boolean fetchPLWHistory(String termNo) {
        LotteryHistory plwHistory = getPLWHistory(termNo);
        String result = plwHistory.getResult();
        if (StringUtils.isBlank(result)) {
            return false;
        }
        List<PrizeDetail> details = plwHistory.getDetails();
        String prizeDetail = "";
        if (null != details) {
            prizeDetail = JSONObject.toJSONString(details).replace("\"addNum\":0,", "");
        }
        lotteryService.updateTermByTermNo(LotteryType.PLW, termNo, result, prizeDetail);
        return true;
    }

    public LotteryHistory getPLWHistory(String termNo) {
        LotteryHistory history = new LotteryHistory();
        history.setType(String.valueOf(LotteryType.PLW.getCode()));
        history.setTermNo(termNo);
        String[] urls = plwUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            if (type.equals(WEBSITE.OK.getCode())) {
                String resp = URLConnectionUtil.doGet(realUrl, "?LotteryNo=" + termNo.substring(2) + "&Type=lotteryresult");
                if (StringUtils.isBlank(resp)) {
                    logger.info("从澳客抓取排列五历史数据出错");
                    continue;
                }
                JSONObject respJson = JSONObject.parseObject(resp);
                String result = respJson.getString("LotteryResult").replace(";", ",");
                history.setResult(result);
                history.setOpenDate(respJson.getDate("date"));
                history.setDetails(createPLWPrizeDetailFromOK(respJson.getJSONObject("PrizeList")));
                return history;
            } else if (type.equals(WEBSITE.FIVEBILLION.getCode())){
                realUrl = realUrl.replace("{}", termNo.substring(2));
                Document document = null;
                try {
                    document = Jsoup.connect(realUrl).get();
                } catch (IOException e) {
                    logger.info("从500W抓取排列五历史数据出错");
                    continue;
                }
                history.setResult(getPLWResultFrom500W(document));
                history.setOpenDate(getPLWDateFrom500W(document));
                history.setDetails(getPLWPrizeDetailFrom500W(document));
            }
        }
        return history;
    }

    //{"LotteryResult":"5;0;6;8;8","PrizeList":{"1":{"LotteryType":"P5","LotteryNo":"18024","PrizeLevel":"1","Prize":"100000","HitNum":"42"}},"Sale":"10651140","PoolPrize":"-1","riqi":["01","24","\u661f\u671f\u4e09"],"date":"2018-01-24"}
    private List<PrizeDetail> createPLWPrizeDetailFromOK(JSONObject json) {
        List<PrizeDetail> detailList = new ArrayList<>();
        PrizeDetail prize = new PrizeDetail();
        JSONObject jsonObject = json.getJSONObject("1");
        prize.setAmount(jsonObject.getBigDecimal("Prize"));
        prize.setNum(jsonObject.getIntValue("HitNum"));
        prize.setName(String.valueOf("1"));
        detailList.add(prize);
        return detailList;
    }

    private String getPLWResultFrom500W(Document document) {
        Elements iSelectList1 = document.getElementsByClass("ball_box01");
        Elements lis = iSelectList1.select("li");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Element li : lis) {
            sb.append(li.text());
            if (i == 4) {

            } else {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }

    private List<PrizeDetail> getPLWPrizeDetailFrom500W(Document document) {
        List<PrizeDetail> detailList = new ArrayList<>();
        Elements trs = document.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");

        PrizeDetail prize1 = new PrizeDetail();
        Elements tds1 = trs.get(2).getElementsByTag("td");
        prize1.setName(String.valueOf(1));
        if (tds1.get(1).text().contains("--") || tds1.get(2).text().contains("--")) {
            prize1.setAmount(BigDecimal.ZERO);
            prize1.setNum(0);
        } else {
            prize1.setAmount(BigDecimal.valueOf(Double.parseDouble(tds1.get(2).text().replace(",", ""))));
            prize1.setNum(Integer.parseInt(tds1.get(1).text()));
        }
        detailList.add(prize1);

        return detailList;
    }

    private Date getPLWDateFrom500W(Document document) {
        Elements span_right = document.getElementsByClass("span_right");
        String endTime = span_right.text();
        String date = endTime;
        if (endTime.contains("年")) {
            String year = endTime.split("兑奖截止日期")[0].split("年")[0].split("：")[1].trim();
            String month = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[0].trim();
            String day = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[1].split("日")[0].trim();
            date = year + "-"+ month + "-"+day;
        } else {
            date = date.split("日期：")[1].split("兑奖")[0].trim();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(date);
            return d;
        } catch (ParseException e) {
            logger.error("日期格式转换失败");
            return null;
        }
    }
}
