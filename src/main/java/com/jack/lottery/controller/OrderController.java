package com.jack.lottery.controller;

import com.jack.lottery.service.OrderService;
import com.jack.lottery.utils.exception.Exception2ResponseUtils;
import com.jack.lottery.vo.CommonResponose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 账户充值
     * @param userId 用户编号
     * @param amount 金额
     * @param type 充值类型
     * */
    @RequestMapping("/recharge")
    public CommonResponose<Boolean> recharge(@RequestParam long userId, @RequestParam BigDecimal amount,
                                             @RequestParam String type) {
        try {
            boolean success = orderService.recharge(userId, amount, type);
            return new CommonResponose<>(success);
        } catch (Exception e) {
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
     * 根据type不一致，投注内容不一样
     * type=1 双色球   01^02^03^04^05^06^07|01^02
     * type=2 大乐透   01^02^03^04^05^06^07|01^02
     * type=3 排列三   0^1|0^1|0^1
     * type=4 排列五   0^1|0^1|0^1|0^1|0^1
     * type=5 福彩3D   0^1|0^1|0^1
     * */
    @RequestMapping("/buy")
    public CommonResponose<Boolean> buyLottery(@RequestParam long userId, @RequestParam String type,
                                               @RequestParam String content, @RequestParam BigDecimal amount,
                                               @RequestParam int num) {
        try {
            boolean success = orderService.buyLottery(userId, type, content, amount, num);
            return new CommonResponose<>(success);
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }
    }
}
