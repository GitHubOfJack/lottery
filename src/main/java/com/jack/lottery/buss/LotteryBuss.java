package com.jack.lottery.buss;

import com.jack.lottery.enums.LotteryType;
import com.jack.lottery.utils.exception.BaseException;
import com.jack.lottery.utils.exception.ParamException;
import com.jack.lottery.utils.exception.SystermException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LotteryBuss {
    /**
     * 验证投注内容
     * type=1 双色球   01^02^03^04^05^06^07|01^02
     * type=2 大乐透   01^02^03^04^05^06^07|01^02
     * type=3 排列三   0^1|0^1|0^1
     * type=4 排列五   0^1|0^1|0^1|0^1|0^1
     * type=5 福彩3D   0^1|0^1|0^1
     *
     */
    public void checkContent(String type, String content, int num, BigDecimal amt, int muti) throws BaseException {
        LotteryType lotteryType = LotteryType.getTypeByCode(type);
        if (StringUtils.isBlank(content)) {
            throw new ParamException("投注格式不正确");
        }
        if (0 >= num) {
            throw new ParamException("总注数不正确");
        }
        if (BigDecimal.valueOf(2).compareTo(amt) > 0) {
            throw new ParamException("金额不正确");
        }
        if (0 >= muti) {
            muti = 1;
        }
        if (BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(num))
                .multiply(BigDecimal.valueOf(muti)).compareTo(amt) != 0) {
            throw new ParamException("总注数和金额不匹配");
        }
        switch (lotteryType) {
            case SSQ:
                checkSSQ(content, num);
                break;
            case DLT:
                checkDLT(content, num);
                break;
            case PLS:
                checkPLS(content, num);
                break;
            case PLW:
                checkPLW(content, num);
                break;
            case SD:
                checkSD(content, num);
                break;
        }
    }

    //6个红球最大33，最小01，1个蓝球最大16，最小01
    private void checkSSQ(String content, int num) throws ParamException, SystermException {
        String[] contents = content.split("\\|");
        if (contents.length != 2) {
            throw new ParamException("投注格式不正确");
        }
        String red = contents[0];
        String blue = contents[1];
        if (StringUtils.isBlank(red) || StringUtils.isBlank(blue)) {
            throw new ParamException("投注格式不正确");
        }
        String[] reds = red.split("\\^");
        for (String r : reds) {
            if (!StringUtils.isNumeric(r) || r.compareTo("34") >= 0 || r.compareTo("00") <= 0) {
                throw new ParamException("投注格式不正确");
            }
        }
        String[] blues = blue.split("\\^");
        for (String b : blues) {
            if (!StringUtils.isNumeric(b) || b.compareTo("17") >= 0 || b.compareTo("00") <= 0) {
                throw new ParamException("投注格式不正确");
            }
        }
        int allNum = getNFromM(6, reds.length) * getNFromM(1, blues.length);
        if (num != allNum) {
            throw new ParamException("投注总注数不正确");
        }
    }

    //总共三个数，每个数字都是从0到9
    private void checkSD(String content, int num) throws ParamException {
        checkPLS(content, num);
    }

    //总共五个数，每个数字都是从0到9
    private void checkPLW(String content, int num) throws ParamException {
        checkPL(content, num, 5);
    }

    //总共三个数，每个数字都是从0到9
    private void checkPLS(String content, int num) throws ParamException {
        checkPL(content, num, 3);
    }

    private void checkPL(String content, int num, int length) throws ParamException {
        String[] contents = content.split("\\|");
        if (contents.length != length) {
            throw new ParamException("投注格式不正确");
        }
        int totalNum = 1;
        for (int i=0; i<contents.length; i++) {
            String part = contents[i];
            if (StringUtils.isBlank(part)) {
                throw new ParamException("投注格式不正确");
            }
            String[] parts = part.split("\\^");
            totalNum = totalNum * parts.length;
            for (String s : parts) {
                if (StringUtils.isBlank(s) || !StringUtils.isNumeric(s) || !isBetween(s, "0", "9")) {
                    throw new ParamException("投注格式不正确");
                }
            }
        }
        if (num != totalNum) {
            throw new ParamException("投注总注数不正确");
        }
    }

    //5个红球最大35，最小01，2个篮球最大12，最小01
    private void checkDLT(String content, int num) throws ParamException, SystermException {
        String[] contents = content.split("\\|");
        if (contents.length != 2) {
            throw new ParamException("投注格式不正确");
        }
        String red = contents[0];
        String blue = contents[1];
        if (StringUtils.isBlank(red) || StringUtils.isBlank(blue)) {
            throw new ParamException("投注格式不正确");
        }
        String[] reds = red.split("\\^");
        for (String r : reds) {
            if (!StringUtils.isNumeric(r) || r.compareTo("36") >= 0 || r.compareTo("00") <= 0) {
                throw new ParamException("投注格式不正确");
            }
        }
        String[] blues = blue.split("\\^");
        for (String b : blues) {
            if (!StringUtils.isNumeric(b) || b.compareTo("13") >= 0 || b.compareTo("00") <= 0) {
                throw new ParamException("投注格式不正确");
            }
        }
        int allNum = getNFromM(5, reds.length) * getNFromM(2, blues.length);
        if (num != allNum) {
            throw new ParamException("投注总注数不正确");
        }
    }


    //从M个数字中筛选N个数字算法
    private int getNFromM(int n, int m) throws SystermException {
        if (n <= 0 || m <= 0) {
            return 0;
        }
        int totalA = 1;
        for (int i=0; i<n; i++) {
            totalA = totalA * (m-i);
        }
        int totalB = 1;
        for (int i=0; i<n; i++) {
            totalB = totalB * (n-i);
        }
        if (totalA % totalB != 0) {
            throw new SystermException("金额计算错误");
        }
        return totalA/totalB;
    }

    private boolean isBetween(String n, String max, String min) {
        int maxInt = Integer.parseInt(max);
        int minInt = Integer.parseInt(min);
        int nInt = Integer.parseInt(n);
        return nInt >= minInt && nInt <= maxInt;
    }

    public static void main(String[] args) {
        System.out.println("35".compareTo("34") <= 0);
    }
}
