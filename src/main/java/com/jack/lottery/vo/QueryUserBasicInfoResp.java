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

    /**
     * 命中率
     * */
    private String winPercent;

    /**
     * 是否实名
     * */
    private boolean cetification;

    /**
     * 是否绑卡
     * */
    private boolean bind;

    /**
     * 银行卡号
     * */
    private String card;

    /**
     * 支行名称
     * */
    private String branch;

    /**
     * 真实姓名
     * */
    private String realName;

    /**
     * 身份证号
     * */
    private String idNo;

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

    public String getWinPercent() {
        return winPercent;
    }

    public void setWinPercent(String winPercent) {
        this.winPercent = winPercent;
    }

    public boolean isCetification() {
        return cetification;
    }

    public void setCetification(boolean cetification) {
        this.cetification = cetification;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    @Override
    public String toString() {
        return "QueryUserBasicInfoResp{" +
                "nickName='" + nickName + '\'' +
                ", balance=" + balance +
                ", winPrize=" + winPrize +
                ", mobile='" + mobile + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", winPercent='" + winPercent + '\'' +
                ", cetification=" + cetification +
                ", bind=" + bind +
                ", card='" + card + '\'' +
                ", branch='" + branch + '\'' +
                ", realName='" + realName + '\'' +
                ", idNo='" + idNo + '\'' +
                '}';
    }
}
