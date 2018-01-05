package com.jack.lottery.utils;

import java.util.Random;

public class RandomUtils {
    public static String getRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<length; i++) {
            Random random = new Random();
            int r = random.nextInt(10);
            sb.append(r);
        }
        return sb.toString();
    }
}
