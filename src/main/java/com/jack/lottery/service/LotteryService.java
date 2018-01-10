package com.jack.lottery.service;

import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.entity.LotteryTermExample;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.mapper.LotteryTermMapper;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryService {
    @Autowired
    private LotteryTermMapper lotteryTermMapper;

    public LotteryTerm getCurrentTerm(int type) throws BaseException {
        LotteryType typeByCode = LotteryType.getTypeByCode(type);
        LotteryTermExample example = new LotteryTermExample();
        example.createCriteria().andTypeEqualTo(String.valueOf(typeByCode.getCode()))
                .andIscurrentEqualTo(Byte.valueOf("0"));
        List<LotteryTerm> lotteryTerms = lotteryTermMapper.selectByExample(example);
        if (null == lotteryTerms || lotteryTerms.isEmpty()) {
            throw new ParamException("当前期不存在");
        }
        return lotteryTerms.get(0);
    }
}
