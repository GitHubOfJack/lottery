package com.jack.lottery.vo;

import com.jack.lottery.entity.LotteryOrder;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.LotteryOrderStatus;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.utils.exception.ParamException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaoma3 on 2018/1/27.
 */
public class LotteryOrderDetail {
    //订单详情信息
    private OrderDetailInfo orderDetailInfo;

    //出票详情信息
    private List<TicketDetailInfo> ticketDetailInfos;

    public OrderDetailInfo getOrderDetailInfo() {
        return orderDetailInfo;
    }

    public void setOrderDetailInfo(OrderDetailInfo orderDetailInfo) {
        this.orderDetailInfo = orderDetailInfo;
    }

    public List<TicketDetailInfo> getTicketDetailInfos() {
        return ticketDetailInfos;
    }

    public void setTicketDetailInfos(List<TicketDetailInfo> ticketDetailInfos) {
        this.ticketDetailInfos = ticketDetailInfos;
    }

    @Override
    public String toString() {
        return "LotteryOrderDetail{" +
                "orderDetailInfo=" + orderDetailInfo +
                ", ticketDetailInfos=" + ticketDetailInfos +
                '}';
    }

    public OrderDetailInfo buildOrderDetailInfo(LotteryOrder order, LotteryTerm term) throws ParamException {
        OrderDetailInfo info = new OrderDetailInfo();
        String lotteryType = order.getLotteryType();
        LotteryType type = LotteryType.getTypeByCode(lotteryType);
        info.setLotteryType(type.getDesc());
        info.setAddition(order.getAddition());
        info.setAmount(order.getAmount());
        info.setMutilpe(order.getMutiply());
        info.setOpenResult(term.getResult());
        info.setOrderNo(order.getOrderId());
        info.setOrderTime(order.getCreateTime());
        info.setPlayType("直选");
        if ((LotteryType.SD.equals(type) || LotteryType.PLS.equals(type))) {
            if (order.getLotteryContent().contains("zs_bh")) {
                info.setPlayType("组三包号");
            } else if (order.getLotteryContent().contains("zl_bh")) {
                info.setPlayType("组六包号");
            }
        }
        info.setStopAfterWin(order.getStopAfterWin().equals("0") ? true : false);
        info.setTermNo(term.getTerm());
        BigDecimal winPrize = null == order.getWinPrize() ? BigDecimal.ZERO : order.getWinPrize();
        info.setWinAmount(winPrize);
        LotteryOrderStatus orderStatus = LotteryOrderStatus.getByCode(order.getStatus());
        info.setTicketResult(orderStatus.getTicketStatus());
        info.setWinResult(orderStatus.getWinStatus());
        info.setContents(Arrays.asList(order.getLotteryContent().split("\\^")));
        return info;
    }

    public List<TicketDetailInfo> buildTicketDetailInfo(String notifyMsg) {
        return new ArrayList<>();
    }

    private class OrderDetailInfo {
        //彩票类型
        private String lotteryType;
        //期号
        private String termNo;
        //投注内容
        private List<String> contents;
        //金额
        private BigDecimal amount;
        //中奖金额
        private BigDecimal winAmount;
        //倍投注数
        private int mutilpe;
        //追号
        private int addition;
        //中奖是否停止追号
        private boolean stopAfterWin;
        //购买时间
        private Date orderTime;
        //玩法类型
        private String playType;
        //订单编号
        private String orderNo;
        //开奖结果
        private String openResult;
        //中奖状态
        private String winResult;
        //出票状态
        private String ticketResult;

        public String getLotteryType() {
            return lotteryType;
        }

        public void setLotteryType(String lotteryType) {
            this.lotteryType = lotteryType;
        }

        public String getTermNo() {
            return termNo;
        }

        public void setTermNo(String termNo) {
            this.termNo = termNo;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getWinAmount() {
            return winAmount;
        }

        public void setWinAmount(BigDecimal winAmount) {
            this.winAmount = winAmount;
        }

        public int getMutilpe() {
            return mutilpe;
        }

        public void setMutilpe(int mutilpe) {
            this.mutilpe = mutilpe;
        }

        public int getAddition() {
            return addition;
        }

        public void setAddition(int addition) {
            this.addition = addition;
        }

        public boolean isStopAfterWin() {
            return stopAfterWin;
        }

        public void setStopAfterWin(boolean stopAfterWin) {
            this.stopAfterWin = stopAfterWin;
        }

        public Date getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(Date orderTime) {
            this.orderTime = orderTime;
        }

        public String getPlayType() {
            return playType;
        }

        public void setPlayType(String playType) {
            this.playType = playType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getOpenResult() {
            return openResult;
        }

        public void setOpenResult(String openResult) {
            this.openResult = openResult;
        }

        public String getWinResult() {
            return winResult;
        }

        public void setWinResult(String winResult) {
            this.winResult = winResult;
        }

        public String getTicketResult() {
            return ticketResult;
        }

        public void setTicketResult(String ticketResult) {
            this.ticketResult = ticketResult;
        }

        public List<String> getContents() {
            return contents;
        }

        public void setContents(List<String> contents) {
            this.contents = contents;
        }

        @Override
        public String toString() {
            return "OrderDetailInfo{" +
                    "lotteryType='" + lotteryType + '\'' +
                    ", termNo='" + termNo + '\'' +
                    ", contents=" + contents +
                    ", amount=" + amount +
                    ", winAmount=" + winAmount +
                    ", mutilpe=" + mutilpe +
                    ", addition=" + addition +
                    ", stopAfterWin=" + stopAfterWin +
                    ", orderTime=" + orderTime +
                    ", playType='" + playType + '\'' +
                    ", orderNo='" + orderNo + '\'' +
                    ", openResult='" + openResult + '\'' +
                    ", winResult='" + winResult + '\'' +
                    ", ticketResult='" + ticketResult + '\'' +
                    '}';
        }
    }

    private class TicketDetailInfo {
        private String content;

        private String code;

        private String playType;

        private String ticketStatus;

        private int mitiple;

        private String drawType;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPlayType() {
            return playType;
        }

        public void setPlayType(String playType) {
            this.playType = playType;
        }

        public String getTicketStatus() {
            return ticketStatus;
        }

        public void setTicketStatus(String ticketStatus) {
            this.ticketStatus = ticketStatus;
        }

        public int getMitiple() {
            return mitiple;
        }

        public void setMitiple(int mitiple) {
            this.mitiple = mitiple;
        }

        public String getDrawType() {
            return drawType;
        }

        public void setDrawType(String drawType) {
            this.drawType = drawType;
        }

        @Override
        public String toString() {
            return "TicketDetailInfo{" +
                    "content='" + content + '\'' +
                    ", code='" + code + '\'' +
                    ", playType='" + playType + '\'' +
                    ", ticketStatus='" + ticketStatus + '\'' +
                    ", mitiple=" + mitiple +
                    ", drawType='" + drawType + '\'' +
                    '}';
        }
    }
}
