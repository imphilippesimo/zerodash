package com.zerofiltre.zerodash.presentation.graphql.resolver.query;

import com.zerofiltre.zerodash.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.service.*;
import com.zerofiltre.zerodash.utils.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZerodashApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserQueryTest {

    private UserService userServiceMock = mock(UserService.class);
    private UserQuery userQuery = new UserQuery(userServiceMock);

    @Test
    @WithMockUser(username = Constants.TEST_EMAIL, roles = "ADMIN")
    public void allUsers() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);

        doReturn(new PageImpl<ZDUser>(Arrays.asList(user))).when(userServiceMock).allUsers(0, 10);

        List<ZDUser> users = userQuery.allUsers(0, 10);
        assertThat(users.get(0).getEmail()).isEqualTo(Constants.TEST_EMAIL);
        assertThat(users.get(0).getPhoneNumber()).isEqualTo(Constants.TEST_PHONE_NUMBER);

    }
}
