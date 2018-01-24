package com.jack.lottery.po;

import java.util.Date;
import java.util.List;

public class LotteryHistory {
    /**
     * 彩票类型
     * */
    private String type;
    /**
     * 期号
     * */
    private String termNo;
    /**
     * 开奖日期
     * */
    private Date openDate;
    /**
     * 开奖结果
     * */
    private String result;
    /**
     * 奖金详情
     * */
    private List<PrizeDetail> details;

    public String getTermNo() {
        return termNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<PrizeDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PrizeDetail> details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LotteryHistory{" +
                "type='" + type + '\'' +
                ", termNo='" + termNo + '\'' +
                ", openDate=" + openDate +
                ", result='" + result + '\'' +
                ", details=" + details +
                '}';
    }
}
