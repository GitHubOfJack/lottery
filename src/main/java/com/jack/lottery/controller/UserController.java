package com.jack.lottery.controller;

import com.jack.lottery.entity.User;
import com.jack.lottery.service.UserService;
import com.jack.lottery.utils.VerificationCode;
import com.jack.lottery.utils.exception.Exception2ResponseUtils;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.CommonResponose;
import com.jack.lottery.vo.LoginResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

        VerificationCode vCode = new VerificationCode(120,40,4,100);
        session.setAttribute("code", vCode.getCode());
        try {
            vCode.write(response.getOutputStream());
        } catch (IOException e) {
            logger.error("生成验证码图片错误", e);
        }
    }

    /**
     * 根据手机号查询用户
     * @param mobile 手机号
     * */
    @RequestMapping("/getUserByMobile")
    public CommonResponose<User> getUserByMobile(@RequestParam String mobile) {
        try {
            if (StringUtils.isBlank(mobile)) {
                throw new ParamException("手机号为空");
            }
            return new CommonResponose<>(userService.getUserInfoByMobile(mobile));
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }

    }

    /**
     * 根据昵称查询用户
     * @param nickName 用户昵称
     * */
    @RequestMapping("/getUserByNickName")
    public CommonResponose<User> getUserByNickName(@RequestParam String nickName) {
        try {
            if (StringUtils.isBlank(nickName)) {
                throw new ParamException("用户昵称为空");
            }
            return new CommonResponose<>(userService.getUserInfoByNickName(nickName));
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }

    }

    /**
     * 注册功能
     * @param mobile 手机号
     * @param password 密码
     * @param code  验证码
     * @param smsCode  短信验证码
     * @param nickName 昵称
     * */
    @RequestMapping("/register")
    public CommonResponose<Boolean> register(@RequestParam String mobile, @RequestParam String password,
                                             @RequestParam String code, @RequestParam String smsCode,
                                             @RequestParam String nickName, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            String sessionCode = (String) session.getAttribute("code");
            if (StringUtils.isBlank(sessionCode) || StringUtils.isBlank(code) ||
                    !sessionCode.toLowerCase().equals(code.toLowerCase())) {
                throw new ParamException("验证码错误");
            }
            userService.registerUser(mobile, password, smsCode, nickName);
            return  new CommonResponose<>(true);
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    /**
     * 登录功能--支持密码登录和验证码登录
     * @param mobile 手机号
     * @param password 用户密码
     * @param smsCode 手机验证码
     * */
    @RequestMapping("/login")
    public CommonResponose<LoginResponse> login(@RequestParam String mobile, @RequestParam(required = false) String password,
                                                @RequestParam(required = false) String smsCode, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(mobile)) {
                throw new ParamException("手机号为空");
            }
            HttpSession session = request.getSession();
            LoginResponse resp = null;
            if (StringUtils.isNoneBlank(password)) {
                resp = userService.loginByPwd(mobile, password);
                session.setAttribute("loginToken", resp.getToken());
            } else if (StringUtils.isNoneBlank(smsCode)) {
                resp = userService.loginByCode(mobile, smsCode);
                session.setAttribute("loginToken", resp.getToken());
            } else {
                throw new ParamException("请选择一种登录方式");
            }
            return new CommonResponose<>(resp);
        } catch (Exception e){
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    /**
     * 修改密码
     * @param mobile 手机号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * */
    @RequestMapping("/changePwd")
    public CommonResponose<Boolean> changePwd(@RequestParam String mobile, @RequestParam String oldPwd,
                                              @RequestParam String newPwd) {
        try {
            boolean success = userService.changePwd(mobile, oldPwd, newPwd);
            return new CommonResponose<>(success);
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    /**
     * 重置密码
     * @param mobile 手机号
     * @param pwd 新密码
     * @param code 手机验证码
     * */
    @RequestMapping("/resetPwd")
    public CommonResponose<Boolean> resetPwd(@RequestParam String mobile, @RequestParam String pwd,
                                             @RequestParam String code) {
        try {
            boolean success = userService.resetPwd(mobile, pwd, code);
            return new CommonResponose<>(success);
        } catch (Exception e) {
            return Exception2ResponseUtils.getResponse(e);
        }
    }
}
