package com.jack.lottery.service;


import com.jack.lottery.dao.LotteryTermDao;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.utils.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotteryService {
    @Autowired
    private LotteryTermDao lotteryTermDao;

    public LotteryTerm getCurrentTerm(int type) throws BaseException {
        return lotteryTermDao.getCurrentTerm(type);
    }
}
