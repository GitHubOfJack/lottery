package com.jack.lottery.dao;

import com.jack.lottery.entity.CardInfo;
import com.jack.lottery.entity.CardInfoExample;
import com.jack.lottery.mapper.CardInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardInfoDao {
    @Autowired
    private CardInfoMapper cardInfoMapper;

    public CardInfo getCardInfoById(long id) {
        return cardInfoMapper.selectByPrimaryKey(id);
    }

    public void insertCardInfo(CardInfo info) {
        cardInfoMapper.insertSelective(info);
    }

    public CardInfo getCardInfoByCardNo(String cardNo) {
        CardInfoExample example = new CardInfoExample();
        example.createCriteria().andCardNoEqualTo(cardNo);
        List<CardInfo> cardInfos = cardInfoMapper.selectByExample(example);
        if (null == cardInfos || cardInfos.isEmpty()) {
            return null;
        }
        return cardInfos.get(0);
    }

    public void updateCardInfoById(CardInfo info) {
        cardInfoMapper.updateByPrimaryKeySelective(info);
    }
}
