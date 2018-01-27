package com.jack.lottery.dao;

import com.github.pagehelper.PageHelper;
import com.jack.lottery.entity.LotteryOrder;
import com.jack.lottery.entity.LotteryOrderExample;
import com.jack.lottery.mapper.LotteryOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LotteryOrderDao {

    @Autowired
    private LotteryOrderMapper lotteryOrderMapper;

    public void insertOrder(LotteryOrder order) {
        lotteryOrderMapper.insertSelective(order);
    }

    public int getOrderCountByUserIdAndStatus(long userId, String status) {
        LotteryOrderExample example = new LotteryOrderExample();
        LotteryOrderExample.Criteria criteria = example.createCriteria().andUseridEqualTo(userId);
        if (!StringUtils.isBlank(status)) {
            criteria.andStatusEqualTo(status);
        }
        return lotteryOrderMapper.countByExample(example);
    }

    public List<LotteryOrder> getOrdersByUserIdAndStatus(long userId, String status, int pageNo, int pageSize) {
        LotteryOrderExample example = new LotteryOrderExample();
        LotteryOrderExample.Criteria criteria = example.createCriteria().andUseridEqualTo(userId);
        if (!StringUtils.isBlank(status)) {
            criteria.andStatusEqualTo(status);
        }
        PageHelper.startPage(pageNo, pageSize);
        List<LotteryOrder> lotteryOrders = lotteryOrderMapper.selectByExample(example);
        return lotteryOrders;
    }

    public LotteryOrder getOrderById(long orderId) {
        return lotteryOrderMapper.selectByPrimaryKey(orderId);
    }
}
