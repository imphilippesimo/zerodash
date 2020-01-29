package com.zerofiltre.zerodash.presentation.query;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.zerofiltre.zerodash.utils.Constants;
import com.zerofiltre.zerodash.ZerodashApplication;
import com.zerofiltre.zerodash.model.ZDUser;
import com.zerofiltre.zerodash.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZerodashApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserQueryTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void allUsers() throws Exception {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        userService.createUser(user);
        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/all-users.graphql");
        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.allUsers[0].email")).isEqualTo(Constants.TEST_EMAIL);
        assertThat(response.get("$.data.allUsers[0].phoneNumber")).isEqualTo(Constants.TEST_PHONE_NUMBER);
        assertThat(response.get("$.data.allUsers[0].role")).isEqualTo(Constants.ROLE_USER);
        userService.delete(user.getId());

    }
}
