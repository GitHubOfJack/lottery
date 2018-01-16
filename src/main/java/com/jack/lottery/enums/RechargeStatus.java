package com.jack.lottery.enums;

import com.jack.lottery.utils.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

public enum RechargeStatus {
    ACCETP("1", "充值请求已受理"),
    SEND_SUCCESS("2", "充值请求已发送至三方"),
    SEND_FAIL("3", "充值请求发送三方失败"),
    RECHARGE_SUCCESS("4", "充值成功"),
    RECHARGE_FAIL("5", "充值失败"),
    ;
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private RechargeStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, RechargeStatus> map = new HashMap<>();

    static {
        for (RechargeStatus type : RechargeStatus.values()) {
            map.put(type.getCode(), type);
        }
    }

    public static RechargeStatus getTypeByCode(String code) throws ParamException {
        RechargeStatus rechargeType = map.get(code);
        if (null == rechargeType) {
            throw new ParamException("充值状态不正确");
        }
        return rechargeType;
    }
}
