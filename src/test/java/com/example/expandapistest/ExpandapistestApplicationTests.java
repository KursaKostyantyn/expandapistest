package com.example.expandapistest;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.expandapistest.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExpandapistestApplicationTests {
    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
    }

}
