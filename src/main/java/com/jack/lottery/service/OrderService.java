package com.jack.lottery.service;

import com.jack.lottery.buss.LotteryBuss;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.SaleStatus;
import com.jack.lottery.mapper.LotteryOrderMapper;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private LotteryOrderMapper lotteryOrderMapper;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryBuss lotteryBuss;

    public boolean buyLottery(String type, String content,
                              BigDecimal amount, int num) throws BaseException {
        //类型&&彩种是否在可售时间内是否正确
        LotteryTerm currentTerm = lotteryService.getCurrentTerm(Integer.parseInt(type));
        if (!currentTerm.getStatus().equals(String.valueOf(SaleStatus.SALE.getCode())) || currentTerm.getEndtime().compareTo(new Date()) < 0) {
            throw new ParamException("当前期已停止");
        }
        //内容&&金额是否正确
        lotteryBuss.checkContent(type, content, num, amount);
        //生成订单

        //扣除用户金额
        //修改订单状态
        //往出票商发送信息
        //更新订单状态
        return false;
    }
}
