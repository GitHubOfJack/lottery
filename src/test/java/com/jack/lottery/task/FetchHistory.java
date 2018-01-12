package com.jack.lottery.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/spring-context.xml"
})
public class FetchHistory {
    @Test
    public void ssq() {
        System.out.println("ssq");
    }
}
