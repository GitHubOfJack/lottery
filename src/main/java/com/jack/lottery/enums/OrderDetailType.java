package com.jack.lottery.enums;

public enum OrderDetailType {
    RECHARGE("1", "用户充值"),
    DRAW("2", "用户体现"),
    BUY_FREEZE("3", "购彩冻结"),
    BUY_SUCCESS("4", "购彩成功,减除金额"),
    WIN("5", "中奖派奖"),
    ;

    private String code;

    private String desc;

    private OrderDetailType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
