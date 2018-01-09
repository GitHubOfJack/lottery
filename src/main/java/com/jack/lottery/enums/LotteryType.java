package com.jack.lottery.enums;

public enum LotteryType {
    SSQ(1, "双色球"),
    DLT(2, "大乐透"),
    PLS(3, "排列三"),
    PLW(4, "排列五"),
    SD(5, "福彩3D"),
    ;
    private int code;

    private String desc;

    private LotteryType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
