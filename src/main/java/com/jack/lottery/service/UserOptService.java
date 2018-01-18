package com.jack.lottery.service;

import com.jack.lottery.dao.AccountDao;
import com.jack.lottery.dao.CardInfoDao;
import com.jack.lottery.dao.UserDao;
import com.jack.lottery.entity.Account;
import com.jack.lottery.entity.CardInfo;
import com.jack.lottery.entity.User;
import com.jack.lottery.utils.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserOptService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CardInfoDao cardInfoDao;

    @Transactional
    public void doRegister(User user, Account account) throws BaseException {
        userDao.insertUser(user);
        account.setUserid(user.getId());
        accountDao.insertAccount(account);
    }

    @Transactional
    public void doBindCard(User user, CardInfo info, long id) {
        if (-1 == id) {
            cardInfoDao.insertCardInfo(info);
            user.setCardId(info.getId());
            userDao.updateUserByUserId(user);
        } else {
            cardInfoDao.updateCardInfoById(info);
        }
    }
}
