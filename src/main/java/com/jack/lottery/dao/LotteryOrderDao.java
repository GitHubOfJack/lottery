package com.jack.lottery.dao;

import com.jack.lottery.entity.LotteryOrder;
import com.jack.lottery.mapper.LotteryOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LotteryOrderDao {

    @Autowired
    private LotteryOrderMapper lotteryOrderMapper;

    public void insertOrder(LotteryOrder order) {
        lotteryOrderMapper.insertSelective(order);
    }
}
