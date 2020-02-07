package com.zerofiltre.zerodash.presentation.security;


import com.graphql.spring.boot.test.*;
import com.zerofiltre.zerodash.presentation.graphql.resolver.mutation.*;
import com.zerofiltre.zerodash.presentation.graphql.resolver.query.*;
import com.zerofiltre.zerodash.utils.*;
import com.zerofiltre.zerodash.utils.error.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit4.*;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GraphqlSecurityTests {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private UserMutation userMutation;

    @Autowired
    private UserQuery userQuery;

    @Test
    @DisplayName("Access Unsecured resource should be OK")
    public void unsecured_resource_ok() throws IOException {
        userMutation.createUser(Constants.TEST_EMAIL, Constants.TEST_PHONE_NUMBER, Constants.TEST_PASSWORD);
    }


    @Test
    @DisplayName("Unauthenticated Access to secured resource should be unauthorized ")
    public void secured_unauthorized_access_throws_exception() {
        Assertions.assertThrows(UnauthenticatedAccessException.class, () ->
                userMutation.securedResource()
        );
    }

    @Test
    @DisplayName("Authenticated Access to secured resource should be OK ")
    @WithMockUser(username = Constants.TEST_EMAIL)
    public void secured_ok() throws IOException {
        userMutation.securedResource();
    }

    @Test
    @DisplayName("Unauthenticated Access to admin secured resource should be unauthorized ")
    @WithMockUser(username = Constants.TEST_EMAIL)
    public void admin_unauthorized_access_throws_exception() {
        Assertions.assertThrows(UnauthenticatedAccessException.class, () -> {
            userQuery.allUsers(0, 10);
        });
    }


    @Test
    @DisplayName("Unauthorized Access to admin secured resource should be forbidden ")
    @WithMockUser(username = Constants.TEST_EMAIL)
    public void without_admin_role_throws_exception() {
        Assertions.assertThrows(UnauthenticatedAccessException.class, () -> {
            userQuery.allUsers(0, 10);
        });
    }

    @Test
    @DisplayName("Admin Authorized Access to admin secured resource should be OK ")
    @WithMockUser(username = Constants.TEST_EMAIL, roles = "ADMIN")
    public void admin_role_ok() {
        userQuery.allUsers(0, 10);

    }
}
