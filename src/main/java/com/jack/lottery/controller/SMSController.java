package com.jack.lottery.controller;

import com.jack.lottery.service.SMSService;
import com.jack.lottery.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired
    private SMSService smsService;

    @Value("${sms.content.templet}")
    private String smsContent;

    /**
     * 发送验证码短信
     * type = 0  文字验证码
     * type = 1  语音验证码
     * */
    @RequestMapping("/send")
    public void sendMsg(@RequestParam String userId, @RequestParam int type) {
        String verificationCode = RandomUtils.getRandomNumber(4);
        if (0 == type) {
            smsContent = String.format(smsContent, verificationCode);
            smsService.sendMsg("", smsContent);
        } else {
            smsService.sendVoiceMsg("", verificationCode);
        }
    }
}
