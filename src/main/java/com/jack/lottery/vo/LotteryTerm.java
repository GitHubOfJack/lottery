package com.jack.lottery.vo;

import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.enums.SaleStatus;

import java.util.Date;

public class LotteryTerm {
    /**
     * 当前期号
     * */
    private String termNo;

    /**
     * 当期开始售卖时间
     * */
    private Date startDate;

    /**
     * 当期结束售卖时间
     * */
    private Date endDate;

    /**
     * 开奖时间
     * */
    private Date awardDate;

    /**
     * 彩种类型
     * */
    private LotteryType type;

    /**
     * 售卖状态
     * */
    private SaleStatus saleStatus;

    public String getTermNo() {
        return termNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public LotteryType getType() {
        return type;
    }

    public void setType(LotteryType type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public SaleStatus getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(SaleStatus saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(Date awardDate) {
        this.awardDate = awardDate;
    }

    @Override
    public String toString() {
        return "LotteryTerm{" +
                "termNo='" + termNo + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", awardDate=" + awardDate +
                ", type=" + type +
                ", saleStatus=" + saleStatus +
                '}';
    }
}
