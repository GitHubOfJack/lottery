package com.jack.lottery.enums;

import com.jack.lottery.utils.exception.ParamException;

import java.util.HashMap;
import java.util.Map;

public enum  LotteryOrderStatus {
    SUBMIT("1", "已提交", "未出票", "未开奖"),
    SEND_SUCCESS("2", "发送出票商成功", "已出票", "未开奖"),
    SEND_FAIL("3", "发送出票商失败", "出票失败", "未开奖"),
    SUCCESS("4", "出票成功", "出票成功", "未开奖"),
    FAIL("5", "出票失败", "出票失败", "未开奖"),
    WIN("6", "已中奖", "出票成功", "已中奖"),
    CASH("7", "已兑奖", "出票成功", "已中奖"),
    WIN_FAIL("8", "未中奖", "出票成功", "未中奖"),
    ;

    private String code;

    private String desc;

    private String ticketStatus;

    private String winStatus;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getWinStatus() {
        return winStatus;
    }

    private LotteryOrderStatus(String code, String desc, String ticketStatus, String winStatus) {
        this.code = code;
        this.desc = desc;
        this.ticketStatus = ticketStatus;
        this.winStatus = winStatus;
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
