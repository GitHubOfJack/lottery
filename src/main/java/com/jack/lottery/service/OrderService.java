package com.jack.lottery.service;

import com.jack.lottery.buss.LotteryBuss;
import com.jack.lottery.dao.AccountDao;
import com.jack.lottery.dao.LotteryOrderDao;
import com.jack.lottery.entity.Account;
import com.jack.lottery.entity.AccountDetail;
import com.jack.lottery.entity.LotteryOrder;
import com.jack.lottery.entity.LotteryTerm;
import com.jack.lottery.enums.LotteryOrderStatus;
import com.jack.lottery.enums.OrderDetailType;
import com.jack.lottery.enums.SaleStatus;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.vo.QueryOrderResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryBuss lotteryBuss;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private OrderOptService orderOptService;

    @Autowired
    private LotteryOrderDao lotteryOrderDao;

    public boolean buyLottery(long userId, String type, String content,
                              BigDecimal amount, int num) throws BaseException {
        //类型&&彩种是否在可售时间内是否正确
        LotteryTerm currentTerm = lotteryService.getCurrentTerm(Integer.parseInt(type));
        if (!currentTerm.getStatus().equals(String.valueOf(SaleStatus.SALE.getCode())) || currentTerm.getEndtime().compareTo(new Date()) < 0) {
            throw new ParamException("当前期已停止");
        }
        //内容&&金额是否正确
        lotteryBuss.checkContent(type, content, num, amount);
        //验证用户余额
        Account account = accountDao.getAccountByUserId(userId);
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new ParamException("用户余额不足");
        }
        //生成订单
        LotteryOrder order = createOrder(userId, type, content, amount, num, currentTerm.getTerm());
        account.setFreezeBalance(account.getFreezeBalance().add(amount));
        account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
        AccountDetail accountDetail = createAccountDetail(userId, amount, account.getId());
        orderOptService.buyOrder(order, account, accountDetail);
        return true;
    }

    private LotteryOrder createOrder(long uerId, String type, String content, BigDecimal amount, int num, String term) {
        Date now = new Date();
        LotteryOrder order = new LotteryOrder();
        order.setAmount(amount);
        order.setCreateTime(now);
        order.setLotteryContent(content);
        order.setLotteryTerm(term);
        order.setLotteryType(type);
        order.setNum(num);
        order.setStatus(LotteryOrderStatus.SUBMIT.getCode());
        order.setUpdateTime(now);
        order.setUserid(uerId);
        return order;
    }

    private AccountDetail createAccountDetail(long uerId, BigDecimal amount, long accountId) {
        Date now = new Date();
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAmount(amount);
        accountDetail.setCreateTime(now);
        accountDetail.setDesc("用户购买冻结,冻结金额:"+amount);
        accountDetail.setType(OrderDetailType.BUY_FREEZE.getCode());
        accountDetail.setUpdateTime(now);
        accountDetail.setUserid(uerId);
        accountDetail.setAccountid(accountId);
        return accountDetail;
    }

    public int countByUserIdAndStatus(long userId, String status) {
        if (!StringUtils.isBlank(status)) {
            try {
                LotteryOrderStatus orderStatus = LotteryOrderStatus.getByCode(status);
            } catch (ParamException e) {
                status = "";
            }
        }
        return lotteryOrderDao.getOrderCountByUserIdAndStatus(userId, status);
    }

    public List<LotteryOrder> getOrderListByUserIdAndStatus(long userId, String status, int pageNo, int pageSize) {
        return lotteryOrderDao.getOrdersByUserIdAndStatus(userId, status, pageNo, pageSize);
    }

    public QueryOrderResp queryOrder(long userId, String status, int pageNo, int pageSize) {
        if (!StringUtils.isBlank(status)) {
            try {
                LotteryOrderStatus orderStatus = LotteryOrderStatus.getByCode(status);
            } catch (ParamException e) {
                status = "";
            }
        }
        if (0 >= pageNo) {
            pageNo = 1;
        }
        if (0 >= pageSize) {
            pageSize = 10;
        }
        int count = countByUserIdAndStatus(userId, status);
        int page = count % pageSize != 0 ? count / pageSize + 1 : count / pageSize;
        QueryOrderResp resp = new QueryOrderResp();
        resp.setTotalPage(page);
        resp.setTotal(count);
        resp.setOrders(getOrderListByUserIdAndStatus(userId, status, pageNo, pageSize));
        return resp;
    }
}
