package com.jack.lottery.task;

import com.jack.lottery.dao.LotteryTermDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/spring-context.xml"
})
public class FetchHistory {

    @Autowired
    private LotteryTermDao lotteryTermDao;

    @Test
    public void ssq() {
        System.out.println("ssq");
    }
}
