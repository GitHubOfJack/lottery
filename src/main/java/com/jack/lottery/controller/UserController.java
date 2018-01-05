package com.jack.lottery.controller;

import com.jack.lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 注册功能
     * mobile 手机号
     *
     * */
    private boolean register() {
        return false;
    }
}
