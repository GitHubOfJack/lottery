package com.jack.lottery.buss;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.enums.WEBSITE;
import com.jack.lottery.po.LotteryHistory;
import com.jack.lottery.po.PrizeDetail;
import com.jack.lottery.service.LotteryService;
import com.jack.lottery.utils.URLConnectionUtil;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.utils.exception.SystermException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.*;

@Component
public class LotteryBuss {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${task.fetch.history.ssq.urls}")
    private String ssqUrls;

    @Value("${task.fetch.history.dlt.urls}")
    private String dltUrls;

    @Value("${task.fetch.history.pls.urls}")
    private String plsUrls;

    @Value("${task.fetch.history.plw.urls}")
    private String plwUrls;

    @Value("${task.fetch.history.sd.urls}")
    private String sdUrls;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryCacheBuss lotteryCacheBuss;

    /**
     * 验证投注内容
     * type=1 双色球   fs-06,07,11,14,30,31|10（1注） fs-01,10,12,17,23,32|05,11（2注） fs-02,10,12,14,22,25,27|15（7注）
     * type=2 大乐透   fs-12,17,20,21,29|01,05（普通投注1注） fs-19,23,28,29,33|02,05,09（普通投注3注） fs-04,12,17,22,30,34|01,12（普通投注6注）
     * type=3 排列三   fs-3,8,3（直选1注） zs_bh-8,4（组三包号2注） zl_bh-6,2,8（组六包号1注） zl_bh-4,5,7^zs_bh-4,2^fs-9,5,1（4注）
     * type=4 排列五   fs-9,8,2,5,0（单式投注）
     * type=5 福彩3D   fs-3,8,3（直选1注） zs_bh-8,4（组三包号2注） zl_bh-6,2,8（组六包号1注） zl_bh-4,5,7^zs_bh-4,2^fs-9,5,1（4注）
     */
    public void checkContent(String type, String content, int num, BigDecimal amt, int muti) throws BaseException {
        LotteryType lotteryType = LotteryType.getTypeByCode(type);
        if (StringUtils.isBlank(content)) {
            throw new ParamException("投注格式不正确");
        }
        if (0 >= num) {
            throw new ParamException("总注数不正确");
        }
        if (BigDecimal.valueOf(2).compareTo(amt) > 0) {
            throw new ParamException("金额不正确");
        }
        if (0 >= muti) {
            muti = 1;
        }
        if (BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(num))
                .multiply(BigDecimal.valueOf(muti)).compareTo(amt) != 0) {
            throw new ParamException("总注数和金额不匹配");
        }
        switch (lotteryType) {
            case SSQ:
                checkSSQ(content, num);
                break;
            case DLT:
                checkDLT(content, num);
                break;
            case PLS:
                checkPLS(content, num);
                break;
            case PLW:
                checkPLW(content, num);
                break;
            case SD:
                checkSD(content, num);
                break;
        }
    }

    //6个红球最大33，最小01，1个蓝球最大16，最小01
    //fs-06,07,11,14,30,31|10^fs-01,10,12,17,23,32|05,11
    private void checkSSQ(String content, int num) throws ParamException, SystermException {
        int allNum = 0;
        String[] orders = content.split("\\^");
        for (String order : orders) {
            String type = order.split("-")[0];
            if (!"fs".equals(type)) {
                throw new ParamException("投注格式不正确");
            }
            String oneOrder = order.split("-")[1];
            String[] contents = oneOrder.split("\\|");
            if (contents.length != 2) {
                throw new ParamException("投注格式不正确");
            }
            String red = contents[0];
            String blue = contents[1];
            if (StringUtils.isBlank(red) || StringUtils.isBlank(blue)) {
                throw new ParamException("投注格式不正确");
            }
            String[] reds = red.split(",");
            for (String r : reds) {
                if (!StringUtils.isNumeric(r) || r.compareTo("34") >= 0 || r.compareTo("00") <= 0) {
                    throw new ParamException("投注格式不正确");
                }
            }
            String[] blues = blue.split(",");
            for (String b : blues) {
                if (!StringUtils.isNumeric(b) || b.compareTo("17") >= 0 || b.compareTo("00") <= 0) {
                    throw new ParamException("投注格式不正确");
                }
            }
            allNum = allNum + (getNFromM(6, reds.length) * getNFromM(1, blues.length));
        }
        if (num != allNum) {
            throw new ParamException("投注总注数不正确");
        }
    }

    //总共三个数，每个数字都是从0到9
    //zl_bh-4,5,7^zs_bh-4,2^fs-9,5,1
    private void checkSD(String content, int num) throws BaseException {
        checkPLS(content, num);
    }

    //总共五个数，每个数字都是从0到9
    //fs-9,8,2,5,0^fs-8,9,2,5,1
    private void checkPLW(String content, int num) throws ParamException {
        int allNum = 0;
        String[] orders = content.split("\\^");
        for (String order : orders) {
            String type = order.split("-")[0];
            if (!"fs".equals(type)) {
                throw new ParamException("投注格式不正确");
            }
            String oneOrder = order.split("-")[1];
            allNum = allNum + checkPL(oneOrder, 5);
        }
        if (allNum != num) {
            throw new ParamException("投注总注数不正确");
        }
    }

    //总共三个数，每个数字都是从0到9
    //zl_bh-4,5,7^zs_bh-4,2^fs-9,5,1
    private void checkPLS(String content, int num) throws BaseException {
        int allNum = 0;
        String[] orders = content.split("\\^");
        for (String order : orders) {
            String type = order.split("-")[0];
            String contents = order.split("-")[1];
            if ("zl_bh".equals(type)) {
                allNum = allNum + checkZLBH(contents);
            } else if ("zs_bh".equals(type)) {
                allNum = allNum + checkZSBH(contents);
            } else if ("fs".equals(type)) {
                allNum = allNum + checkPL(contents, 3);
            } else {
                throw new ParamException("投注格式不正确");
            }
        }
        if (allNum != num) {
            throw new ParamException("投注总注数不正确");
        }
    }

    private int checkZSBH(String content) throws BaseException {
        String[] contents = content.split(",");
        if (contents.length < 2) {
            throw new ParamException("投注格式不正确");
        }
        for (String s : contents) {
            if (StringUtils.isBlank(s) || !StringUtils.isNumeric(s) || !isBetween(s, "0", "9")) {
                throw new ParamException("投注格式不正确");
            }
        }
        return contents.length * (contents.length - 1);
    }

    private int checkZLBH(String content) throws BaseException {
        String[] contents = content.split(",");
        if (contents.length < 3) {
            throw new ParamException("投注格式不正确");
        }
        for (String s : contents) {
            if (StringUtils.isBlank(s) || !StringUtils.isNumeric(s) || !isBetween(s, "0", "9")) {
                throw new ParamException("投注格式不正确");
            }
        }
        return getNFromM(3, contents.length);
    }

    private int checkPL(String content, int length) throws ParamException {
        String[] contents = content.split(",");
        if (contents.length != length) {
            throw new ParamException("投注格式不正确");
        }
        int totalNum = 1;
        for (int i=0; i<contents.length; i++) {
            String part = contents[i];
            if (StringUtils.isBlank(part)) {
                throw new ParamException("投注格式不正确");
            }
            String[] parts = part.split("");
            totalNum = totalNum * parts.length;
            for (String s : parts) {
                if (StringUtils.isBlank(s) || !StringUtils.isNumeric(s) || !isBetween(s, "0", "9")) {
                    throw new ParamException("投注格式不正确");
                }
            }
        }
        return totalNum;
    }

    //5个红球最大35，最小01，2个篮球最大12，最小01
    //fs-12,17,20,21,29|01,05^fs-19,23,28,29,33|02,05,09
    private void checkDLT(String content, int num) throws ParamException, SystermException {
        int allNum = 0;
        String[] orders = content.split("\\^");
        for (String order : orders) {
            String type = order.split("-")[0];
            if (!"fs".equals(type)) {
                throw new ParamException("投注格式不正确");
            }
            String oneOrder = order.split("-")[1];
            String[] contents = oneOrder.split("\\|");
            if (contents.length != 2) {
                throw new ParamException("投注格式不正确");
            }
            String red = contents[0];
            String blue = contents[1];
            if (StringUtils.isBlank(red) || StringUtils.isBlank(blue)) {
                throw new ParamException("投注格式不正确");
            }
            String[] reds = red.split(",");
            for (String r : reds) {
                if (!StringUtils.isNumeric(r) || r.compareTo("36") >= 0 || r.compareTo("00") <= 0) {
                    throw new ParamException("投注格式不正确");
                }
            }
            String[] blues = blue.split(",");
            for (String b : blues) {
                if (!StringUtils.isNumeric(b) || b.compareTo("13") >= 0 || b.compareTo("00") <= 0) {
                    throw new ParamException("投注格式不正确");
                }
            }
            allNum = allNum + (getNFromM(5, reds.length) * getNFromM(2, blues.length));
        }
        if (num != allNum) {
            throw new ParamException("投注总注数不正确");
        }
    }


    //从M个数字中筛选N个数字算法
    private int getNFromM(int n, int m) throws SystermException {
        if (n <= 0 || m <= 0) {
            return 0;
        }
        int totalA = 1;
        for (int i=0; i<n; i++) {
            totalA = totalA * (m-i);
        }
        int totalB = 1;
        for (int i=0; i<n; i++) {
            totalB = totalB * (n-i);
        }
        if (totalA % totalB != 0) {
            throw new SystermException("金额计算错误");
        }
        return totalA/totalB;
    }

    private boolean isBetween(String n, String max, String min) {
        int maxInt = Integer.parseInt(max);
        int minInt = Integer.parseInt(min);
        int nInt = Integer.parseInt(n);
        return nInt >= minInt && nInt <= maxInt;
    }

    public LotteryHistory getHistory(LotteryType type, String termNo) {
        if (type.equals(LotteryType.SSQ)) {
            fetchSSQHistory(termNo);
        } else if (type.equals(LotteryType.DLT)) {

        } else if (type.equals(LotteryType.PLS)) {

        } else if (type.equals(LotteryType.PLW)) {

        } else if (type.equals(LotteryType.SD)) {

        }
        return null;
    }

    //获取上期开奖结果
    //澳客网返回数据   {"LotteryResult":"5,10,17,23,26,32|7","PrizeList":{"1":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"1","Prize":"5477449","HitNum":"17"},"2":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"2","Prize":"45908","HitNum":"221"},"3":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"3","Prize":"3000","HitNum":"2633"},"4":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"4","Prize":"200","HitNum":"113710"},"5":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"5","Prize":"10","HitNum":"1793617"},"6":{"LotteryType":"SSQ","LotteryNo":"2018009","PrizeLevel":"6","Prize":"5","HitNum":"19825084"}},"Sale":"384256808","SortNum":["10","17","26","23","05","32","07"],"PoolPrize":"364964347","riqi":["01","21","\u661f\u671f\u65e5"],"date":"2018-01-21"}
    public boolean fetchSSQHistory(String termNo) {
        String result = getSSQHistory(termNo).getResult();
        if (StringUtils.isBlank(result)) {
            return false;
        }
        lotteryService.updateTermByTermNo(LotteryType.SSQ, termNo, result);
        return true;
    }

    private LotteryHistory getSSQHistory(String termNo) {
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
                String result = respJson.getString("LotteryResult").replace(",", "^");
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
                sb.append("^");
            }
            i++;
        }
        return sb.toString();
    }

    private List<PrizeDetail> getSSQPrizeDetailFrom500W(Document document) {
        List<PrizeDetail> detailList = new ArrayList<>();
        Elements trs = document.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");
        for (int i=2; i<8; i++) {
            PrizeDetail prize = new PrizeDetail();
            Elements tds = trs.get(i).getElementsByTag("td");
            prize.setAmount(BigDecimal.valueOf(Double.parseDouble(tds.get(2).text())));
            prize.setName(String.valueOf(i-1));
            prize.setNum(Integer.parseInt(tds.get(1).text()));
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

    public static void main(String[] args) {
        String t = "2018010";
        System.out.println(t.substring(2));
    }
}
