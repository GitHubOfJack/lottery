package com.jack.lottery.enums;

public enum WEBSITE {
    FIVEBILLION("500w", "500W彩票网"),
    TAOBAO("taobao", "淘宝彩票"),
    OK("ok", "澳客网"),
    WOZHONGLA("wzl", "我中了彩票网"),
    DAYINGJIA("dyj", "大赢家彩票网"),
    ;
    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private WEBSITE(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
