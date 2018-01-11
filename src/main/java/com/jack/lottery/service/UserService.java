package com.jack.lottery.service;

import com.jack.lottery.dao.SMSCodeDao;
import com.jack.lottery.dao.UserDao;
import com.jack.lottery.entity.SMSCode;
import com.jack.lottery.entity.SMSCodeExample;
import com.jack.lottery.entity.User;
import com.jack.lottery.entity.UserExample;
import com.jack.lottery.mapper.SMSCodeMapper;
import com.jack.lottery.mapper.UserMapper;
import com.jack.lottery.utils.MD5Util;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.DBException;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.LoginResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private SMSCodeDao smsCodeDao;

    /**
     * 根据用户ID查询用户信息
     * */
    public User getUserInfoById(long userId) throws DBException {
        return userDao.getUserById(userId);
    }

    /**
     * 根据用户手机号查询用户信息
     * */
    public User getUserInfoByMobile(String mobile) throws DBException {
        return userDao.getUserInfoByMobile(mobile);
    }

    /**
     * 根据用户昵称查询用户信息
     * */
    public User getUserInfoByNickName(String nickName) throws DBException {
        return userDao.getUserInfoByNickName(nickName);
    }

    /**
     * 用户注册
     * */
    public void registerUser(String mobile, String password, String smsCode, String nickName) throws BaseException {
        checkSmsCode(mobile, smsCode);
        Date now = new Date();
        User user = new User();
        user.setMobile(mobile);
        user.setCreateTime(now);
        user.setNickName(nickName);
        password = MD5Util.encode(password);
        user.setPassword(password);
        user.setUpdateTime(now);
        userDao.insertUser(user);
    }

    public void checkSmsCode(String mobile, String smsCode) throws ParamException {
        SMSCode latestSmsCode = smsCodeDao.getLatestSmsCode(mobile);
        if (StringUtils.isBlank(latestSmsCode.getCode()) || !smsCode.equals(latestSmsCode.getCode())) {
            throw new ParamException("验证码不正确");
        }
    }

    /**
     * 根据密码登录
     * */
    public LoginResponse loginByPwd(String mobile, String pwd) throws BaseException {
        User user = getUserInfoByMobile(mobile);
        String password = user.getPassword();
        pwd = MD5Util.encode(pwd);
        if (!password.equals(pwd)) {
            throw new ParamException("密码不正确");
        }
        String token = UUID.randomUUID().toString();
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        return resp;
    }

    /**
     * 根据手机验证码登录
     * */
    public LoginResponse loginByCode(String mobile, String code) throws BaseException {
        User user = getUserInfoByMobile(mobile);
        checkSmsCode(mobile, code);
        LoginResponse resp = new LoginResponse();
        resp.setToken(UUID.randomUUID().toString());
        resp.setUserId(user.getId());
        return resp;
    }

    /**
     * 修改登录密码
     * */
    public boolean changePwd(String mobile, String oldPwd, String newPwd) throws BaseException {
        User user = getUserInfoByMobile(mobile);
        String password = user.getPassword();
        oldPwd = MD5Util.encode(oldPwd);
        if (!password.equals(oldPwd)) {
            throw new ParamException("密码不正确");
        }
        newPwd = MD5Util.encode(newPwd);
        user.setPassword(newPwd);
        userDao.updateUserByUserId(user);
        return true;
    }

    /**
     * 重置登录密码
     * */
    public boolean resetPwd(String mobile, String pwd, String code) throws BaseException {
        User user = getUserInfoByMobile(mobile);
        checkSmsCode(mobile, code);
        pwd = MD5Util.encode(pwd);
        user.setPassword(pwd);
        userDao.updateUserByUserId(user);
        return true;
    }
}
