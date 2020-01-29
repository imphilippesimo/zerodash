package com.zerofiltre.zerodash.dao;

import com.zerofiltre.zerodash.utils.Constants;
import com.zerofiltre.zerodash.ZerodashApplication;
import com.zerofiltre.zerodash.model.ZDUser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZerodashApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserDAOTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    public void testAddUser() {

        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);

        int dbSizeBeforeAdd = userRepository.findAll().size();
        userRepository.save(user);
        List<ZDUser> users = userRepository.findAll();
        int dbSizeAfterAdd = users.size();

        assertThat(dbSizeAfterAdd).isEqualTo(dbSizeBeforeAdd + 1);
        assertThat(user.getEmail()).isEqualTo(users.get(0).getEmail());
        assertThat(user.getPhoneNumber()).isEqualTo(users.get(0).getPhoneNumber());
        assertThat(user.getPassword()).isEqualTo(users.get(0).getPassword());
    }
}
