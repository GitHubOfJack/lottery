package com.jack.lottery.controller;

import com.jack.lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取验证码接口
     * */
    @RequestMapping("/verificationCode")
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession();

        ValidateCode vCode = new ValidateCode(120,40,5,100);
        session.setAttribute("code", vCode.getCode());
        vCode.write(response.getOutputStream());
    }

    /**
     * 注册功能
     * mobile 手机号
     * password 密码
     * code  验证码
     * smsCode  短信验证码
     * nickName 昵称
     * */
    @RequestMapping("/register")
    public boolean register(@RequestParam String mobile, @RequestParam String password,
                            @RequestParam String code, @RequestParam String smsCode,
                            @RequestParam String nickName) {

    }
}
