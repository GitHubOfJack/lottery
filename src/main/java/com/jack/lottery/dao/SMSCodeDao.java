package com.jack.lottery.dao;

import com.jack.lottery.entity.SMSCode;
import com.jack.lottery.entity.SMSCodeExample;
import com.jack.lottery.mapper.SMSCodeMapper;
import com.jack.lottery.utils.exception.ParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class SMSCodeDao {
    @Autowired
    private SMSCodeMapper sMSCodeMapper;

    public void insertSMSCode(String mobile, String code) {
        SMSCode model = new SMSCode();
        model.setCode(code);
        model.setCreateTime(new Date());
        model.setMobile(mobile);
        sMSCodeMapper.insert(model);
    }

    //获取最新的验证码
    public SMSCode getLatestSmsCode(String mobile) throws ParamException {
        SMSCodeExample example = new SMSCodeExample();
        example.setOrderByClause(" id desc ");
        example.createCriteria().andMobileEqualTo(mobile);
        List<SMSCode> smsCodes = sMSCodeMapper.selectByExample(example);
        if (null == smsCodes || smsCodes.isEmpty()) {
            throw new ParamException("发送短信验证码异常");
        }
        return smsCodes.get(0);
    }
}
