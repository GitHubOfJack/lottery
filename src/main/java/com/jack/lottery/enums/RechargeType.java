package com.jack.lottery.enums;

import com.jack.lottery.utils.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

public enum RechargeType {
    ALIPAY("1", "支付宝充值"),
    TENPAY("2", "微信充值"),
    KPAY("3", "快捷支付"),
    ;
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private RechargeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, RechargeType> map = new HashMap<>();

    static {
        for (RechargeType type : RechargeType.values()) {
            map.put(type.getCode(), type);
        }
    }

    public static RechargeType getTypeByCode(String code) throws ParamException {
        RechargeType rechargeType = map.get(code);
        if (null == rechargeType) {
            throw new ParamException("充值类型不正确|充值类型:"+code);
        }
        return rechargeType;
    }
}
