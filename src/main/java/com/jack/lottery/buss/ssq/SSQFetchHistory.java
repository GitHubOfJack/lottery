package com.jack.lottery.buss.ssq;

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
public class SSQFetchHistory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.history.ssq.urls}")
    private String ssqUrls;

    @Autowired
    private LotteryService lotteryService;

    //获取上期开奖结果
    public boolean fetchSSQHistory(String termNo) {
        LotteryHistory ssqHistory = getSSQHistory(termNo);
        String result = ssqHistory.getResult();
        if (StringUtils.isBlank(result)) {
            return false;
        }
        List<PrizeDetail> details = ssqHistory.getDetails();
        String prizeDetail = "";
        if (null != details) {
            prizeDetail = JSONObject.toJSONString(details).replace("\"addNum\":0,", "");
        }
        lotteryService.updateTermByTermNo(LotteryType.SSQ, termNo, result, prizeDetail);
        return true;
    }

    //澳客网返回数据   {"LotteryResult":"5,10,17,23,26,32|7","PrizeList":{"1":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"1","Prize":"5477449","HitNum":"17"},"2":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"2","Prize":"45908","HitNum":"221"},"3":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"3","Prize":"3000","HitNum":"2633"},"4":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"4","Prize":"200","HitNum":"113710"},"5":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"5","Prize":"10","HitNum":"1793617"},"6":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"6","Prize":"5","HitNum":"19825084"}},"Sale":"384256808","SortNum":["10","17","26","23","05","32","07"],"PoolPrize":"364964347","riqi":["01","21","\u661f\u671f\u65e5"],"date":"2018-01-21"}
    public LotteryHistory getSSQHistory(String termNo) {
        LotteryHistory history = new LotteryHistory();
        history.setType(String.valueOf(LotteryType.SSQ.getCode()));
        history.setTermNo(termNo);
        String[] urls = ssqUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            if (type.equals(WEBSITE.OK.getCode())) {
                String resp = URLConnectionUtil.doGet(realUrl, "?LotteryNo=" + termNo + "&Type=lotteryresult");
                if (StringUtils.isBlank(resp)) {
                    logger.info("从澳客抓取双色球历史数据出错");
                    continue;
                }
                JSONObject respJson = JSONObject.parseObject(resp);
                String result = respJson.getString("LotteryResult");
                history.setResult(result);
                history.setOpenDate(respJson.getDate("date"));
                history.setDetails(createSSQPrizeDetailFromOK(respJson.getJSONObject("PrizeList")));
                return history;
            } else if (type.equals(WEBSITE.FIVEBILLION.getCode())){
                realUrl = realUrl.replace("{}", termNo.substring(2));
                Document document = null;
                try {
                    document = Jsoup.connect(realUrl).get();
                } catch (IOException e) {
                    logger.info("从500W抓取双色球历史数据出错");
                    continue;
                }
                history.setResult(getSSQResultFrom500W(document));
                history.setOpenDate(getSSQDateFrom500W(document));
                history.setDetails(getSSQPrizeDetailFrom500W(document));
            }
        }
        return history;
    }

    //{"1":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"1","Prize":"5477449","HitNum":"17"},"2":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"2","Prize":"45908","HitNum":"221"},"3":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"3","Prize":"3000","HitNum":"2633"},"4":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"4","Prize":"200","HitNum":"113710"},"5":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"5","Prize":"10","HitNum":"1793617"},"6":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"6","Prize":"5","HitNum":"19825084"}}
    private List<PrizeDetail> createSSQPrizeDetailFromOK(JSONObject json) {
        List<PrizeDetail> detailList = new ArrayList<>();
        for (int i=1; i<7; i++) {
            PrizeDetail prize = new PrizeDetail();
            JSONObject jsonObject = json.getJSONObject(String.valueOf(i));
            prize.setAmount(jsonObject.getBigDecimal("Prize"));
            prize.setName(String.valueOf(i));
            prize.setNum(jsonObject.getIntValue("HitNum"));
            detailList.add(prize);
        }
        return detailList;
    }

    private String getSSQResultFrom500W(Document document) {
        Elements iSelectList1 = document.getElementsByClass("ball_box01");
        Elements lis = iSelectList1.select("li");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Element li : lis) {
            sb.append(li.text());
            if (i == 5) {
                sb.append("|");
            } else if (i == 6) {

            } else {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }

    private List<PrizeDetail> getSSQPrizeDetailFrom500W(Document document) {
        List<PrizeDetail> detailList = new ArrayList<>();
        Elements trs = document.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");
        for (int j=2; j<8; j++) {
            PrizeDetail prize = new PrizeDetail();
            Elements tds = trs.get(j).getElementsByTag("td");
            if (tds.get(2).text().equals("--") || tds.get(1).text().equals("--")) {
                prize.setAmount(BigDecimal.valueOf(0));
                prize.setNum(0);
            } else {
                prize.setAmount(BigDecimal.valueOf(Double.parseDouble(tds.get(2).text().replace(",", ""))));
                prize.setNum(Integer.parseInt(tds.get(1).text()));
            }
            prize.setName(String.valueOf(j-1));
            detailList.add(prize);
        }
        return detailList;
    }

    private Date getSSQDateFrom500W(Document document) {
        Elements span_right = document.getElementsByClass("span_right");
        String endTime = span_right.text();
        String year = endTime.split("兑奖截止日期")[0].split("年")[0].split("：")[1].trim();
        String month = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[0].trim();
        String day = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[1].split("日")[0].trim();
        String date = year + "-"+ month + "-"+day;
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
