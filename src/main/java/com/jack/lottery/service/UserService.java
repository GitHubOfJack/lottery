package com.jack.lottery.service;

import com.jack.lottery.entity.User;
import com.jack.lottery.mapper.UserMapper;
import com.jack.lottery.utils.exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new DBException("userId不存在:"+userId);
        }
        return user;
    }
}
