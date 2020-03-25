package com.zerofiltre.zerodash.dao;

import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.utils.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDAOTest {

    @Autowired
    private UserRepository userRepository;

    @Test
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

    @Test
    public void testFindUserByUsernameOrEmail() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);

        userRepository.save(user);

        Optional<ZDUser> createdUSer = userRepository.findOneByPhoneNumber(Constants.TEST_PHONE_NUMBER);
        Optional<ZDUser> createdUSer2 = userRepository.findOneByEmail(Constants.TEST_EMAIL);
        assertThat(createdUSer.isPresent()).isTrue();
        assertThat(createdUSer2.isPresent()).isTrue();
        assertThat(createdUSer).isEqualTo(createdUSer2);
        assertThat(createdUSer.get().getEmail()).isEqualTo(Constants.TEST_EMAIL);
        assertThat(createdUSer.get().getPassword()).isEqualTo(Constants.TEST_PASSWORD);

    }
}
