package com.jack.lottery.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HistoryTask {

    @Scheduled(cron="0 0 0 * * * *")
    private void ssq() {

    }
}
