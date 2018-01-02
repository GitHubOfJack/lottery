package com.jack.lottery.service;

import com.jack.lottery.entity.Test;
import com.jack.lottery.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public void test() {
        Test t = new Test();
        testMapper.insert(t);
    }
}
