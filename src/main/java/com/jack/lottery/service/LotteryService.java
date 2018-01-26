package com.jack.lottery.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.lottery.dao.LotteryTermDao;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.po.LotteryHistory;
import com.jack.lottery.po.PrizeDetail;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.GetHistoryTermResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

    public LotteryHistory getLotteryHistory(LotteryType type, String termNo) throws ParamException {
        LotteryTerm term = lotteryTermDao.getLotteryTermByTypeAndTermNo(type, termNo);
        LotteryHistory history = new LotteryHistory();
        history.setType(term.getType());
        history.setOpenDate(term.getEndtime());
        history.setResult(term.getResult());
        history.setTermNo(termNo);
        history.setDetails(createPrizeDetails(type, term.getPrizeDetail()));
        return history;
    }

    private List<PrizeDetail> createPrizeDetails(LotteryType type, String prizeDetail) {
        JSONArray jsonArray = JSONObject.parseArray(prizeDetail);
        List<PrizeDetail> details = new ArrayList<>();
        for (int i=0; i<jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            PrizeDetail detailAdd = null;
            if (type.equals(LotteryType.DLT) && "7".equals(name)) {
                detailAdd = details.get(0);
            } else if (type.equals(LotteryType.DLT) && "8".equals(name)) {
                detailAdd = details.get(1);
            } else if (type.equals(LotteryType.DLT) && "9".equals(name)) {
                detailAdd = details.get(2);
            } else if (type.equals(LotteryType.DLT) && "10".equals(name)) {
                detailAdd = details.get(3);
            } else if (type.equals(LotteryType.DLT) && "11".equals(name)) {
                detailAdd = details.get(4);
            }
            if (null != detailAdd) {
                detailAdd.setAddAmount(jsonObject.getBigDecimal("amount"));
                detailAdd.setAddNum(jsonObject.getIntValue("num"));
                continue;
            }
            PrizeDetail detail = new PrizeDetail();
            detail.setName(name);
            detail.setNum(jsonObject.getIntValue("num"));
            detail.setAmount(jsonObject.getBigDecimal("amount"));
            details.add(detail);
        }
        return details;
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
