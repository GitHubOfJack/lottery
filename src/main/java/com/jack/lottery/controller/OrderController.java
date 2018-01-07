package com.jack.lottery.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 购买彩票
     * @param userId 用户编号
     * @param type 彩种类型
     * @param content 投注内容
     * @param amount 投注金额
     * 根据type不一致，投注内容不一样
     * type=1 双色球   01^02^03^04^05^06^07|01^02
     * type=2 大乐透   01^02^03^04^05^06^07|01^02
     * type=3 排列三   0^1|0^1|0^1
     * type=4 排列五   0^1|0^1|0^1|0^1|0^1
     * type=5 福彩3D   0^1|0^1|0^1
     * */
    @RequestMapping("/buy")
    public void buyLottery(@RequestParam long userId, @RequestParam String type,
                           @RequestParam String content, @RequestParam BigDecimal amount) {
        //用户是否存在
        //类型是否正确
        //内容是否正确
        //金额是否正确
        //彩种是否在可售时间内
        //生成订单
        //扣除用户金额
        //修改订单状态
        //往出票商发送信息
        //更新订单状态
    }
}
