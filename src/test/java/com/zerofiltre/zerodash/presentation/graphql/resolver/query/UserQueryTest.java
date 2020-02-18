package com.zerofiltre.zerodash.presentation.graphql.resolver.query;

import com.graphql.spring.boot.test.*;
import com.zerofiltre.zerodash.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.service.*;
import com.zerofiltre.zerodash.utils.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZerodashApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserQueryTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserQuery userQuery;

    @Test
    @WithMockUser(username = Constants.TEST_EMAIL, roles = "ADMIN")
    public void allUsers() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        userService.createUser(user);
        List<ZDUser> users = userQuery.allUsers(0, 10);
        assertThat(users.get(0).getEmail()).isEqualTo(Constants.TEST_EMAIL);
        assertThat(users.get(0).getPhoneNumber()).isEqualTo(Constants.TEST_PHONE_NUMBER);
        assertThat(users.get(0).getRole()).isEqualTo(Constants.ROLE_USER);

    }
}
