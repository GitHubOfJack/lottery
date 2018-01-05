package com.jack.lottery.vo;

public class CommonResponose<T> {
    /**
     * 响应码
     * */
    private String code;
    /**
     * 响应描述
     * */
    private String msg;
    /**
     * 返回数据对象
     * */
    private T data;

    public CommonResponose(T data) {
        this.code = ResponseCode.SUCCESS.getResCode();
        this.msg = ResponseCode.SUCCESS.getResDesc();
        this.data = data;
    }

    public CommonResponose(ResponseCode resp, T data) {
        this.code = resp.getResCode();
        this.msg = resp.getResDesc();
        this.data = data;
    }

    public CommonResponose(ResponseCode resp, T data, String appandMsg) {
        this.code = resp.getResCode();
        this.msg = resp.getResDesc()+":"+appandMsg;
        this.data = data;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResponose{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
