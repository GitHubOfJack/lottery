package com.jack.lottery.dao;

import com.jack.lottery.entity.User;
import com.jack.lottery.entity.UserExample;
import com.jack.lottery.mapper.UserMapper;
import com.jack.lottery.utils.exception.DBException;
import com.jack.lottery.utils.exception.ParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(long userId) throws DBException {
        User user = userMapper.selectByPrimaryKey(userId);
        if (null == user) {
            throw new DBException("用户不存在,userId:"+userId);
        }
        return user;
    }

    public User getUserInfoByMobile(String mobile) throws DBException {
        UserExample example = new UserExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<User> users = userMapper.selectByExample(example);
        if (null == users || users.isEmpty()) {
            throw new DBException("用户不存在,mobile:"+mobile);
        }
        return users.get(0);
    }

    public User getUserInfoByNickName(String nickName) throws DBException {
        UserExample example = new UserExample();
        example.createCriteria().andNickNameEqualTo(nickName);
        List<User> users = userMapper.selectByExample(example);
        if (null == users || users.isEmpty()) {
            throw new DBException("用户不存在,nickName:"+nickName);
        }
        return users.get(0);
    }

    public void insertUser(User user) throws ParamException, DBException {
        try {
            userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            throw new ParamException("用户已存在");
        } catch (Exception e) {
            throw new DBException("数据库异常", e);
        }
    }

    public void updateUserByUserId(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }
}
