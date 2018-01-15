package com.jack.lottery.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaoma3 on 2018/1/15.
 */
public class LotteryStringUtil {

    public static boolean isMobile(String mobile) {
        String regex = "^1[3|4|5|7|8][0-9]\\d{4,8}$";
        if(11 != mobile.length()){
            return false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobile);
            return m.matches();
        }
    }

}
