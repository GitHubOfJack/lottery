package com.jack.lottery.service;

import com.jack.lottery.entity.User;
import com.jack.lottery.entity.UserExample;
import com.jack.lottery.mapper.UserMapper;
import com.jack.lottery.utils.exception.DBException;
import com.jack.lottery.utils.exception.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

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
    public void registerUser(String mobile, String password, String code,
                             String smsCode, String nickName) throws DBException {
        Date now = new Date();
        User user = new User();
        user.setMobile(mobile);
        user.setCreateTime(now);
        user.setNickName(nickName);
        user.setPassword(password);
        user.setUpdateTime(now);
        userMapper.insertSelective(user);

    }
}
