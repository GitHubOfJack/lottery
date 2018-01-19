package com.jack.lottery.service;

import com.jack.lottery.dao.AccountDao;
import com.jack.lottery.dao.CardInfoDao;
import com.jack.lottery.dao.SMSCodeDao;
import com.jack.lottery.dao.UserDao;
import com.jack.lottery.entity.*;
import com.jack.lottery.utils.LotteryStringUtil;
import com.jack.lottery.utils.MD5Util;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.DBException;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.LoginResponse;
import com.jack.lottery.vo.QueryUserBasicInfoResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private UserOptService userOptService;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CardInfoDao cardInfoDao;

    @Value("${user.profile.img.path}")
    private String imgPath;

    /**
     * 根据用户ID查询用户信息
     * */
    public User getUserInfoById(long userId) throws DBException {
        return userDao.getUserById(userId);
    }

    /**
     * 验证手机号是否存在
     * */
    public boolean mobileExist(String mobile) {
        return userDao.mobileExist(mobile);
    }

    /**
     * 根据用户手机号查询用户信息
     * */
    public User getUserInfoByMobile(String mobile) throws DBException {
        return userDao.getUserInfoByMobile(mobile);
    }

    /**
     * 验证用户昵称是否存在
     * */
    public boolean nickNameExist(String nickName) {
        return userDao.nickNameExist(nickName);
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
        User user = createUser(mobile, password, nickName);
        Account account = createAccount();
        userOptService.doRegister(user, account);
    }

    private User createUser(String mobile, String password, String nickName) {
        Date now = new Date();
        User user = new User();
        user.setMobile(mobile);
        user.setCreateTime(now);
        user.setNickName(nickName);
        password = MD5Util.encode(password);
        user.setPassword(password);
        user.setUpdateTime(now);
        return user;
    }

    private Account createAccount() {
        Account account = new Account();
        Date now = new Date();
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setFreezeBalance(BigDecimal.ZERO);
        account.setBalance(BigDecimal.ZERO);
        account.setCreateTime(now);
        account.setUpdateTime(now);
        return account;
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
    public boolean changePwd(long userId, String oldPwd, String newPwd) throws BaseException {
        User user = getUserInfoById(userId);
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
        checkSmsCode(user.getMobile(), code);
        pwd = MD5Util.encode(pwd);
        user.setPassword(pwd);
        userDao.updateUserByUserId(user);
        return true;
    }

    /**
     * 查询用户基本信息
     * */
    public QueryUserBasicInfoResp getUserBasicInfo(long userId) throws BaseException {
        User userInfo = getUserInfoById(userId);
        Account account = accountDao.getAccountByUserId(userId);
        QueryUserBasicInfoResp resp = new QueryUserBasicInfoResp();
        resp.setBalance(account.getBalance());
        resp.setImgUrl(userInfo.getImgUrl());
        resp.setMobile(userInfo.getMobile());
        resp.setNickName(userInfo.getNickName());
        BigDecimal winPrize  = null == account.getWinPrize() ? BigDecimal.ZERO : account.getWinPrize();
        resp.setWinPrize(winPrize);
        resp.setMobile(userInfo.getMobile());
        resp.setIdNo(userInfo.getIdNo());
        resp.setRealName(userInfo.getRealName());
        Long cardId = userInfo.getCardId();
        if (null == cardId || 0 >= cardId) {
            resp.setBind(false);
            resp.setBranch(null);
            resp.setCard(null);
        } else {
            CardInfo cardInfo = cardInfoDao.getCardInfoById(cardId);
            resp.setBind(true);
            resp.setBranch(cardInfo.getBankBranch());
            resp.setCard(cardInfo.getCardNo());
        }
        resp.setCetification(StringUtils.isBlank(userInfo.getRealName()) ? false : true);
        Double winPercent = null == userInfo.getWinPercent() ? 0.0 : userInfo.getWinPercent();
        winPercent *= 100;
        resp.setWinPercent(winPercent+"%");
        return resp;
    }

    public boolean identify(long userId, String realName, String idType, String idNo) {
        User user = new User();
        user.setUpdateTime(new Date());
        user.setIdNo(idNo);
        user.setIdType(idType);
        user.setRealName(realName);
        user.setId(userId);
        userDao.updateUserByUserId(user);
        return true;
    }

    public boolean bindCard(long userId, String cardNo, String branch,
                            String province, String city, String bankName,
                            String name) {
        CardInfo cardInfo = createCardInfo(cardNo, branch, province, city, bankName);
        User user = new User();
        user.setId(userId);
        long id = null == cardInfoDao.getCardInfoByCardNo(cardNo) ? -1 : cardInfoDao.getCardInfoByCardNo(cardNo).getId();
        userOptService.doBindCard(user, cardInfo, id);
        return true;
    }

    private CardInfo createCardInfo(String cardNo, String branch,
                           String province, String city, String bankName) {
        CardInfo info = new CardInfo();
        info.setBankBranch(branch);
        info.setBankName(bankName);
        info.setCardNo(cardNo);
        info.setCity(city);
        info.setProvince(province);
        return info;
    }

    public boolean updateUserImg(long userId, String img) {
        String imgAddr = imgPath + userId;
        LotteryStringUtil.generateImage(img, imgAddr);
        User user = new User();
        user.setId(userId);
        user.setImgUrl(imgAddr);
        userDao.updateUserByUserId(user);
        return true;
    }
}
