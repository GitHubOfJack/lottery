package com.jack.lottery.po;

import java.math.BigDecimal;

public class PrizeDetail {
    /**
     * 奖金级别
     * */
    private String name;

    /**
     * 中奖注数
     * */
    private int num;

    /**
     * 奖金
     * */
    private BigDecimal amount;

    /**
     * 追加注数
     * */
    private int addNum;

    /**
     * 追加金额
     * */
    private BigDecimal addAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getAddNum() {
        return addNum;
    }

    public void setAddNum(int addNum) {
        this.addNum = addNum;
    }

    public BigDecimal getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(BigDecimal addAmount) {
        this.addAmount = addAmount;
    }

    @Override
    public String toString() {
        return "PrizeDetail{" +
                "name='" + name + '\'' +
                ", num=" + num +
                ", amount=" + amount +
                ", addNum=" + addNum +
                ", addAmount=" + addAmount +
                '}';
    }
}
