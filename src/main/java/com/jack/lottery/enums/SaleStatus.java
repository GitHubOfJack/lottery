package com.jack.lottery.enums;

import java.util.HashMap;
import java.util.Map;

public enum SaleStatus {

    SALE(1, "可售"),
    CANT_SALE(2, "不可售");

    private int code;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private String desc;

    private SaleStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Map<Integer, SaleStatus> map = new HashMap<>();

    static {
        SaleStatus[] values = SaleStatus.values();
        for (SaleStatus value : values) {
            map.put(value.getCode(), value);
        }
    }

    public static SaleStatus getSaleByCode(int code) {
        return map.get(code);
    }
}
