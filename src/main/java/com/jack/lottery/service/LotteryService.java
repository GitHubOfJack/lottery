package com.jack.lottery.service;


import com.jack.lottery.dao.LotteryTermDao;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.GetHistoryTermResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LotteryService {
    @Autowired
    private LotteryTermDao lotteryTermDao;

    public LotteryTerm getCurrentTerm(int type) throws BaseException {
        return lotteryTermDao.getCurrentTerm(type);
    }

    public GetHistoryTermResp getHistoryTerm(int type, int pageNo, int pageSize) throws ParamException {
        LotteryType typeByCode = LotteryType.getTypeByCode(type);
        int total = lotteryTermDao.countHistoryTermNum(type);
        int page = total % pageSize != 0 ? total / pageSize + 1 : total / pageSize;
        List<LotteryTerm> historyTerms = lotteryTermDao.getHistoryTerms(type, pageNo, pageSize);
        GetHistoryTermResp resp = new GetHistoryTermResp();
        resp.setTotal(total);
        resp.setTotalPage(page);
        resp.setTerms(historyTerms);
        return resp;
    }

    @Transactional
    public void insertTerm(LotteryType type, String termNo, Date endTime) throws BaseException {
        LotteryTerm currentTerm = getCurrentTerm(type.getCode());
        currentTerm.setIscurrent((byte)1);
        currentTerm.setStatus("2");//不可售
        lotteryTermDao.updateTerm(currentTerm);
        LotteryTerm term = new LotteryTerm();
        term.setType(String.valueOf(type.getCode()));
        term.setTerm(termNo);
        term.setStatus("1");//可售
        term.setStarttime(new Date());//
        term.setIscurrent((byte)0);
        term.setEndtime(endTime);
        lotteryTermDao.insertTerm(term);
    }

    public void updateTermByTermNo(LotteryType type, String term, String result) {
        LotteryTerm lotteryTerm = new LotteryTerm();
        lotteryTerm.setResult(result);
        lotteryTerm.setTerm(term);
        lotteryTerm.setType(String.valueOf(type.getCode()));
        lotteryTermDao.updateTerm(lotteryTerm);
    }
}
