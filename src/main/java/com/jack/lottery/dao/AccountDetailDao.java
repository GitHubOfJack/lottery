package com.jack.lottery.dao;

import com.jack.lottery.entity.AccountDetail;
import com.jack.lottery.mapper.AccountDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDetailDao {

    @Autowired
    private AccountDetailMapper accountDetailMapper;

    public void insertDetail(AccountDetail accountDetail) {
        accountDetailMapper.insertSelective(accountDetail);
    }

}
