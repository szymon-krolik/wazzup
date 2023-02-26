package com.wazzup;

import com.wazzup.controller.UserController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {

    @Autowired
    private UserController userController;
    @Test
    void contextLoads() {
        Assertions.assertThat(userController).isNotNull();
    }

}
