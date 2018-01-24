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

    @Override
    public String toString() {
        return "PrizeDetail{" +
                "name='" + name + '\'' +
                ", num=" + num +
                ", amount=" + amount +
                '}';
    }
}
