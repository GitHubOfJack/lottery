package com.jack.lottery.buss.dlt;

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
public class DLTFetchHistory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.history.dlt.urls}")
    private String dltUrls;

    @Autowired
    private LotteryService lotteryService;

    public boolean fetchDLTHistory(String termNo) {
        String result = getDLTHistory(termNo).getResult();
        if (StringUtils.isBlank(result)) {
            return false;
        }
        lotteryService.updateTermByTermNo(LotteryType.DLT, termNo, result);
        return true;
    }

    public LotteryHistory getDLTHistory(String termNo) {
        LotteryHistory history = new LotteryHistory();
        history.setType(String.valueOf(LotteryType.DLT.getCode()));
        history.setTermNo(termNo);
        String[] urls = dltUrls.split(";");
        for (String url : urls) {
            String[] split = url.split("\\|");
            String type = split[0];//网站标识
            String realUrl = split[1];//网站地址
            if (type.equals(WEBSITE.OK.getCode())) {
                String resp = URLConnectionUtil.doGet(realUrl, "?LotteryNo=" + termNo.substring(2) + "&Type=lotteryresult");
                if (StringUtils.isBlank(resp)) {
                    logger.info("从澳客抓取大乐透历史数据出错");
                    continue;
                }
                JSONObject respJson = JSONObject.parseObject(resp);
                String result = respJson.getString("LotteryResult");
                history.setResult(result);
                history.setOpenDate(respJson.getDate("date"));
                history.setDetails(createDLTPrizeDetailFromOK(respJson.getJSONObject("PrizeList")));
                return history;
            } else if (type.equals(WEBSITE.FIVEBILLION.getCode())){
                realUrl = realUrl.replace("{}", termNo.substring(2));
                Document document = null;
                try {
                    document = Jsoup.connect(realUrl).get();
                } catch (IOException e) {
                    logger.info("从500W抓取大乐透历史数据出错");
                    continue;
                }
                history.setResult(getDLTResultFrom500W(document));
                history.setOpenDate(getDLTDateFrom500W(document));
                history.setDetails(getDLTPrizeDetailFrom500W(document));
            }
        }
        return history;
    }

    //{"LotteryResult":"24,25,26,28,29|04,12","PrizeList":{"1":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"1","Prize":"10000000","HitNum":"2"},"2":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"2","Prize":"136700","HitNum":"74"},"3":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"3","Prize":"7654","HitNum":"521"},"4":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"4","Prize":"200","HitNum":"23242"},"5":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"5","Prize":"10","HitNum":"470156"},"6":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"6","Prize":"5","HitNum":"5027120"},"7":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"7","Prize":"6000000","HitNum":"2"},"8":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"8","Prize":"82020","HitNum":"23"},"9":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"9","Prize":"4592","HitNum":"148"},"10":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"10","Prize":"100","HitNum":"7152"},"11":{"LotteryType":"SuperLotto","LotteryNo":"18010","PrizeLevel":"11","Prize":"5","HitNum":"150551"}},"Sale":"209455315","SortNum":["25","28","24","29","26","12","04"],"PoolPrize":"4588018755","riqi":["01","22","\u661f\u671f\u4e00"],"date":"2018-01-22"}
    private List<PrizeDetail> createDLTPrizeDetailFromOK(JSONObject json) {
        List<PrizeDetail> detailList = new ArrayList<>();
        for (int i=1; i<11; i++) {
            PrizeDetail prize = new PrizeDetail();
            JSONObject jsonObject = json.getJSONObject(String.valueOf(i));
            prize.setAmount(jsonObject.getBigDecimal("Prize"));
            prize.setName(String.valueOf(i));
            prize.setNum(jsonObject.getIntValue("HitNum"));
            detailList.add(prize);
        }
        return detailList;
    }

    private String getDLTResultFrom500W(Document document) {
        Elements iSelectList1 = document.getElementsByClass("ball_box01");
        Elements lis = iSelectList1.select("li");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Element li : lis) {
            sb.append(li.text());
            if (i == 4) {
                sb.append("|");
            } else if (i == 6) {

            } else {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }

    private List<PrizeDetail> getDLTPrizeDetailFrom500W(Document document) {
        List<PrizeDetail> detailList = new ArrayList<>();
        Elements trs = document.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");

        int j = 0;
        for (Element tr : trs) {
            if (tr.text().contains("一等奖") && !tr.text().contains("baoshi")) {
                PrizeDetail prize1 = new PrizeDetail();
                Elements tds1 = tr.getElementsByTag("td");
                prize1.setName("1");
                if (tds1.get(2).text().equals("--") || tds1.get(3).text().equals("--")) {
                    prize1.setAmount(BigDecimal.valueOf(0));
                    prize1.setNum(0);
                } else {
                    prize1.setAmount(BigDecimal.valueOf(Double.parseDouble(tds1.get(3).text().replace(",", ""))));
                    prize1.setNum(Integer.parseInt(tds1.get(2).text()));
                }
                detailList.add(prize1);
            } else if (tr.text().contains("二等奖") && !tr.text().contains("baoshi")) {
                PrizeDetail prize2 = new PrizeDetail();
                Elements tds2 = tr.getElementsByTag("td");
                prize2.setName("2");
                if (tds2.get(2).text().equals("--") || tds2.get(3).text().equals("--")) {
                    prize2.setAmount(BigDecimal.valueOf(0));
                    prize2.setNum(0);
                } else {
                    prize2.setAmount(BigDecimal.valueOf(Double.parseDouble(tds2.get(3).text().replace(",", ""))));
                    prize2.setNum(Integer.parseInt(tds2.get(2).text()));
                }
                detailList.add(prize2);
            } else if (tr.text().contains("三等奖") && !tr.text().contains("baoshi")) {
                PrizeDetail prize3 = new PrizeDetail();
                Elements tds3 = tr.getElementsByTag("td");
                prize3.setName("3");
                if (tds3.get(2).text().equals("--") || tds3.get(3).text().equals("--")) {
                    prize3.setAmount(BigDecimal.valueOf(0));
                    prize3.setNum(0);
                } else {
                    prize3.setAmount(BigDecimal.valueOf(Double.parseDouble(tds3.get(3).text().replace(",", ""))));
                    prize3.setNum(Integer.parseInt(tds3.get(2).text()));
                }
                detailList.add(prize3);
            } else if (tr.text().contains("四等奖") && !tr.text().contains("baoshi")) {
                PrizeDetail prize4 = new PrizeDetail();
                Elements tds4 = tr.getElementsByTag("td");
                prize4.setName("4");
                if (tds4.get(2).text().equals("--") || tds4.get(3).text().equals("--")) {
                    prize4.setAmount(BigDecimal.valueOf(0));
                    prize4.setNum(0);
                } else {
                    prize4.setAmount(BigDecimal.valueOf(Double.parseDouble(tds4.get(3).text().replace(",", ""))));
                    prize4.setNum(Integer.parseInt(tds4.get(2).text()));
                }
                detailList.add(prize4);
            } else if (tr.text().contains("五等奖") && !tr.text().contains("baoshi")) {
                PrizeDetail prize5 = new PrizeDetail();
                Elements tds5 = tr.getElementsByTag("td");
                prize5.setName("5");
                if (tds5.get(2).text().equals("--") || tds5.get(3).text().equals("--")) {
                    prize5.setAmount(BigDecimal.valueOf(0));
                    prize5.setNum(0);
                } else {
                    prize5.setAmount(BigDecimal.valueOf(Double.parseDouble(tds5.get(3).text().replace(",", ""))));
                    prize5.setNum(Integer.parseInt(tds5.get(2).text()));
                }
                detailList.add(prize5);
            } else if (tr.text().contains("六等奖") && !tr.text().contains("baoshi")) {
                PrizeDetail prize6 = new PrizeDetail();
                Elements tds6 = tr.getElementsByTag("td");
                prize6.setName("6");
                if (tds6.get(2).text().equals("--") || tds6.get(3).text().equals("--")) {
                    prize6.setAmount(BigDecimal.valueOf(0));
                    prize6.setNum(0);
                } else {
                    prize6.setAmount(BigDecimal.valueOf(Double.parseDouble(tds6.get(3).text().replace(",", ""))));
                    prize6.setNum(Integer.parseInt(tds6.get(2).text()));
                }
                detailList.add(prize6);
            } else if (tr.text().contains("追加") && j == 0) {
                PrizeDetail prize7 = new PrizeDetail();
                Elements tds7 = tr.getElementsByTag("td");
                prize7.setName("7");
                if (tds7.get(2).text().equals("--") || tds7.get(1).text().equals("--")) {
                    prize7.setAmount(BigDecimal.valueOf(0));
                    prize7.setNum(0);
                } else {
                    prize7.setAmount(BigDecimal.valueOf(Double.parseDouble(tds7.get(2).text().replace(",", ""))));
                    prize7.setNum(Integer.parseInt(tds7.get(1).text()));
                }
                detailList.add(prize7);
                j++;
            } else if (tr.text().contains("追加") && j == 1) {
                PrizeDetail prize8 = new PrizeDetail();
                Elements tds8 = tr.getElementsByTag("td");
                prize8.setName("8");
                if (tds8.get(2).text().equals("--") || tds8.get(1).text().equals("--")) {
                    prize8.setAmount(BigDecimal.valueOf(0));
                    prize8.setNum(0);
                } else {
                    prize8.setAmount(BigDecimal.valueOf(Double.parseDouble(tds8.get(2).text().replace(",", ""))));
                    prize8.setNum(Integer.parseInt(tds8.get(1).text()));
                }
                detailList.add(prize8);
                j++;
            } else if (tr.text().contains("追加") && j == 2) {
                PrizeDetail prize9 = new PrizeDetail();
                Elements tds9 = tr.getElementsByTag("td");
                prize9.setName("9");
                if (tds9.get(2).text().equals("--") || tds9.get(1).text().equals("--")) {
                    prize9.setAmount(BigDecimal.valueOf(0));
                    prize9.setNum(0);
                } else {
                    prize9.setAmount(BigDecimal.valueOf(Double.parseDouble(tds9.get(2).text().replace(",", ""))));
                    prize9.setNum(Integer.parseInt(tds9.get(1).text()));
                }
                detailList.add(prize9);
                j++;
            } else if (tr.text().contains("追加") && j == 3) {
                PrizeDetail prize10 = new PrizeDetail();
                Elements tds10 = tr.getElementsByTag("td");
                prize10.setName("10");
                if (tds10.get(2).text().equals("--") || tds10.get(1).text().equals("--")) {
                    prize10.setAmount(BigDecimal.valueOf(0));
                    prize10.setNum(0);
                } else {
                    prize10.setAmount(BigDecimal.valueOf(Double.parseDouble(tds10.get(2).text().replace(",", ""))));
                    prize10.setNum(Integer.parseInt(tds10.get(1).text()));
                }
                detailList.add(prize10);
                j++;
            } else if (tr.text().contains("追加") && j == 4) {
                PrizeDetail prize11 = new PrizeDetail();
                Elements tds11 = tr.getElementsByTag("td");
                prize11.setName("11");
                if (tds11.get(2).text().equals("--") || tds11.get(1).text().equals("--")) {
                    prize11.setAmount(BigDecimal.valueOf(0));
                    prize11.setNum(0);
                } else {
                    prize11.setAmount(BigDecimal.valueOf(Double.parseDouble(tds11.get(2).text().replace(",", ""))));
                    prize11.setNum(Integer.parseInt(tds11.get(1).text()));
                }
                detailList.add(prize11);
                j++;
            }
        }

        return detailList;
    }

    private Date getDLTDateFrom500W(Document document) {
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
