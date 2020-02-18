package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.utils.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZerodashApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void shouldCreateAUser(){
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);

        int dbSizeBeforeAdd = userService.allUsers(0, 5).getContent().size();
        userService.createUser(user);
        Page<ZDUser> pagesOfUsers = userService.allUsers(0, 5);
        List<ZDUser> users = pagesOfUsers.getContent();
        int dbSizeAfterAdd = users.size();

        assertThat(dbSizeAfterAdd).isEqualTo(dbSizeBeforeAdd + 1);
        assertThat(user.getEmail()).isEqualTo(users.get(0).getEmail());
        assertThat(user.getPhoneNumber()).isEqualTo(users.get(0).getPhoneNumber());
        assertThat(user.getPassword()).isEqualTo(users.get(0).getPassword());
    }

    @Test
    public void shouldReturnAnExceptionOnExistingEmail(){
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        userService.createUser(user);

        ZDUser anotherUser = new ZDUser();
        anotherUser.setEmail(Constants.TEST_EMAIL);
        anotherUser.setPhoneNumber(Constants.ANOTHER_TEST_PHONE_NUMBER);
        anotherUser.setPassword(Constants.TEST_PASSWORD);
        try {
            userService.createUser(anotherUser);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(Constants.ACCOUNT_ALREADY_EXISTING);
        }

    }

    @Test
    public void shouldReturnAnExceptionOnExistingPhoneNumber(){
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        userService.createUser(user);

        ZDUser anotherUser = new ZDUser();
        anotherUser.setEmail(Constants.ANOTHER_TEST_EMAIL);
        anotherUser.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        anotherUser.setPassword(Constants.TEST_PASSWORD);
        try {
            userService.createUser(anotherUser);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(Constants.ACCOUNT_ALREADY_EXISTING);
        }

    }
}
