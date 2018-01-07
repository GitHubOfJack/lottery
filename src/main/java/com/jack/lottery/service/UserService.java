package com.jack.lottery.service;

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
    private UserMapper userMapper;

    @Autowired
    private SMSCodeMapper sMSCodeMapper;

    /**
     * 根据用户ID查询用户信息
     * */
    public User getUserInfoById(long userId) throws DBException {
        User user = userMapper.selectByPrimaryKey(userId);
        if (null == user) {
            throw new DBException("用户不存在,userId:"+userId);
        }
        return user;
    }

    /**
     * 根据用户手机号查询用户信息
     * */
    public User getUserInfoByMobile(String mobile) throws DBException {
        UserExample example = new UserExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<User> users = userMapper.selectByExample(example);
        if (null == users || users.isEmpty()) {
            throw new DBException("用户不存在,mobile:"+mobile);
        }
        return users.get(0);
    }

    /**
     * 根据用户昵称查询用户信息
     * */
    public User getUserInfoByNickName(String nickName) throws DBException {
        UserExample example = new UserExample();
        example.createCriteria().andNickNameEqualTo(nickName);
        List<User> users = userMapper.selectByExample(example);
        if (null == users || users.isEmpty()) {
            throw new DBException("用户不存在,nickName:"+nickName);
        }
        return users.get(0);
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
        try {
            userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            throw new ParamException("用户已存在");
        } catch (Exception e) {
            throw new DBException("数据库异常", e);
        }
    }

    private void checkSmsCode(String mobile, String smsCode) throws ParamException {
        SMSCodeExample example = new SMSCodeExample();
        example.setOrderByClause(" id desc ");
        example.createCriteria().andMobileEqualTo(mobile);
        List<SMSCode> smsCodes = sMSCodeMapper.selectByExample(example);
        if (null == smsCodes || smsCodes.isEmpty() || StringUtils.isBlank(smsCodes.get(0).getCode()) || !smsCode.equals(smsCodes.get(0).getCode())) {
            throw new ParamException("短信验证码不正确");
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
        LoginResponse resp = new LoginResponse();
        resp.setToken(UUID.randomUUID().toString());
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
        userMapper.updateByPrimaryKeySelective(user);
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
        userMapper.updateByPrimaryKeySelective(user);
        return true;
    }
}
