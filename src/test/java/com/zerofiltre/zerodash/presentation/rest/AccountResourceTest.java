package com.zerofiltre.zerodash.presentation.rest;

import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.service.*;
import com.zerofiltre.zerodash.utils.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AccountResourceTest {

    private UserService userServiceMock = mock(UserService.class);

    private AccountResource accountResource = new AccountResource(userServiceMock);

    @Test
    public void callActivateUser() {
        doReturn(Optional.of(new ZDUser())).when(userServiceMock).activateRegistration(anyString());
        accountResource.activateAccount(Constants.TEST_ACTIVATION_KEY);
        verify(userServiceMock).activateRegistration(Constants.TEST_ACTIVATION_KEY);
    }
}
