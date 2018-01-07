package com.jack.lottery.controller;

import com.jack.lottery.entity.User;
import com.jack.lottery.service.SMSService;
import com.jack.lottery.service.UserService;
import com.jack.lottery.utils.exception.Exception2ResponseUtils;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.CommonResponose;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 发送验证码短信
     * @param userId 用户编号
     * @param type = 0  文字验证码   type = 1  语音验证码
     * */
    @RequestMapping("/send")
    public CommonResponose<Boolean> sendMsg(@RequestParam long userId, @RequestParam int type) {
        try {
            validateSendMsg(userId, type);
            User user = userService.getUserInfoById(userId);
            String mobile = user.getMobile();
            if (StringUtils.isBlank(mobile)) {
                throw new ParamException("用户手机号不存在,userId:"+userId);
            }
            return new CommonResponose(smsService.send(mobile, type));
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void validateSendMsg(long userId, int type) throws ParamException {
        if (0 >= userId) {
            throw new ParamException("userId不存在");
        }
        if (0 != type && 1 != type) {
            throw new ParamException("type不正确");
        }
    }
}
