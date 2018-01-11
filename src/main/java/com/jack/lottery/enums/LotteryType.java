package com.jack.lottery.enums;

import com.jack.lottery.utils.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<Integer, LotteryType> map = new HashMap<>();

    static {
        LotteryType[] values = LotteryType.values();
        for (LotteryType type : values) {
            map.put(type.code, type);
        }
    }

    public static LotteryType getTypeByCode(String code) throws ParamException {
        int codeInt = -1;
        try {
            codeInt = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            throw new ParamException("彩票类型不存在");
        }
        return getTypeByCode(codeInt);
    }

    public static LotteryType getTypeByCode(int code) throws ParamException {
        LotteryType lotteryType = map.get(code);
        if (null == lotteryType) {
            throw new ParamException("彩票类型不存在");
        }
        return lotteryType;
    }
}
