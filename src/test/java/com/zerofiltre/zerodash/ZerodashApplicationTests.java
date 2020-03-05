package com.zerofiltre.zerodash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.*;

@SpringBootTest(classes = ZerodashApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ZerodashApplicationTests {

    @Test
    void contextLoads() {
        //Just to test if the context is loading successfully
    }

}
