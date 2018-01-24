package com.jack.lottery.enums;

import com.jack.lottery.utils.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

public enum  LotteryOrderStatus {
    SUBMIT("1", "已提交"),
    SEND_SUCCESS("2", "发送出票商成功"),
    SEND_FAIL("3", "发送出票商失败"),
    SUCCESS("4", "出票成功"),
    FAIL("5", "出票失败"),
    WIN("6", "已中奖"),
    CASH("7", "已兑奖"),
    WIN_FAIL("8", "未中奖"),
    ;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private LotteryOrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, LotteryOrderStatus> map = new HashMap<>();

    static {
        for (LotteryOrderStatus status : LotteryOrderStatus.values()) {
            map.put(status.getCode(), status);
        }
    }

    public static LotteryOrderStatus getByCode(String code) throws ParamException {
        LotteryOrderStatus status = map.get(code);
        if (null == status) {
            throw new ParamException("订单状态不正确");
        }
        return status;
    }
}
