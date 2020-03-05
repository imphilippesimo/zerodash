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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class GraphQLSecurityTests {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private UserMutation userMutation;

    @Autowired
    private UserQuery userQuery;

    @Test
    @DisplayName("Access Unsecured resource should be OK")
    public void unsecuredResourceOk() {
        userMutation.createUser(Constants.TEST_EMAIL, Constants.TEST_PHONE_NUMBER, Constants.TEST_PASSWORD);
    }


    @Test
    @DisplayName("Unauthenticated Access to secured resource should be unauthorized ")
    public void securedUnauthorizedAccessThrowsException() {
        Assertions.assertThrows(UnauthenticatedAccessException.class, () -> userMutation.securedResource());
    }

    @Test
    @DisplayName("Authenticated Access to secured resource should be OK ")
    @WithMockUser(username = Constants.TEST_EMAIL)
    public void securedOk() {
        userMutation.securedResource();
    }

    @Test
    @DisplayName("Unauthenticated Access to admin secured resource should be unauthorized ")
    @WithMockUser(username = Constants.TEST_EMAIL)
    public void adminUnauthorizedAccessThrowsException() {
        Assertions.assertThrows(UnauthenticatedAccessException.class, () -> userQuery.allUsers(0, 10));
    }


    @Test
    @DisplayName("Unauthorized Access to admin secured resource should be forbidden ")
    @WithMockUser(username = Constants.TEST_EMAIL)
    public void withoutAdminRoleThrowsException() {
        Assertions.assertThrows(UnauthenticatedAccessException.class, () -> userQuery.allUsers(0, 10));
    }

    @Test
    @DisplayName("Admin Authorized Access to admin secured resource should be OK ")
    @WithMockUser(username = Constants.TEST_EMAIL, roles = "ADMIN")
    public void adminRoleOk() {
        userQuery.allUsers(0, 10);

    }
}
