package com.jack.lottery.controller;

import com.jack.lottery.service.OrderService;
import com.jack.lottery.utils.exception.Exception2ResponseUtils;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.CommonResponose;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;

    /**
     * 账户充值
     * @param userId 用户编号
     * @param amount 金额
     * @param type 充值类型
     * */
    @RequestMapping("/recharge")
    public CommonResponose<Boolean> recharge(long userId, BigDecimal amount, String type) {
        try {
            boolean success = orderService.recharge(userId, amount, type);
            return new CommonResponose<>(success);
        } catch (Exception e) {
            logger.error("账户充值接口报错,请求参数:{},{},{}", userId, amount, type, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    /**
     * 购买彩票
     * @param userId 用户编号
     * @see com.jack.lottery.enums.LotteryType
     * @param type 彩种类型
     * @param content 投注内容
     * @param amount 投注金额
     * @param num 投注注数
     * @param muti 倍投
     * @param addition 追号
     * @param stopAfterWin 中奖后追号是否停止
     * 根据type不一致，投注内容不一样
     * type=1 双色球   01^02^03^04^05^06^07|01^02
     * type=2 大乐透   01^02^03^04^05^06^07|01^02
     * type=3 排列三   0^1|0^1|0^1
     * type=4 排列五   0^1|0^1|0^1|0^1|0^1
     * type=5 福彩3D   0^1|0^1|0^1
     * */
    @RequestMapping("/buy")
    public CommonResponose<Boolean> buyLottery(long userId, String type, String content,
                                               BigDecimal amount, int num, int muti,
                                               int addition, boolean stopAfterWin) {
        try {
            checkBuyLottery(userId, type, content, amount, num);
            boolean success = orderService.buyLottery(userId, type, content, amount, num, muti, addition, stopAfterWin);
            return new CommonResponose<>(success);
        } catch (Exception e) {
            logger.error("购彩接口报错,请求参数");
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void checkBuyLottery(long userId, String type, String content,
                                 BigDecimal amount, int num) throws ParamException {
        if (0 >= userId) {
            throw new ParamException("用户编号不存在");
        }
        if (StringUtils.isBlank(type)) {
            throw new ParamException("彩票类型不存在");
        }
        if (StringUtils.isBlank(content)) {
            throw new ParamException("购彩内容不存在");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamException("金额不正确");
        }
        if (0 >= num) {
            throw new ParamException("投注注数不正确");
        }
    }
}
