package com.jack.lottery.dao;

import com.jack.lottery.entity.RechargeOrder;
import com.jack.lottery.entity.RechargeOrderExample;
import com.jack.lottery.mapper.RechargeOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RechargeOrderDao {
    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    public void insertOrder(RechargeOrder order) {
        rechargeOrderMapper.insertSelective(order);
    }

    public List<RechargeOrder> getOrdersByUserId(long userId) {
        RechargeOrderExample example = new RechargeOrderExample();
        example.createCriteria().andUseridEqualTo(userId);
        List<RechargeOrder> orders = rechargeOrderMapper.selectByExample(example);
        return orders;
    }
}
