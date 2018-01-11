package com.jack.lottery.service;

import com.jack.lottery.dao.AccountDao;
import com.jack.lottery.dao.AccountDetailDao;
import com.jack.lottery.dao.LotteryOrderDao;
import com.jack.lottery.entity.Account;
import com.jack.lottery.entity.AccountDetail;
import com.jack.lottery.entity.LotteryOrder;
import com.jack.lottery.utils.exception.ParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderOptService {

    @Autowired
    private LotteryOrderDao lotteryOrderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountDetailDao accountDetailDao;

    //订单购买操作
    //插入订单表-更新账户表冻结金额和可用金额-插入账户明细表
    @Transactional
    public void buyOrder(LotteryOrder order, Account account, AccountDetail accountDetail) throws ParamException {
        lotteryOrderDao.insertOrder(order);
        accountDao.updateAccountById(account);
        accountDetail.setOrderid(String.valueOf(order.getId()));
        accountDetailDao.insertDetail(accountDetail);
    }

}
