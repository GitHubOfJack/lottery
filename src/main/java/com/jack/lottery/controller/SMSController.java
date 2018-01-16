package com.jack.lottery.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jack.lottery.entity.User;
import com.jack.lottery.service.SMSService;
import com.jack.lottery.service.UserService;
import com.jack.lottery.utils.LotteryStringUtil;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.Exception2ResponseUtils;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.utils.exception.SystermException;
import com.jack.lottery.vo.CommonResponose;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired
    private SMSService smsService;

    @Autowired
    private UserService userService;

    private LoadingCache<String, Integer> mobileCount = CacheBuilder.newBuilder()
            .refreshAfterWrite(5, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String s) throws Exception {
                    return 1;
                }
            });

    /**
     * 发送验证码短信
     * @param mobile 手机号
     * @param type = 0  文字验证码   type = 1  语音验证码
     * @param checkMobile 是否需要验证手机号是否存在
     * */
    @RequestMapping("/send")
    public CommonResponose<Boolean> sendMsg(@RequestParam String mobile, @RequestParam int type,
                                            @RequestParam boolean checkMobile) {
        try {
            validateSendMsg(mobile, type, checkMobile);
            return new CommonResponose(smsService.send(mobile, type));
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void validateSendMsg(String mobile, int type, boolean checkMobile) throws BaseException {
        if (StringUtils.isBlank(mobile)) {
            throw new ParamException("用户手机号不存在|mobile:"+mobile);
        }
        if (!LotteryStringUtil.isMobile(mobile)) {
            throw new ParamException("手机号格式不正确|mobile:"+mobile);
        }
        if (checkMobile) {
            userService.getUserInfoByMobile(mobile);
        }
        if (0 != type && 1 != type) {
            throw new ParamException("type不正确|type:"+type);
        }
        try {
            int num = mobileCount.get(mobile).intValue();
            if (num <= 5) {
                mobileCount.put(mobile, num++);
            } else {
                throw new SystermException("请5分钟后重试");
            }
        } catch (ExecutionException e) {
            throw new SystermException("系统异常|手机验证码次数信息取值错误");
        }
    }
}
