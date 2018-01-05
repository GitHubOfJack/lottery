package com.jack.lottery.vo;

public enum ResponseCode {

    SUCCESS("0", "操作成功"),
    PARAM_ERROR("1", "参数异常"),
    INTERFACE_ERROR("2", "远程接口异常"),
    DB_ERROR("3", "数据库异常"),
    UNKNOW_ERROR("4", "未知异常"),
    ;

    private String resCode;
    private String resDesc;
    private boolean isSuccess;

    private ResponseCode(String resCode, String resDesc) {
        this.resCode = resCode;
        this.resDesc = resDesc;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public static ResponseCode getResponseByCode(String resCode) {
        for (ResponseCode mrc : ResponseCode.values()) {
            if (mrc.getResCode().equals(resCode)) {
                return mrc;
            }
        }
        return null;
    }
}
