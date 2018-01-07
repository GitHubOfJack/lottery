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
     * */
    @RequestMapping("/buy")
    public void buyLottery(@RequestParam long userId, @RequestParam String type,
                           @RequestParam String content, @RequestParam BigDecimal amount) {

    }
}
