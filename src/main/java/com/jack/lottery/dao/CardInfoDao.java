package com.jack.lottery.dao;

import com.jack.lottery.entity.CardInfo;
import com.jack.lottery.mapper.CardInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CardInfoDao {
    @Autowired
    private CardInfoMapper cardInfoMapper;

    public CardInfo getCardInfoById(long id) {
        return cardInfoMapper.selectByPrimaryKey(id);
    }
}
