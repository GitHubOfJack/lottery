package com.jack.lottery.controller;

import com.jack.lottery.entity.User;
import com.jack.lottery.service.SMSService;
import com.jack.lottery.service.UserService;
import com.jack.lottery.utils.RandomUtils;
import com.jack.lottery.utils.exception.Exception2ResponseUtils;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.CommonResponose;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private UserService userService;

    @Value("${sms.content.templet}")
    private String smsContent;

    /**
     * 发送验证码短信
     * userId 用户编号
     * type = 0  文字验证码
     * type = 1  语音验证码
     * */
    @RequestMapping("/send")
    public CommonResponose<Boolean> sendMsg(@RequestParam long userId, @RequestParam int type) {
        try {
            User user = userService.getUserInfoById(userId);
            String mobile = user.getMobile();
            if (StringUtils.isBlank(mobile)) {
                throw new ParamException("用户手机号不存在,userId:"+userId);
            }
            String verificationCode = RandomUtils.getRandomNumber(4);
            boolean success = false;
            if (0 == type) {
                smsContent = String.format(smsContent, verificationCode);
                success = smsService.sendMsg("", smsContent);
            } else {
                success = smsService.sendVoiceMsg("", verificationCode);
            }
            return new CommonResponose(success);
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }
    }
}
