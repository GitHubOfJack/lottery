package com.jack.lottery.dao;

import com.jack.lottery.entity.Account;
import com.jack.lottery.entity.AccountExample;
import com.jack.lottery.mapper.AccountMapper;
import com.jack.lottery.utils.exception.ParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDao {

    @Autowired
    private AccountMapper accountMapper;

    public Account getAccountByUserId(long userId) throws ParamException {
        AccountExample example = new AccountExample();
        example.createCriteria().andUseridEqualTo(userId);
        List<Account> accounts = accountMapper.selectByExample(example);
        if (null == accounts || accounts.isEmpty()) {
            throw new ParamException("用户账户不存在");
        }
        return accounts.get(0);
    }

    public void updateAccountById(Account account) {
        accountMapper.updateByPrimaryKeySelective(account);
    }
}
