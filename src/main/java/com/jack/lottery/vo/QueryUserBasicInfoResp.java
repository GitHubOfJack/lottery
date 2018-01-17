package com.jack.lottery.vo;

import java.math.BigDecimal;

public class QueryUserBasicInfoResp {
    /**
     * 用户昵称
     * */
    private String nickName;
    /**
     * 用户余额
     * */
    private BigDecimal balance;
    /**
     * 用户累计中奖金额
     * */
    private BigDecimal winPrize;
    /**
     * 用户手机号
     * */
    private String mobile;
    /**
     * 用户头像
     * */
    private String imgUrl;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getWinPrize() {
        return winPrize;
    }

    public void setWinPrize(BigDecimal winPrize) {
        this.winPrize = winPrize;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "QueryUserBasicInfoResp{" +
                "nickName='" + nickName + '\'' +
                ", balance=" + balance +
                ", winPrize=" + winPrize +
                ", mobile='" + mobile + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
