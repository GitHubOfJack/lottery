package com.jack.lottery.controller;

import com.jack.lottery.entity.User;
import com.jack.lottery.enums.IdType;
import com.jack.lottery.service.OrderService;
import com.jack.lottery.service.UserService;
import com.jack.lottery.utils.LotteryStringUtil;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.Exception2ResponseUtils;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.CommonResponose;
import com.jack.lottery.vo.LoginResponse;
import com.jack.lottery.vo.QueryOrderResp;
import com.jack.lottery.vo.QueryUserBasicInfoResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * 根据手机号查询用户是否存在
     * @param mobile 手机号
     * */
    @RequestMapping("/mobileExist")
    public CommonResponose<Boolean> mobileExist(String mobile) {
        try {
            if (StringUtils.isBlank(mobile)) {
                throw new ParamException("手机号为空|mobile="+mobile);
            }
            return new CommonResponose<>(!userService.mobileExist(mobile));
        } catch (Exception e) {
            logger.error("调用手机号查询用户是否存在报错,请求参数:{}", mobile, e);
            return Exception2ResponseUtils.getResponse(e);
        }

    }

    /**
     * 根据昵称查询用户是否存在
     * @param nickName 用户昵称
     * */
    @RequestMapping("/nickNameExist")
    public CommonResponose<Boolean> nickNameExist(String nickName) {
        try {
            if (StringUtils.isBlank(nickName)) {
                throw new ParamException("用户昵称为空|nickName="+nickName);
            }
            return new CommonResponose<>(!userService.nickNameExist(nickName));
        } catch (Exception e) {
            logger.error("调用昵称查询用户是否存在报错,请求参数:{}", nickName, e);
            return Exception2ResponseUtils.getResponse(e);
        }

    }

    /**
     * 注册功能
     * @param mobile 手机号
     * @param password 密码
     * @param smsCode  短信验证码
     * @param nickName 昵称
     * */
    @RequestMapping("/register")
    public CommonResponose<LoginResponse> register(String mobile, String password,
                                             String smsCode, String nickName, HttpServletRequest request) {
        try {
            checkRegisterParam(mobile, password, smsCode, nickName);
            userService.registerUser(mobile, password, smsCode, nickName);
            HttpSession session = request.getSession();
            LoginResponse resp = userService.loginByPwd(mobile, password);
            session.setAttribute("loginToken", resp.getToken());
            return  new CommonResponose<>(resp);
        } catch (Exception e) {
            logger.error("调用注册接口报错,请求参数:{},{},{},{}", mobile, password, smsCode, nickName, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void checkRegisterParam(String mobile, String password,
                                    String smsCode, String nickName) throws ParamException {
        if (StringUtils.isBlank(mobile)) {
            throw new ParamException("手机号为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new ParamException("密码为空");
        }
        if (!LotteryStringUtil.validatePwd(password)) {
            throw new ParamException("密码格式不正确");
        }
        if (StringUtils.isBlank(smsCode)) {
            throw new ParamException("手机验证码为空");
        }
        if (StringUtils.isBlank(nickName)) {
            throw new ParamException("用户昵称为空");
        }
        if (!LotteryStringUtil.isMobile(mobile)) {
            throw new ParamException("手机号格式不正确");
        }
    }

    /**
     * 登录功能--支持密码登录和验证码登录
     * @param mobile 手机号
     * @param password 用户密码
     * @param smsCode 手机验证码
     * */
    @RequestMapping("/login")
    public CommonResponose<LoginResponse> login(String mobile,String password,
                                                String smsCode, HttpServletRequest request) {
        try {
            checkLoginParam(mobile, password, smsCode);
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
            logger.error("调用登录接口报错,入参:{},{},{}", mobile, password, smsCode, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void checkLoginParam(String mobile, String password, String smsCode) throws ParamException {
        if (StringUtils.isBlank(mobile)) {
            throw new ParamException("手机号为空");
        }
        if (StringUtils.isBlank(password) && StringUtils.isBlank(smsCode)) {
            throw new ParamException("密码和验证码不能同时为空");
        }
        if (!LotteryStringUtil.isMobile(mobile)) {
            throw new ParamException("手机号格式不正确");
        }
    }

    /**
     * 修改密码
     * @param userId 用户编号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * */
    @RequestMapping("/changePwd")
    public CommonResponose<Boolean> changePwd(long userId, String oldPwd, String newPwd) {
        try {
            checkChangePwdParam(userId, oldPwd, newPwd);
            boolean success = userService.changePwd(userId, oldPwd, newPwd);
            return new CommonResponose<>(success);
        } catch (Exception e) {
            logger.error("修改密码接口报错,入参:{},{},{}", userId, oldPwd, newPwd, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void checkChangePwdParam(long userId, String oldPwd, String newPwd) throws ParamException {
        if (0 >= userId) {
            throw new ParamException("用户编号不存在");
        }
        if (StringUtils.isBlank(oldPwd)) {
            throw new ParamException("旧密码为空");
        }
        if (StringUtils.isBlank(newPwd)) {
            throw new ParamException("新密码为空");
        }
        if (!LotteryStringUtil.validatePwd(newPwd)) {
            throw new ParamException("新密码格式不正确");
        }
    }

    /**
     * 重置密码
     * @param mobile 用户手机号
     * @param pwd 新密码
     * @param code 手机验证码
     * */
    @RequestMapping("/resetPwd")
    public CommonResponose<Boolean> resetPwd(String mobile, String pwd, String code) {
        try {
            checkResetPwdParam(mobile, pwd, code);
            boolean success = userService.resetPwd(mobile, pwd, code);
            return new CommonResponose<>(success);
        } catch (Exception e) {
            logger.error("重置密码接口报错,入参:{},{},{}", mobile, pwd, code, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void checkResetPwdParam(String mobile, String pwd, String code) throws ParamException {
        if (StringUtils.isBlank(mobile)) {
            throw new ParamException("手机号不存在");
        }
        if (StringUtils.isBlank(pwd)) {
            throw new ParamException("新密码不存在");
        }
        if (!LotteryStringUtil.validatePwd(pwd)) {
            throw new ParamException("新密码格式不正确");
        }
        if (StringUtils.isBlank(code)) {
            throw new ParamException("手机号验证码不存在");
        }
        if (!LotteryStringUtil.isMobile(mobile)) {
            throw new ParamException("手机号格式不正确");
        }
    }

    /**
     * 查询用户所有订单接口
     * @param userId 用户编号
     * @see com.jack.lottery.enums.LotteryOrderStatus
     * @param type 查询类型
     * @param pageNo 页码
     * @param pageSize 每页大小
     * */
    @RequestMapping("/queryOrder")
    public CommonResponose<QueryOrderResp> queryOrder(long userId, String type, int pageNo, int pageSize) {
        try {
            QueryOrderResp resp = orderService.queryOrder(userId, type, pageNo, pageSize);
            return new CommonResponose<>(resp);
        } catch (Exception e) {
            logger.error("查询用户订单接口报错,入参:{},{}", userId, type, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    /**
     * 用户基本信息查询
     * @param userId 用户编号
     * */
    @RequestMapping("/queryUserBasicInfo")
    public CommonResponose<QueryUserBasicInfoResp> queryUserBasicInfo(long userId) {
        try {
            if (0 >= userId) {
                throw new ParamException("用户编号不存在");
            }
            return new CommonResponose<>(userService.getUserBasicInfo(userId));
        } catch (Exception e) {
            logger.error("查询用户基本信息接口报错,入参:{}",userId, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    /**
     * 实名接口
     * */
    @RequestMapping("identify")
    public CommonResponose<Boolean> identify(long userId, String realName, String idType, String idNo) {
        try {
            checkIdentifyParam(userId, realName, idType, idNo);
            return new CommonResponose<>(userService.identify(userId, realName, idType, idNo));
        } catch (Exception e) {
            logger.error("实名接口报错,入参:{},{},{},{}",userId, realName, idType, idNo, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void checkIdentifyParam(long userId, String realName, String idType,
                                    String idNo) throws ParamException {
        if (0 >= userId) {
            throw new ParamException("用户编号不存在");
        }
        if (StringUtils.isBlank(realName)) {
            throw new ParamException("真实姓名为空");
        }
        if (StringUtils.isBlank(idType)) {
            throw new ParamException("证件类型为空");
        }
        IdType.getTypeByCode(idType);
        if (StringUtils.isBlank(idNo)) {
            throw new ParamException("证件号码为空");
        }
        if (!LotteryStringUtil.validateIdNo(idNo)) {
            throw new ParamException("证件号码格式错误");
        }
    }

    /**
     * 绑卡接口
     * */
    @RequestMapping("bindCard")
    public CommonResponose<Boolean> bindCard(long userId, String cardNo, String branch,
                                             String province, String city, String bankName,
                                             String name) {
        try {
            checkBindCardParam(userId, cardNo, branch, province, city, bankName, name);
            return new CommonResponose<>(userService.bindCard(userId, cardNo, branch,
                    province, city, bankName, name));
        } catch (Exception e) {
            logger.error("实名接口报错,入参:{},{},{},{}",userId, cardNo, branch, e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }

    private void checkBindCardParam(long userId, String cardNo, String branch,
                                    String province, String city, String bankName,
                                    String name) throws BaseException {
        if (0 >= userId) {
            throw new ParamException("用户编号不存在");
        }
        if (StringUtils.isBlank(cardNo)) {
            throw new ParamException("银行卡号不存在");
        }
        if (StringUtils.isBlank(branch)) {
            throw new ParamException("银行支行不存在");
        }
        if (StringUtils.isBlank(province)) {
            throw new ParamException("省份不能为空");
        }
        if (StringUtils.isBlank(city)) {
            throw new ParamException("市不能为空");
        }
        if (StringUtils.isBlank(bankName)) {
            throw new ParamException("银行不能为空");
        }
        if (StringUtils.isBlank(name)) {
            throw new ParamException("用户姓名为空");
        }
        User user = userService.getUserInfoById(userId);
        if (!user.getRealName().equals(name)) {
            throw new ParamException("银行卡实名信息必须与身份证实名信息一致");
        }
    }

    /**
     * 上传用户头像
     * */
    @RequestMapping("/uploadImg")
    public CommonResponose<Boolean> uploadImg(long userId, String img) {
        try {
            if (!img.contains("data:image/jpeg")) {
                throw new ParamException("文件格式不正确");
            }
            return new CommonResponose<>(userService.updateUserImg(userId, img));
        } catch (Exception e) {
            logger.error("头像上传接口报错", e);
            return Exception2ResponseUtils.getResponse(e);
        }
    }
}
