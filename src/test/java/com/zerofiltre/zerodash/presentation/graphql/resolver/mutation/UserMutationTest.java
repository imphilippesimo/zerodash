package com.zerofiltre.zerodash.presentation.graphql.resolver.mutation;

import com.graphql.spring.boot.test.*;
import com.zerofiltre.zerodash.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.service.*;
import com.zerofiltre.zerodash.utils.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit4.*;

import java.io.*;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZerodashApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserMutationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() throws IOException {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/create-user.graphql");
        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.createUser.id")).isNotNull();
        assertThat(response.get("$.data.createUser.role")).isEqualTo(Constants.ROLE_USER);
        //userService.delete(Integer.valueOf(response.get("$.data.createUser.id")));


    }
}
