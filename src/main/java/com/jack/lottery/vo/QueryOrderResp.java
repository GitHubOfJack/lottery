package com.jack.lottery.vo;

import com.jack.lottery.entity.LotteryOrder;

import java.util.List;

public class QueryOrderResp {
    private int total;

    private int totalPage;

    private List<LotteryOrder> orders;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<LotteryOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<LotteryOrder> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "QueryOrderResp{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", orders=" + orders +
                '}';
    }
}
