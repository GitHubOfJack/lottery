package com.jack.lottery.vo;

import com.jack.lottery.entity.LotteryTerm;

import java.util.List;

public class GetHistoryTermResp {
    private int total;

    private int totalPage;

    private List<LotteryTerm> terms;

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

    public List<LotteryTerm> getTerms() {
        return terms;
    }

    public void setTerms(List<LotteryTerm> terms) {
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "GetHistoryTermResp{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", terms=" + terms +
                '}';
    }
}
