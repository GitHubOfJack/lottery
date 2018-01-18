package com.jack.lottery.dao;

import com.github.pagehelper.PageHelper;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.entity.LotteryTermExample;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.mapper.LotteryTermMapper;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LotteryTermDao {
    @Autowired
    private LotteryTermMapper lotteryTermMapper;

    public LotteryTerm getCurrentTerm(int type) throws BaseException {
        LotteryType typeByCode = LotteryType.getTypeByCode(type);
        LotteryTermExample example = new LotteryTermExample();
        example.createCriteria().andTypeEqualTo(String.valueOf(typeByCode.getCode()))
                .andIscurrentEqualTo(Byte.valueOf("0"));
        List<LotteryTerm> lotteryTerms = lotteryTermMapper.selectByExample(example);
        if (null == lotteryTerms || lotteryTerms.isEmpty()) {
            throw new ParamException("当前期不存在|type="+type);
        }
        return lotteryTerms.get(0);
    }

    public int countHistoryTermNum(int type) {
        LotteryTermExample example = new LotteryTermExample();
        example.createCriteria().andTypeEqualTo(String.valueOf(type))
                .andIscurrentEqualTo(Byte.valueOf("1"));
        return lotteryTermMapper.countByExample(example);
    }

    public List<LotteryTerm> getHistoryTerms(int type, int pageNo, int pageSize) {
        LotteryTermExample example = new LotteryTermExample();
        example.setOrderByClause(" term desc ");
        example.createCriteria().andTypeEqualTo(String.valueOf(type))
                .andIscurrentEqualTo(Byte.valueOf("1"));
        PageHelper.startPage(pageNo, pageSize);
        return lotteryTermMapper.selectByExample(example);
    }
}
