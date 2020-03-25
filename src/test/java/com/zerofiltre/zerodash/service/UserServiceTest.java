package com.zerofiltre.zerodash.service;

import com.zerofiltre.zerodash.dao.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.utils.*;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    private UserRepository userRepositoryMock = mock(UserRepository.class);
    private EmailService emailServiceMock = mock(EmailService.class);

    private UserService userService = new UserService(userRepositoryMock, emailServiceMock, new BCryptPasswordEncoder());


    @Test
    public void shouldCreateAUser() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);

        doReturn(new PageImpl<ZDUser>(new ArrayList<>())).when(userRepositoryMock).findAll(PageRequest.of(0, 5));
        Page<ZDUser> prePagesOfUsers = userService.allUsers(0, 5);
        int dbSizeBeforeAdd = prePagesOfUsers.getContent().size();

        doReturn(Optional.empty()).when(userRepositoryMock).findOneByEmail(anyString());
        doReturn(Optional.empty()).when(userRepositoryMock).findOneByPhoneNumber(anyString());
        doReturn(user).when(userRepositoryMock).save(user);

        doNothing().when(emailServiceMock).sendActivationEmail(any(ZDUser.class));
        userService.createUser(user);

        doReturn(new PageImpl<ZDUser>(Arrays.asList(user))).when(userRepositoryMock).findAll(PageRequest.of(0, 5));
        Page<ZDUser> pagesOfUsers = userService.allUsers(0, 5);
        List<ZDUser> users = pagesOfUsers.getContent();
        int dbSizeAfterAdd = users.size();

        assertThat(dbSizeAfterAdd).isEqualTo(dbSizeBeforeAdd + 1);
        assertThat(user.getEmail()).isEqualTo(users.get(0).getEmail());
        assertThat(user.getPhoneNumber()).isEqualTo(users.get(0).getPhoneNumber());
        assertThat(user.getPassword()).isEqualTo(users.get(0).getPassword());
    }

    @Test
    public void shouldReturnAnExceptionOnExistingEmail() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        doReturn(Optional.empty()).when(userRepositoryMock).findOneByEmail(anyString());
        doReturn(Optional.empty()).when(userRepositoryMock).findOneByPhoneNumber(anyString());
        doReturn(user).when(userRepositoryMock).save(user);
        doNothing().when(emailServiceMock).sendActivationEmail(any(ZDUser.class));
        userService.createUser(user);

        ZDUser anotherUser = new ZDUser();
        anotherUser.setEmail(Constants.TEST_EMAIL);
        anotherUser.setPhoneNumber(Constants.ANOTHER_TEST_PHONE_NUMBER);
        anotherUser.setPassword(Constants.TEST_PASSWORD);
        doReturn(Optional.of(user)).when(userRepositoryMock).findOneByEmail(anyString());
        doReturn(Optional.empty()).when(userRepositoryMock).findOneByPhoneNumber(anyString());
        doNothing().when(emailServiceMock).sendActivationEmail(any(ZDUser.class));
        try {
            userService.createUser(anotherUser);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(Constants.ACCOUNT_ALREADY_EXISTING);
        }

    }

    @Test
    public void shouldReturnAnExceptionOnExistingPhoneNumber() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        doReturn(Optional.empty()).when(userRepositoryMock).findOneByEmail(anyString());
        doReturn(Optional.empty()).when(userRepositoryMock).findOneByPhoneNumber(anyString());
        doReturn(user).when(userRepositoryMock).save(user);
        doNothing().when(emailServiceMock).sendActivationEmail(any(ZDUser.class));
        userService.createUser(user);

        ZDUser anotherUser = new ZDUser();
        anotherUser.setEmail(Constants.ANOTHER_TEST_EMAIL);
        anotherUser.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        anotherUser.setPassword(Constants.TEST_PASSWORD);
        doReturn(Optional.empty()).when(userRepositoryMock).findOneByEmail(anyString());
        doReturn(Optional.of(user)).when(userRepositoryMock).findOneByPhoneNumber(anyString());
        doNothing().when(emailServiceMock).sendActivationEmail(any(ZDUser.class));
        try {
            userService.createUser(anotherUser);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(Constants.ACCOUNT_ALREADY_EXISTING);
        }

    }

    //check that the email service is triggered when user is created
    @Test
    public void createUserShouldTriggerEmailSend() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.ANOTHER_TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        doNothing().when(emailServiceMock).sendActivationEmail(any(ZDUser.class));
        userService.createUser(user);
        verify(emailServiceMock).sendActivationEmail(user);

    }


    //check that the user is effectively activated after the activateUser call
    @Test
    public void shouldActivateUser() {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.ANOTHER_TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        user.setActivationKey(Constants.TEST_ACTIVATION_KEY);
        doReturn(user).when(userRepositoryMock).save(user);
        doReturn(Optional.of(user)).when(userRepositoryMock).findOneByActivationKey(Constants.TEST_ACTIVATION_KEY);
        Optional<ZDUser> activatedUser = userService.activateRegistration(user.getActivationKey());
        if (activatedUser.isPresent())
            user = activatedUser.get();
        assertThat(user.getActivated()).isTrue();


    }

}
