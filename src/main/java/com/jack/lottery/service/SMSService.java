package com.jack.lottery.service;

import com.alibaba.fastjson.JSONObject;
import com.jack.lottery.dao.SMSCodeDao;
import com.jack.lottery.entity.SMSCode;
import com.jack.lottery.mapper.SMSCodeMapper;
import com.jack.lottery.utils.RandomUtils;
import com.jack.lottery.utils.URLConnectionUtil;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

@Component
public class SMSService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${sms.url}")
    private String url;

    @Value("${sms.user}")
    private String user;

    @Value("${sms.pwd}")
    private String pwd;

    @Value("${sms.product.code}")
    private String productCode;

    @Value("${sms.voice.user}")
    private String voiceUser;

    @Value("${sms.voice.pwd}")
    private String voicePwd;

    @Value("${sms.voice.product.code}")
    private String voiceProductCode;

    @Value("${sms.content.templet}")
    private String smsContent;

    @Autowired
    private SMSCodeDao smsCodeDao;

    public boolean send(String mobile, int type) {
        String verificationCode = RandomUtils.getRandomNumber(4);
        if (1 == type) {
            return sendVoiceMsg(mobile, verificationCode);
        } else {
            return sendMsg(mobile, verificationCode);
        }
    }

    /**
     * 发送验证码短信
     * */
    private boolean sendMsg(String mobile, String verificationCode) {
        String content = String.format(smsContent, verificationCode);
        String smsParam = createSMSParam(mobile, content, 0);
        String response = URLConnectionUtil.doGet(url, smsParam);
        if (checkResponse(response)) {
            //发送成功，插入数据库
            smsCodeDao.insertSMSCode(mobile, verificationCode);
            return true;
        }
        return false;
    }

    /**
     * 发送语音验证码短信
     * */
    private boolean sendVoiceMsg(String mobile, String verificationCode) {
        String smsParam = createSMSParam(mobile, verificationCode, 1);
        String response = URLConnectionUtil.doGet(url, smsParam);
        if (checkResponse(response)) {
            //发送成功，插入数据库
            smsCodeDao.insertSMSCode(mobile, verificationCode);
            return true;
        }
        return false;
    }

    private String createSMSParam(String mobile, String content, int type) {
        StringBuilder sb = new StringBuilder();
        if (0 == type) {
            sb.append("sname=");
            sb.append(user);
            sb.append("&spwd=");
            sb.append(pwd);
            sb.append("&sprdid=");
            sb.append(productCode);
        } else {
            sb.append("sname=");
            sb.append(voiceUser);
            sb.append("&spwd=");
            sb.append(voicePwd);
            sb.append("&sprdid=");
            sb.append(voiceProductCode);
        }
        sb.append("&scorpid=&sdst=");
        sb.append(mobile);
        sb.append("&smsg=");
        try {
            sb.append(URLEncoder.encode(content, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("短信内容转码失败,发送短信失败.mobile:{}", mobile, e);
            return null;
        }
        return sb.toString();
    }

    private boolean checkResponse(String xml) {
        XMLSerializer serializer = new XMLSerializer();
        serializer.setSkipNamespaces(true);
        JSON read = serializer.read(xml);
        JSONObject json = JSONObject.parseObject(read.toString());
        String state = json.getString("State");
        if (!"0".equals(state)) {
            logger.info("调用短信发送接口,短信发送返回失败,失败code:"+state+",失败原因:"+json.getString("MsgState"));
            return false;
        }
        return true;
    }
}
