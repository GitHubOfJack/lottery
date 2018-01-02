package com.jack.lottery.controller;

import com.jack.lottery.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/t")
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/t")
    public String test() {
        testService.test();
        return "OK";
    }
}
